package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RunIntake extends Command {
    private final double hatch, cargo;

    public RunIntake(double hatch, double cargo) {
        requires(Robot.intake);
        this.hatch = hatch;
        this.cargo = cargo;
    }

    @Override
    protected void execute() {
        Robot.intake.setHatchGrabbers(hatch);
        Robot.intake.setCargoGrabbers(cargo);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.intake.setHatchGrabbers(0);
        Robot.intake.setCargoGrabbers(0);
    }
}