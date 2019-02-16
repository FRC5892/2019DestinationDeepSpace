package frc.robot.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class SetIntakeSetpoint extends InstantCommand {
    private final double target;

    public SetIntakeSetpoint(double target) {
        this.target = target;
    }

    @Override
    protected void execute() {
        Robot.intake.setWristSetpoint(target);
    }
}