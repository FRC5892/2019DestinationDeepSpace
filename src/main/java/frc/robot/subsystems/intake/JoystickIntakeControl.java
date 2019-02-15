package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickIntakeControl extends Command {

    JoystickIntakeControl() {
        requires(Robot.intake);
    }

    private static final double MANUAL_WRIST_SPEED = 0.5;

    @Override
    protected void execute() {
        Robot.intake.setHatchGrabbers(Robot.oi.pilot.getRawAxis(3) - Robot.oi.pilot.getRawAxis(2));
        Robot.intake.setCargoGrabbers(Robot.oi.pilot.getRawAxis(3) - Robot.oi.pilot.getRawAxis(2));
        if (Robot.oi.pilot.getRawButton(1)) {
            Robot.intake.setWristSpeed(-MANUAL_WRIST_SPEED);
        } else if (Robot.oi.pilot.getRawButton(4)) {
            Robot.intake.setWristSpeed(MANUAL_WRIST_SPEED);
        } else if (!Robot.intake.wristIsOnSetpoint()) {
            Robot.intake.setWristSpeed(0);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}