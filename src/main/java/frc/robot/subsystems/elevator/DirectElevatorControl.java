package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class DirectElevatorControl extends Command {

    DirectElevatorControl() {
        requires(Robot.elevator);
    }

    private static final double WINCH_SPEED_UP = 1;
    private static final double WINCH_SPEED_UP_SLOW = 0.5;

    private boolean toggleLastFrame = false;

    private static final double WINCH_SPEED_DOWN = 0.4;

    @Override
    protected void execute() {
        if (Robot.oi.copilot.getRawButton(2) && !toggleLastFrame) {
            Robot.elevator.slowUp = !Robot.elevator.slowUp;
            toggleLastFrame = true;
        } else if (!Robot.oi.copilot.getRawButton(2)) {
            toggleLastFrame = false;
        }

        var input = -Robot.oi.copilot.getRawAxis(1);
        if (input > 0.15) {
            Robot.elevator.setWinchSpeed((Robot.elevator.slowUp ? WINCH_SPEED_UP_SLOW : WINCH_SPEED_UP) * input);
        } else if (input < -0.15) {
            Robot.elevator.setWinchSpeed(WINCH_SPEED_DOWN * input);
        /*} else if (Robot.oi.copilot.getRawButton(1)) {
            Robot.elevator.setWinchSetpoint(ElevatorSubsystem.BOTTOM); // starting position
        } else if (Robot.oi.copilot.getRawButton(2)) {
            Robot.elevator.setWinchSetpoint(ElevatorSubsystem.HATCH_2); // "acquisition position"
        } else if (Robot.oi.copilot.getRawButton(4)) {
            Robot.elevator.setWinchSetpoint(ElevatorSubsystem.HATCH_3); // loading station position*/
        } else if (/*!Robot.elevator.winchIsOnSetpoint()*/ true) {
            Robot.elevator.setWinchSpeed(0);
        }

        if (Robot.oi.copilot.getRawButton(3)) {
            Robot.elevator.resetEncoder();
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