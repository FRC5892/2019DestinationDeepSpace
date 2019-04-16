package frc.robot.subsystems.intake2;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class SecondIntakeSubsystem extends Subsystem {
    private final DoubleSolenoidGroup piston;

    public SecondIntakeSubsystem() {
        piston = RobotMap.makeDoubleSolenoidGroup(Robot.map.secondIntakePiston);
    }

    @Override
    protected void initDefaultCommand() {
        //setDefaultCommand(new JoystickSecondIntakeControl());
    }

    public void setPiston(Value value) {
        piston.set(value);
    }
}