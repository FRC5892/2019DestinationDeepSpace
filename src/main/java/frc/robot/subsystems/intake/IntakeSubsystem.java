package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class IntakeSubsystem extends Subsystem {

    private final SpeedController hatchGrabbers;
    private final SpeedController cargoGrabbers;
    private TalonSRX wrist;

    public IntakeSubsystem() {
        hatchGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeHatchGrabbers);
        cargoGrabbers = RobotMap.makeVictorGroup(Robot.map.intakeCargoGrabbers);
        wrist = new TalonSRX(Robot.map.intakeWrist);
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
