package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.BuildableCommandGroup;

public class ScoreHatchMacro extends CommandGroup {
    public ScoreHatchMacro() {
        //addParallel(new SetIntakeSetpoint(IntakeSubsystem.DOWN_SETPOINT));
        addParallel(new RunIntake(1, 0, -1), 0.375);
        //addParallel(new AutoTankDrive(-1, -1, 1.0 / 5), 0.4);
        addParallel(new BuildableCommandGroup((cg) -> {
            cg.addSequential(new WaitCommand(0));
            cg.addSequential(new AutoTankDrive(-1, -1, 1.0 / 5), 0.4);
        }));
    }
}