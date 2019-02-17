package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class ScoreHatchMacro extends CommandGroup {
    public ScoreHatchMacro() {
        //addParallel(new SetIntakeSetpoint(IntakeSubsystem.DOWN_SETPOINT));
        addParallel(new RunIntake(1, 0, 1), 0.3);
        addSequential(new AutoTankDrive(-1, -1, 1.0 / 3));
    }
}