package frc.robot.subsystems.drive;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;

public class DriveSubsystem extends Subsystem {

    private final DifferentialDrive drive;
    private final CANEncoder leftEncoder, rightEncoder;

    public boolean manualSlow = false;

    public DriveSubsystem() {
        var left = config(new CANSparkMax(Robot.map.driveLeft[0], MotorType.kBrushless));
        var right = config(new CANSparkMax(Robot.map.driveRight[0], MotorType.kBrushless));
        drive = new DifferentialDrive(left, right);
        leftEncoder = left.getEncoder();
        rightEncoder = right.getEncoder();

        for (var i = 1; i < Robot.map.driveLeft.length; i++) {
            config(new CANSparkMax(Robot.map.driveLeft[i], MotorType.kBrushless)).follow(left);
        }

        for (var i = 1; i < Robot.map.driveRight.length; i++) {
            config(new CANSparkMax(Robot.map.driveRight[i], MotorType.kBrushless)).follow(right);
        }
    }

    DriveSubsystem(DifferentialDrive drive, CANEncoder leftEncoder, CANEncoder rightEncoder) {
        this.drive = drive;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
    }

    private static CANSparkMax config(CANSparkMax spark) {
        spark.setIdleMode(IdleMode.kBrake);
        spark.setOpenLoopRampRate(0.75);
        spark.getEncoder().setPositionConversionFactor(1 / 12.45);
        //spark.setSmartCurrentLimit(40);
        //spark.burnFlash();
        return spark;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickDriveCommand());
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        drive.tankDrive(leftSpeed, rightSpeed);
    }

    public void arcadeDrive(double xSpeed, double zRotation) {
        drive.arcadeDrive(xSpeed, zRotation);
    }

    public double getLeftEncoder() {
        return leftEncoder.getPosition();
    }

    public double getRightEncoder() {
        return rightEncoder.getPosition();
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void stop() {
        drive.stopMotor();
    }
}