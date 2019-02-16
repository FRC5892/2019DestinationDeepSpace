/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.google.gson.Gson;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.MatchTimeServer;
import frc.robot.subsystems.climb.ClimbSubsystem;
import frc.robot.subsystems.drive.DriveSubsystem;
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

  // when testing on Arduino, 0psi -> 100 and 120psi -> 800
  public static AnalogInput pressureSensor;

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

    /* Miscellaneous Sensors */
    pressureSensor = new AnalogInput(map.pressureSensor);
	
  	/* MatchTimeServer */
  	MatchTimeServer.startStarting();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
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
