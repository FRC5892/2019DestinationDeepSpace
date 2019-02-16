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
    private double leftEncoderOffset = 0, rightEncoderOffset = 0;

    public boolean manualSlow = false;

    public DriveSubsystem() {
        var left = config(new CANSparkMax(Robot.map.driveLeft[0], MotorType.kBrushless));
        var right = config(new CANSparkMax(Robot.map.driveRight[0], MotorType.kBrushless));
        drive = new DifferentialDrive(left, right);
        leftEncoder = left.getEncoder();
        rightEncoder = right.getEncoder();

        for (var i=1; i<Robot.map.driveLeft.length; i++) {
            config(new CANSparkMax(Robot.map.driveLeft[i], MotorType.kBrushless)).follow(left);
        }

        for (var i=1; i<Robot.map.driveRight.length; i++) {
            config(new CANSparkMax(Robot.map.driveRight[i], MotorType.kBrushless)).follow(right);
        }
    }

    private static CANSparkMax config(CANSparkMax spark) {
        spark.setIdleMode(IdleMode.kBrake);
        spark.setRampRate(0.5); // TODO change to setOpenLoopRampRate after lib update
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
        return leftEncoder.getPosition() - leftEncoderOffset;
    }

    public double getRightEncoder() {
        return rightEncoder.getPosition() - rightEncoderOffset;
    }

    public void resetEncoders() {
        leftEncoderOffset = getLeftEncoder();
        rightEncoderOffset = getRightEncoder();
    }

    public void stop() {
        drive.stopMotor();
    }

}