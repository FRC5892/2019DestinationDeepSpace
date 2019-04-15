package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.intake2.DeploySecondIntake;

public class OI {
    public Joystick pilot = new Joystick(0);
    public Joystick copilot = new Joystick(1);

    @SuppressWarnings("resource")
    public OI() {
        //new JoystickButton(pilot, 9).whenActive(new AcquireHatchMacro());
        new JoystickButton(pilot, 10).whenActive(new ScoreHatchMacro());
        //new JoystickButton(pilot, 5).whileActive(new AlignWithVisionTargets(-1, -1));
        new JoystickButton(pilot, 5).whenActive(new DeploySecondIntake(Value.kReverse));
        new JoystickButton(pilot, 6).whenActive(new DeploySecondIntake(Value.kForward));

        new JoystickButton(copilot, 10).whenActive(new HatchAndElevateMacro());
    }
}
