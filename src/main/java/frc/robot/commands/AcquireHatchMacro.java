package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AcquireHatchMacro extends CommandGroup {
    public AcquireHatchMacro() {
        //addParallel(new SetIntakeSetpoint(IntakeSubsystem.UP_SETPOINT));
        addParallel(new RunIntake(-1, 0, -1), 0.2);
    }
}