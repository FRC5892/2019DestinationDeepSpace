package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickDriveCommand extends Command {

    JoystickDriveCommand() {
        requires(Robot.drive);
    }

    @Override
    protected void execute() {
        Robot.drive.arcadeDrive(-Robot.oi.pilot.getRawAxis(1), Robot.oi.pilot.getRawAxis(4));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.drive.stop();
    }
}
