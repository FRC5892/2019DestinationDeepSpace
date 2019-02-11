package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;

public class RobotMap {
    public static final String competition = "/home/lvuser/deploy/RobotMap/competition.json";

    /* CAN */
    public int[] driveLeft;
    public int[] driveRight;
    public int intakeWrist;
    public int elevatorWinch;

    /* PWM */
    public int[] intakeHatchGrabbers;
    public int[] intakeCargoGrabbers;
    public int[] climbArms;

    /* DIO */

    public static SpeedController makeVictorGroup(int[] ports) {
        var first = new Victor(Math.abs(ports[0]));
        first.setInverted(ports[0] < 0); // let's hope we don't have to invert 0... meh, just reverse *everything* then.
        if (ports.length == 1) return first;
        var rest = new Victor[ports.length - 1];
        for (var i=1; i<ports.length; i++) {
            @SuppressWarnings("resource")
            var vic = new Victor(Math.abs(ports[i]));
            vic.setInverted(ports[i] < 0);
            rest[i-1] = vic;
        }
        return new SpeedControllerGroup(first, rest);
    }
}