package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class PracticeDriveSubsystem extends DriveSubsystem {

    public PracticeDriveSubsystem() {
        super(makeDriveTrain(), null, null);
    }

    private static DifferentialDrive makeDriveTrain() {
        return new DifferentialDrive(
                RobotMap.makeVictorGroup(Robot.map.driveLeft),
                RobotMap.makeVictorGroup(Robot.map.driveRight)
        );
    }

    @Override
    public double getLeftEncoder() {
        return 0;
    }

    @Override
    public double getRightEncoder() {
        return 0;
    }

    @Override
    public void resetEncoders() {
    }
}
