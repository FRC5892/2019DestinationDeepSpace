package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class DirectElevatorControl extends Command {

    DirectElevatorControl() {
        requires(Robot.elevator);
    }

    private static final double WINCH_SPEED_UP = 1;
    private static final double WINCH_SPEED_DOWN = 1;

    @Override
    protected void execute() {
        var input = -Robot.oi.copilot.getRawAxis(1);
        if (input > 0.15) {
            Robot.elevator.setWinchSpeed(WINCH_SPEED_UP * input);
        } else if (input < -0.15) {
            Robot.elevator.setWinchSpeed(WINCH_SPEED_DOWN * input);
        } else if (!Robot.elevator.winchIsOnSetpoint()) {
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