package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;

public class DriveSubsystem extends Subsystem {

    private DifferentialDrive drive;

    public DriveSubsystem() {
        var left = new CANSparkMax(Robot.map.driveLeft[0], MotorType.kBrushless);
        var right = new CANSparkMax(Robot.map.driveRight[0], MotorType.kBrushless);
        drive = new DifferentialDrive(left, right);

        for (var i=1; i<Robot.map.driveLeft.length; i++) {
            new CANSparkMax(Robot.map.driveLeft[i], MotorType.kBrushless).follow(left);
        }

        for (var i=1; i<Robot.map.driveRight.length; i++) {
            new CANSparkMax(Robot.map.driveRight[i], MotorType.kBrushless).follow(right);
        }
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickDriveCommand());
    }

    public void arcadeDrive(double xSpeed, double zRotation) {
        drive.arcadeDrive(xSpeed, zRotation);
    }

    public void stop() {
        drive.stopMotor();
    }

}