package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.drive.DriveSubsystem.SpeedMode;

class JoystickDriveCommand extends Command {

    JoystickDriveCommand() {
        requires(Robot.drive);
    }

    private boolean toggleLastFrame = false;

    @Override
    protected void execute() {
        if (Robot.oi.pilot.getRawButton(2) && !toggleLastFrame) {
            Robot.drive.speedMode = Robot.drive.speedMode == SpeedMode.SLOW ? SpeedMode.NORMAL : SpeedMode.SLOW;
            toggleLastFrame = true;
        } else if (Robot.oi.pilot.getRawButton(3) && !toggleLastFrame) {
            Robot.drive.speedMode = Robot.drive.speedMode == SpeedMode.KILL ? SpeedMode.NORMAL : SpeedMode.KILL;
            toggleLastFrame = true;
        } else if (!Robot.oi.pilot.getRawButton(2) && !Robot.oi.pilot.getRawButton(3)) {
            toggleLastFrame = false;
        }
        var factor = Robot.drive.speedMode.speedFactor;
        Robot.drive.arcadeDrive(-Robot.oi.pilot.getRawAxis(1) * factor, Robot.oi.pilot.getRawAxis(4) * factor);
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
