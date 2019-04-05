package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class HatchAndElevateMacro extends CommandGroup {
    public HatchAndElevateMacro() {
        addParallel(new RunIntake(0.7, 0, 0), 0.4);
        addParallel(new RunElevator(-0.15), 0.4);
    }
}