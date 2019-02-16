package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;

public class DriveSubsystem extends Subsystem {

    private final DifferentialDrive drive;

    public boolean manualSlow = false;

    public DriveSubsystem() {
        var left = config(new CANSparkMax(Robot.map.driveLeft[0], MotorType.kBrushless));
        var right = config(new CANSparkMax(Robot.map.driveRight[0], MotorType.kBrushless));
        drive = new DifferentialDrive(left, right);

        for (var i=1; i<Robot.map.driveLeft.length; i++) {
            config(new CANSparkMax(Robot.map.driveLeft[i], MotorType.kBrushless)).follow(left);
        }

        for (var i=1; i<Robot.map.driveRight.length; i++) {
            config(new CANSparkMax(Robot.map.driveRight[i], MotorType.kBrushless)).follow(right);
        }
    }

    private static CANSparkMax config(CANSparkMax spark) {
        spark.setIdleMode(IdleMode.kBrake);
        spark.setRampRate(1); // TODO change to setOpenLoopRampRate after lib update
        //spark.burnFlash();
        return spark;
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