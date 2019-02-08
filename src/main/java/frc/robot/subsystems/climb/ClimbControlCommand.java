package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class ClimbControlCommand extends Command {

    ClimbControlCommand() {
        requires(Robot.climb);
    }

    @Override
    protected void execute() {
        Robot.climb.setArms(0 /* TODO controller */);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.climb.setArms(0);
    }
}
