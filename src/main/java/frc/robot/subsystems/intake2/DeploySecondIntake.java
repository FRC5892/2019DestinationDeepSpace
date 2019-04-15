package frc.robot.subsystems.intake2;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class DeploySecondIntake extends InstantCommand {
    private final Value value;

    public DeploySecondIntake(Value value) {
        requires(Robot.intake2);
        this.value = value;
    }

    @Override
    protected void execute() {
        Robot.intake2.setPiston(value);
    }
}