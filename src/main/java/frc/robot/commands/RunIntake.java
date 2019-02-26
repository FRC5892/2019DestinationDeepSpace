package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RunIntake extends Command {
    private final double hatch, cargo, wrist;

    public RunIntake(double hatch, double cargo, double wrist) {
        requires(Robot.intake);
        this.hatch = hatch;
        this.cargo = cargo;
        this.wrist = wrist;
    }

    @Override
    protected void execute() {
        Robot.intake.setHatchGrabbers(hatch);
        Robot.intake.setCargoGrabbers(cargo);
        Robot.intake.setWristSpeed(wrist);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.intake.setHatchGrabbers(0);
        Robot.intake.setCargoGrabbers(0);
        Robot.intake.setWristSpeed(0);
    }
}