/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.google.gson.Gson;
import edu.wpi.first.hal.util.UncleanStatusException;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.MatchTimeServer;
import frc.robot.subsystems.climb.ClimbSubsystem;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.drive.PracticeDriveSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;

import java.io.FileReader;
import java.io.IOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */

    public static RobotMap map;
    public static OI oi;

    public static DriveSubsystem drive;
    public static IntakeSubsystem intake;
    public static ElevatorSubsystem elevator;
    public static ClimbSubsystem climb;

    public static AnalogInput pressureSensor;
    public static SerialPort serial;

    @Override
    @SuppressWarnings("resource")
    public void robotInit() {
        /* RobotMap */
        try {
            map = new Gson().fromJson(new FileReader(RobotMap.competition), RobotMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* Subsystems */
        drive = new DriveSubsystem();
        intake = new IntakeSubsystem();
        elevator = new ElevatorSubsystem();
        climb = new ClimbSubsystem();

        /* OI */
        oi = new OI();

        /* Miscellaneous I/O */
        pressureSensor = new AnalogInput(map.pressureSensor);
        new Notifier(Robot::arduinoCommLoop).startPeriodic(1.0 / 20);

        /* MatchTimeServer */
        MatchTimeServer.startStarting();
    }

    private static final byte[] GREEN = {0};
    private static final byte[] RED = {1};
    private static final byte[] BLUE = {2};
    private static final byte[] ORANGE = {3};

    public static void arduinoCommLoop() {
        if (serial == null) {
            try {
                serial = new SerialPort(9600, Port.kUSB1);
            } catch (UncleanStatusException ignore) {
            }
        } else {
            try {
                if (DriverStation.getInstance().isEnabled()) {
                    serial.write(GREEN, 1);
                } else {
                    if (DriverStation.getInstance().isFMSAttached()) {
                        switch (DriverStation.getInstance().getAlliance()) {
                            case Red:
                                serial.write(RED, 1);
                                break;
                            case Blue:
                                serial.write(BLUE, 1);
                                break;
                            case Invalid:
                                serial.write(ORANGE, 1);
                                break;
                        }
                    } else {
                        serial.write(ORANGE, 1);
                    }
                }
            } catch (UncleanStatusException ignore) {
            }
        }
    }

    @Override
    public void disabledInit() {
        intake.setWristSpeed(0);
        //elevator.setWinchSpeed(0);
    }

    @Override
    public void autonomousInit() {
        System.out.println(MatchTimeServer.receivableSettings.autonSide);
    }

    @Override
    public void autonomousPeriodic() {
        teleopPeriodic();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

}
