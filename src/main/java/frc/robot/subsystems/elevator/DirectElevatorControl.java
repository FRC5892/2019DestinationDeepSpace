package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class DirectElevatorControl extends Command {

    DirectElevatorControl() {
        requires(Robot.elevator);
    }

    private static final double WINCH_SPEED_UP = 0.7;
    private static final double WINCH_SPEED_DOWN = -0.2;

    @Override
    protected void execute() {
        if (Robot.oi.copilot.getRawButton(1)) {
            Robot.elevator.setWinchSpeed(WINCH_SPEED_UP);
        } else if (Robot.oi.copilot.getRawButton(2)) {
            Robot.elevator.setWinchSpeed(WINCH_SPEED_DOWN);
        } else {
            Robot.elevator.setWinchSpeed(0);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.elevator.setWinchSpeed(0);
    }
}