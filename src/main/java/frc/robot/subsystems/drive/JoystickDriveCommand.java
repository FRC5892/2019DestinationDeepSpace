package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickDriveCommand extends Command {

    JoystickDriveCommand() {
        requires(Robot.drive);
    }

    private static final double NORMAL_FACTOR = 0.8;
    private static final double SLOW_FACTOR = 0.5;
    private boolean toggleLastFrame = false;

    @Override
    protected void execute() {
        if (Robot.oi.pilot.getRawButton(2) && !toggleLastFrame) {
            Robot.drive.manualSlow = !Robot.drive.manualSlow;
            toggleLastFrame = true;
        } else if (!Robot.oi.pilot.getRawButton(2)) {
            toggleLastFrame = false;
        }
        var factor = Robot.drive.manualSlow ? SLOW_FACTOR : NORMAL_FACTOR;
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
