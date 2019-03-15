package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import frc.DoubleSolenoidGroup;

public class RobotMap {
    public static final String competition = "/home/lvuser/deploy/RobotMap/competition.json";
    public static final String practice = "/home/lvuser/deploy/RobotMap/practice.json";


    /* CAN */
    public int[] driveLeft;   // SPARK MAX
    public int[] driveRight;  // SPARK MAX // these are victors on the practice bot shhh
    public int intakeWrist;   // Talon SRX
    public int elevatorWinch; // Talon SRX

    /* PWM */
    public int[] intakeHatchGrabbers;
    public int[] intakeCargoGrabbers;
    public int[] elevatorWinchSupport;
    public int[] climbArms;

    /* DIO */
    public int intakeHatchLimitSwitch; // maybe
    public int intakeCargoLimitSwitch;
    public int intakeWristTopLimitSwitch;
    public int intakeWristBottomLimitSwitch;
    public int elevatorTopLimitSwitch;
    public int elevatorBottomLimitSwitch;

    /* Analog */
    public int pressureSensor;

    /* Solenoids */
    public int[][] intakePistons;
    public int[][] climbPistons;

    public static SpeedController makeVictorGroup(int[] ports) {
        if (ports.length == 0) return null;
        WPI_VictorSPX firstSpx = null;
        ArrayList<SpeedController> victorSPs = new ArrayList<>();

        for (var port : ports) {
            var vic = makeSingleVictor(port);
            if (vic instanceof WPI_VictorSPX) {
                if (firstSpx == null) {
                    firstSpx = (WPI_VictorSPX) vic;
                } else {
                    ((WPI_VictorSPX) vic).follow(firstSpx);
                }
            } else {
                victorSPs.add(vic);
            }
        }

        if (firstSpx == null) {
            if (victorSPs.size() == 1) {
                return victorSPs.get(0);
            } else {
                var rest = new SpeedController[victorSPs.size() - 1];
                System.arraycopy(victorSPs.toArray(new SpeedController[0]), 1, rest, 0, rest.length);
                return new SpeedControllerGroup(victorSPs.get(0), rest);
            }
        } else if (victorSPs.isEmpty()) {
            return firstSpx;
        } else {
            return new SpeedControllerGroup(firstSpx, victorSPs.toArray(new SpeedController[0]));
        }
    }

    public static SpeedController makeSingleVictor(int port) {
        var inverted = false;
        if (port < 0) {
            inverted = true;
            port *= -1;
        }

        SpeedController ret;
        if (port >= 10) {
            var _ret = new WPI_VictorSPX(port - 10);
            _ret.setNeutralMode(NeutralMode.Brake);
            ret = _ret;
        } else {
            ret = new VictorSP(port);
        }

        ret.setInverted(inverted);
        return ret;
    }

    public static DoubleSolenoidGroup makeDoubleSolenoidGroup(int[][] ports) {
        var arr = new DoubleSolenoid[ports.length];
        for (var i = 0; i < ports.length; i++) {
            arr[i] = new DoubleSolenoid(ports[i][0], ports[i][1]);
        }
        return new DoubleSolenoidGroup(arr);
    }
}