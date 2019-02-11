package frc.robot.subsystems.intake;

import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class IntakeSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = true;

    private final SpeedController hatchGrabbers;
    private final SpeedController cargoGrabbers;
    private final TalonSRX wrist;

    public IntakeSubsystem() {
        hatchGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeHatchGrabbers);
        cargoGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeCargoGrabbers);
        wrist = new TalonSRX(Robot.map.intakeWrist);
        try {
            TalonUtils.readPID(wrist, "IntakeWrist", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for intake wrist.", e.getStackTrace());
        }
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void setHatchGrabbers(double speed) {
        hatchGrabbers.set(speed);
    }

    public void setCargoGrabbers(double speed) {
        cargoGrabbers.set(speed);
    }

    public void setWristSetpoint(double target) {
        wrist.set(ControlMode.Position, target);
    }
}
