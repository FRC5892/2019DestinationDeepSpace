package frc.robot.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

import java.io.IOException;

public class ElevatorSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = true;

    public static final double BOTTOM = 0;
    public static final double HATCH_2 = 20000;
    public static final double HATCH_3 = 38000;

    private final TalonSRX winch;
    private final SpeedController winchSupport;
    private final DigitalInput topLimitSwitch, bottomLimitSwitch;

    public ElevatorSubsystem() {
        winch = new TalonSRX(Robot.map.elevatorWinch);
        winch.setSensorPhase(true);
        try {
            TalonUtils.readPID(winch, "ElevatorWinch", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for elevator winch.", e.getStackTrace());
        }
        winchSupport = RobotMap.makeVictorGroup(Robot.map.elevatorWinchSupport);
        topLimitSwitch = new DigitalInput(Robot.map.elevatorTopLimitSwitch);
        bottomLimitSwitch = new DigitalInput(Robot.map.elevatorBottomLimitSwitch);

        winch.configPeakOutputReverse(-0.2);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DirectElevatorControl());
    }

    public void setWinchSpeed(double speed) {
        if (speed > 0 && topLimitSwitchTriggered()) {
            winch.set(ControlMode.PercentOutput, 0);
            return;
        }
        winch.set(ControlMode.PercentOutput, speed);
    }

    public void setWinchSetpoint(double target) {
        winch.set(ControlMode.Position, target);
    }

    public boolean winchIsOnSetpoint() {
        return winch.getControlMode() != ControlMode.PercentOutput;
    }

    
    public void resetEncoder() {
        winch.setSelectedSensorPosition(0);
    }

    public boolean topLimitSwitchTriggered() {
        return !topLimitSwitch.get();
    }

    public boolean bottomLimitSwitchTriggered() {
        return bottomLimitSwitch.get();
    }

    @Override
    public void periodic() {
        // lol discount following
        // i guess i could ask them to transplant one of the Victor SPXs from the practice bot
        // but like
        // this works
        winchSupport.set(winch.getMotorOutputPercent());
    }
}