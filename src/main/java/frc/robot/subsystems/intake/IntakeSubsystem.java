package frc.robot.subsystems.intake;

import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class IntakeSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = true;

    private static final double HATCH_GRABBER_SPEED = 1;

    private final SpeedController hatchGrabbers;
    private final DigitalInput hatchLimitSwitch;
    private final SpeedController cargoGrabbers;
    private final TalonSRX wrist;

    private int hatchDirection = 0;

    public IntakeSubsystem() {
        hatchGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeHatchGrabbers);
        hatchLimitSwitch = new DigitalInput(Robot.map.intakeHatchLimitSwitch);
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

    public void resetState() {
        hatchDirection = 0;
    }

    public void setHatchDirection(int dir) {
        hatchDirection = (dir > 0) ? 1 : ((dir < 0) ? -1 : 0);
    }

    public void setCargoGrabbers(double speed) {
        cargoGrabbers.set(speed);
    }

    public void setWristSetpoint(double target) {
        wrist.set(ControlMode.Position, target);
    }

    @Override
    public void periodic() {
        switch (hatchDirection) {
            case 0:
                hatchGrabbers.stopMotor();
                break;
            case 1:
                if (hatchLimitSwitch.get()) {
                    hatchGrabbers.stopMotor();
                } else {
                    hatchGrabbers.set(HATCH_GRABBER_SPEED);
                }
                break;
            case -1:
                hatchGrabbers.set(-HATCH_GRABBER_SPEED);
                break;
        }
    }
}
