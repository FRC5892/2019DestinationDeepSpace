package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

import java.io.IOException;

public class IntakeSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = true;
    public static final double UP_SETPOINT = 0;
    public static final double MID_SETPOINT = -26500;
    public static final double DOWN_SETPOINT = -33000;

    private final SpeedController hatchGrabbers, cargoGrabbers;
    private final DoubleSolenoidGroup pistons;
    @SuppressWarnings("unused")
    private final DigitalInput hatchLimitSwitch, cargoLimitSwitch;
    private final TalonSRX wrist;

    public boolean cargoMode = false;

    public IntakeSubsystem() {
        hatchGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeHatchGrabbers);
        cargoGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeCargoGrabbers);
        pistons = RobotMap.makeDoubleSolenoidGroup(Robot.map.intakePistons);
        hatchLimitSwitch = new DigitalInput(Robot.map.intakeHatchLimitSwitch);
        cargoLimitSwitch = new DigitalInput(Robot.map.intakeCargoLimitSwitch);
        wrist = new TalonSRX(Robot.map.intakeWrist);
        wrist.setSensorPhase(false); // god that method is horribly named.
        try {
            TalonUtils.readPID(wrist, "IntakeWrist", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for intake wrist.", e.getStackTrace());
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickIntakeControl());
    }

    // I can do the stop on limit logic here because these have to be called every frame re: MotorSafety
    public void setHatchGrabbers(double speed) {
        //if (!hasHatch() && !hasCargo()) {
            hatchGrabbers.set(speed);
        /*} else {
            hatchGrabbers.stopMotor();
        }*/
    }

    public void setCargoGrabbers(double speed) {
        //if (!hasHatch() && !hasCargo()) {
            cargoGrabbers.set(speed);
        /*} else {
            cargoGrabbers.stopMotor();
        }*/
    }

    public boolean hasHatch() {
        //return hatchLimitSwitch.get();
        return false;
    }

    public boolean hasCargo() {
        return cargoLimitSwitch.get();
        //return false;
    }

    public void setWristSpeed(double speed) {
        wrist.set(ControlMode.PercentOutput, speed);
    }

    public void setWristSetpoint(double target) {
        wrist.set(ControlMode.Position, target);
    }

    public void resetWristSensor() {
        wrist.setSelectedSensorPosition(0);
    }

    public boolean wristIsOnSetpoint() {
        return wrist.getControlMode() == ControlMode.Position;
    }

    public void setPistons(Value value) {
        pistons.set(value);
    }
}
