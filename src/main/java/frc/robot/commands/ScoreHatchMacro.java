package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.BuildableCommandGroup;

public class ScoreHatchMacro extends CommandGroup {
    public ScoreHatchMacro() {
        //addParallel(new SetIntakeSetpoint(IntakeSubsystem.DOWN_SETPOINT));
        /*addSequential(new BuildableCommandGroup((cg) -> {
            cg.addParallel(new RunIntake(0, 0, -1));
            //cg.addParallel(new AutoTankDrive(-1, -1, 1.0 / 5));
        }), 0.1);*/
        addSequential(new BuildableCommandGroup((cg) -> {
            cg.addParallel(new AutoTankDrive(-1, -1, 1.0 / 5), 0.4);
            cg.addSequential(new WaitCommand(0));
            cg.addParallel(new RunIntake(1, 0, -1), 0.375);
        }));
    }
}