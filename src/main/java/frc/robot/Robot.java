/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.google.gson.Gson;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
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

  // 0psi -> 400 and 120psi -> 3200
  public static AnalogInput pressureSensor;
  public static SerialPort serial;

  @Override
  @SuppressWarnings("resource")
  public void robotInit() {
    /* RobotMap */
    try {
      map = new Gson().fromJson(new FileReader(RobotMap.practice), RobotMap.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    /* Subsystems */
    drive = new PracticeDriveSubsystem();
    intake = new IntakeSubsystem();
    elevator = new ElevatorSubsystem();
    climb = new ClimbSubsystem();

    /* OI */
    oi = new OI();

    /* Miscellaneous I/O */
    pressureSensor = new AnalogInput(map.pressureSensor);
    serial = new SerialPort(9600, Port.kUSB1);
	
  	/* MatchTimeServer */
    MatchTimeServer.startStarting();
  }

  @Override
  public void disabledInit() {
    intake.setWristSpeed(0);
    elevator.setWinchSpeed(0);
  }

  private static final byte[] GREEN = {0};
  private static final byte[] RED = {1};
  private static final byte[] BLUE = {2};
  private static final byte[] ORANGE = {3};
  @Override
  public void disabledPeriodic() {
    if (m_ds.isFMSAttached()) {
      switch (m_ds.getAlliance()) {
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

  @Override
  public void autonomousInit() {
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
    serial.write(GREEN, 1);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
