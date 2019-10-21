package frc.robot.subsystems.drive;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;

public class DriveSubsystem extends Subsystem {

    private final DifferentialDrive drive;
    private final CANEncoder leftEncoder, rightEncoder;

    public SpeedMode speedMode = SpeedMode.NORMAL;

    public DriveSubsystem() {
        var left = config(new CANSparkMax(Robot.map.driveLeft[0], MotorType.kBrushless));
        var right = config(new CANSparkMax(Robot.map.driveRight[0], MotorType.kBrushless));
        drive = new DifferentialDrive(new SpeedControllerGroup(
            left,
            config(new CANSparkMax(Robot.map.driveLeft[1], MotorType.kBrushless)),
            config(new CANSparkMax(Robot.map.driveLeft[2], MotorType.kBrushless))
        ), new SpeedControllerGroup(
            right,
            config(new CANSparkMax(Robot.map.driveRight[1], MotorType.kBrushless)),
            config(new CANSparkMax(Robot.map.driveRight[2], MotorType.kBrushless))
        ));
        leftEncoder = left.getEncoder();
        rightEncoder = right.getEncoder();

        /*for (var i = 1; i < Robot.map.driveLeft.length; i++) {
            config(new CANSparkMax(Robot.map.driveLeft[i], MotorType.kBrushless)).follow(left);
        }

        for (var i = 1; i < Robot.map.driveRight.length; i++) {
            config(new CANSparkMax(Robot.map.driveRight[i], MotorType.kBrushless)).follow(right);
        }*/
    }

    DriveSubsystem(DifferentialDrive drive, CANEncoder leftEncoder, CANEncoder rightEncoder) {
        this.drive = drive;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
    }

    private static CANSparkMax config(CANSparkMax spark) {
        spark.setIdleMode(IdleMode.kBrake);
        spark.setOpenLoopRampRate(0.25);
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

    public static enum SpeedMode {
        NORMAL(0.8, 1),
        SLOW(0.5, 0.79),
        KILL(1, 1);

        public final double speedFactor;
        public final double cameraPosition;
        SpeedMode(double factor, double cameraPosition) {
            this.speedFactor = factor;
            this.cameraPosition = cameraPosition;
        }
    }
}