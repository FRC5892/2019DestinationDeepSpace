package frc;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDInterface;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * A simple class for implementing PID that allows simple tuning using NetworkTables (and Shuffleboard).
 * Based on <a href="http://brettbeauregard.com/blog/2011/04/improving-the-beginners-pid-introduction/">http://brettbeauregard.com/blog/2011/04/improving-the-beginners-pid-introduction/</a>
 * <aside>Note that I did not implement the final step because the entire purpose of that is to negate the output. If you need negated output, negate it yourself.</aside>
 * 
 * @author Kai Page
 */

public abstract class HEROicPIDController implements PIDInterface {

    private double m_setpoint;
    private double m_iTerm = 0, m_lastInput;
    private double m_kP, m_kI, m_kD;
    private double m_kI_pre, m_kD_pre;
    /** Sample time in ms. */
    public static final int SAMPLE_TIME = 50;
    private double m_outMin, m_outMax;
    private boolean m_minmaxClip = false;
    private boolean m_enabled = false;
    private boolean m_computedLastTime;

    private static final ComputationSkipper SKIPPER = new ComputationSkipper();

    private TimerTask m_timerTask;
    private Timer m_timer;

    private final boolean tuningMode;
    private NetworkTableEntry nt_kP, nt_kI, nt_kD, nt_input, nt_error, nt_output, nt_skipComputation;
    private Logger logger;

    public HEROicPIDController(boolean tuningMode) {
        /*var table = NetworkTableInstance.getDefault().getTable("PID").getSubTable(getClass().getSimpleName());
        nt_kP = table.getEntry("kP");
        nt_kI = table.getEntry("kI");
        nt_kD = table.getEntry("kD");
        nt_input = table.getEntry("input");
        nt_error = table.getEntry("error");
        nt_output = table.getEntry("output");
        nt_skipComputation = table.getEntry("skipComputation");*/

        this.tuningMode = tuningMode; // don't need to updateNTGains() here because loading does that for us
        try {
            loadGainsFromFile(getClass().getSimpleName());
        } catch (IOException e) {
            DriverStation.reportWarning(getClass().getSimpleName() + " couldn't open its gain file.", false);
        }

        if (tuningMode) {
			var tab = Shuffleboard.getTab(getClass().getSimpleName());
			nt_kP = tab.add("kP", m_kP).withPosition(0, 0).getEntry();
			nt_kI = tab.add("kI", m_kI).withPosition(0, 1).getEntry();
			nt_kD = tab.add("kD", m_kD).withPosition(0, 2).getEntry();
			nt_input = tab.add("Input", 0).withPosition(1, 0).getEntry();
			nt_error = tab.add("Error", 0).withPosition(1, 1).getEntry();
			nt_output = tab.add("Output", 0).withPosition(1, 2).getEntry();
            nt_skipComputation = tab.add("Computation Skipped", false).withPosition(0, 3).withSize(2, 1).getEntry();
            nt_kP.setDouble(-1);
            nt_kI.setDouble(-1);
            nt_kD.setDouble(-1);
			updateNTGains();

            logger = Logger.getLogger(getClass().getSimpleName());
            synchronized(logger) {
                if (logger.getHandlers().length < 1) {
                    try {
                        var directory = new File("/home/lvuser/PID-logs");
                        if (!directory.exists()) directory.mkdir();
                        logger.addHandler(new FileHandler("/home/lvuser/PID-logs/" + getClass().getSimpleName() + ".log", true));
                    } catch (IOException e) {
                        DriverStation.reportWarning(getClass().getSimpleName() + " couldn't open its log file.", e.getStackTrace());
                    }
                }
            }
        }

        m_timerTask = new TimerTask() {
            @Override
            public void run() {
                loop();
            }
        };
        m_timer = new Timer(true);
        m_timer.schedule(m_timerTask, SAMPLE_TIME, SAMPLE_TIME);
    }

    /**
     * Gets whether the controller is in NetworkTable tuning mode.
     * 
     * @return Whether the controller is in NetworkTable tuning mode.
     */
    public boolean getTuningMode() {
        return tuningMode;
    }

    /**
     * Update the NetworkTable with the current PID gains if we are in tuning mode.
     */
    private void updateNTGains() {
        if (tuningMode && nt_kP != null) {
            nt_kP.setDouble(m_kP);
            nt_kI.setDouble(m_kI);
            nt_kD.setDouble(m_kD);
        }
    }

    /**
     * This method is called periodically to get the current input value.
     * @param skipper Throw this to skip the computation step, and call onSkipComputation instead of usePIDOutput.
     * @return The current input value.
     */
    public abstract double getPIDInput(ComputationSkipper skipper);

    /**
     * This method is called periodically to use the current output value.
     * @param output The current output value.
     */
    public abstract void usePIDOutput(double output);

    protected static class ComputationSkipper extends RuntimeException {
        private ComputationSkipper() {}
        private static final long serialVersionUID = 254L;
    }

    /**
     * This method is called in lieu of usePIDOutput whenever getPIDInput skips the computation.
     */
    public void onSkipComputation() {}

    /**
     * This method is called periodically. To be more precise, about every {@link SAMPLE_TIME} ms.
     */
    protected void loop() {
        if (m_enabled) {
            try {
                var input = getPIDInput(SKIPPER);
                if (!m_computedLastTime) {
                    initialize(input);
                }
                var output = compute(getPIDInput(SKIPPER));
                if (tuningMode) {
                    nt_input.setDouble(input);
                    nt_error.setDouble(m_setpoint - input);
                    nt_output.setDouble(output);
                    nt_skipComputation.setBoolean(false);
                    setPID(nt_kP.getDouble(m_kP), nt_kI.getDouble(m_kI), nt_kD.getDouble(m_kD));
                }
                usePIDOutput(output);
            } catch (ComputationSkipper cs) {
                onSkipComputation();
                if (tuningMode) {
                    nt_skipComputation.setBoolean(true);
                }
                m_computedLastTime = false;
            }
        }
    }

    /**
     * Computes the current output value from the current input value.
     * @param input The current input value, as returned by {@link getPIDInput}.
     * @return The current output value, to be used by {@link usePIDOutput}.
     */
    protected double compute(double input) {
        double error = m_setpoint - input;
        m_iTerm = minmaxClip(m_iTerm + m_kI_pre * error);
        double dInput = (input - m_lastInput);

        /* Compute PID Output */
        double output = minmaxClip(m_kP * error + m_iTerm - m_kD_pre * dInput);

        m_lastInput = input;

        return output;
    }

    /**
     * Sets the current proportional, integral, and derivative constants, respectively.
     */
    public void setPID(double kP, double kI, double kD) {
        this.m_kP = kP;
        this.m_kI = kI;
        m_kI_pre = kI * SAMPLE_TIME / 1000;
        this.m_kD = kD;
        m_kD_pre = kD / SAMPLE_TIME / 1000;
        updateNTGains();
    }

    /**
     * Loads the PID constants from a text file under the "deploy/PID" directory.
     */
    private void loadGainsFromFile(String name) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("/home/lvuser/deploy/PID/" + name + ".txt"));
        try {
            var kP = Double.parseDouble(reader.readLine());
            var kI = Double.parseDouble(reader.readLine());
            var kD = Double.parseDouble(reader.readLine());
            setPID(kP, kI, kD);
        } finally {
            reader.close();
        }
    }

    /**
     * Sets symmetrical output limits with the given absolute value.
     */
    public void setOutputLimits(double abs) {
        setOutputLimits(-Math.abs(abs), Math.abs(abs));
    }

    /**
     * Sets the limits on output for the PID function.
     */
    public void setOutputLimits(double min, double max) {
        if (min > max)
            return;
        m_minmaxClip = true;
        m_outMin = min;
        m_outMax = max;
    }

    /**
     * Unsets the limits on output for the PID function.
     */
    public void resetOutputLimits() {
        m_minmaxClip = false;
    }

    /**
     * Sets the value towards which the PID function will go.
     */
    public void setSetpoint(double setpoint) {
        this.m_setpoint = setpoint;
    }

    private void initialize(double input) {
        m_computedLastTime = true;
        m_lastInput = input;
        m_iTerm = minmaxClip(compute(input));
    }

    /**
     * Enables the controller.
     */
    public void enable() {
        if (!m_enabled) {
            try {
                initialize(getPIDInput(SKIPPER));
            } catch (ComputationSkipper cs) {
                m_computedLastTime = false;
            }
            m_iTerm = 0;
            m_enabled = true;
            if (tuningMode) {
                logger.info(String.format("enabled with kP: %f, kI: %f, kD: %f", m_kP, m_kI, m_kD));
            }
        }
        m_enabled = true;
    }

    /**
     * Disables the controller.
     */
    public void disable() {
        m_enabled = false;
    }

    /**
     * Clips value between the current minimum and maximum.
     * @param value The value to be clipped.
     * @return The current maximum if value was higher, minimum if lower, value otherwise.
     */
    protected double minmaxClip(double value) {
        if (m_minmaxClip) {
            if (value < m_outMin)
                return m_outMin;
            if (value > m_outMax)
                return m_outMax;
        }
        return value;
    }

    public double getLastInput() {
        return m_lastInput;
    }

    @Override
    public double getP() {
        return m_kP;
    }

    @Override
    public double getI() {
        return m_kI;
    }

    @Override
    public double getD() {
        return m_kD;
    }

    @Override
    public double getSetpoint() {
        return m_setpoint;
    }

    @Override
    public double getError() {
        return m_setpoint - m_lastInput;
    }

    @Override
    public void reset() {
        disable();
    }
}