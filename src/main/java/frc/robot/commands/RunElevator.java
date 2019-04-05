package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RunElevator extends Command {
    private final double speed;

    public RunElevator(double speed) {
        requires(Robot.elevator);
        this.speed = speed;
    }

    @Override
    protected void execute() {
        Robot.elevator.setWinchSpeed(speed);
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