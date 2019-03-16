package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.AcquireHatchMacro;
import frc.robot.commands.AlignWithVisionTargets;
import frc.robot.commands.ScoreHatchMacro;

public class OI {
    public Joystick pilot = new Joystick(0);
    public Joystick copilot = new Joystick(1);

    @SuppressWarnings("resource")
    public OI() {
        new JoystickButton(pilot, 9).whenActive(new AcquireHatchMacro());
        new JoystickButton(pilot, 10).whenActive(new ScoreHatchMacro());
        //new JoystickButton(pilot, 5).whileActive(new AlignWithVisionTargets(-1, -1));
    }
}
