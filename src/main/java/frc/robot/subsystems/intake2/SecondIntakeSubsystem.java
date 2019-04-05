package frc.robot.subsystems.intake2;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.robot.RobotMap;

// I got told this was a joke after already writing all the code for it.
// So screw it, we now have all the necessary code to strap on 1477's hatch mech.
public class SecondIntakeSubsystem extends Subsystem {
    private final DoubleSolenoidGroup piston;

    public SecondIntakeSubsystem() {
        piston = RobotMap.makeDoubleSolenoidGroup(null);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickSecondIntakeControl());
    }

    public void setPiston(Value value) {
        piston.set(value);
    }
}