package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Victor;
import frc.DoubleSolenoidGroup;

public class RobotMap {
    public static final String competition = "/home/lvuser/deploy/RobotMap/competition.json";

    /* CAN */
    public int[] driveLeft;   // SPARK MAX
    public int[] driveRight;  // SPARK MAX
    public int intakeWrist;   // Talon SRX
    public int elevatorWinch; // Talon SRX

    /* PWM */
    public int[] intakeHatchGrabbers;
    public int[] intakeCargoGrabbers;
    public int[] climbArms;

    /* DIO */
    public int intakeHatchLimitSwitch; // maybe

    /* Analog */
    public int pressureSensor;

    /* Solenoids */
    public int[][] climbPistons;
    public int[][] elevatorBrake;

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

    public static DoubleSolenoidGroup makeDoubleSolenoidGroup(int[][] ports) {
        var arr = new DoubleSolenoid[ports.length];
        for (var i=0; i<ports.length; i++) {
            arr[i] = new DoubleSolenoid(ports[i][0], ports[i][1]);
        }
        return new DoubleSolenoidGroup(arr);
    }
}