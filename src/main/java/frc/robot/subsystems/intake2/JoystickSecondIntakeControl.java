package frc.robot.subsystems.intake2;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

// I got told this was a joke after already writing all the code for it.
// So screw it, we now have all the necessary code to strap on 1477's hatch mech.
public class JoystickSecondIntakeControl extends Command {

    public JoystickSecondIntakeControl() {
        requires(Robot.intake2);
    }

    @Override
    protected void execute() {
        if (Robot.oi.pilot.getRawButton(5)) {
            Robot.intake2.setPiston(Value.kForward);
        } else if (Robot.oi.pilot.getRawButton(6)) {
            Robot.intake2.setPiston(Value.kReverse);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}