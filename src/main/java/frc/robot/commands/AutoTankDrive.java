package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class AutoTankDrive extends Command {
    private final double leftSpeed, rightSpeed, encoderTarget;

    public AutoTankDrive(double leftSpeed, double rightSpeed, double encoderTarget) {
        requires(Robot.drive);
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
        this.encoderTarget = encoderTarget;
    }

    @Override
    protected void initialize() {
        Robot.drive.resetEncoders();
    }

    @Override
    protected void execute() {
        Robot.drive.tankDrive(leftSpeed, rightSpeed);
    }

    @Override
    protected boolean isFinished() {
        // the OR was for redundancy in previous years
        // but with the brushless motors, we're fried if the encoder fails anyway
        // /shrug
        return timeSinceInitialized() > 0.1 && (Math.abs(Robot.drive.getLeftEncoder()) > encoderTarget ||
               Math.abs(Robot.drive.getRightEncoder()) > encoderTarget);
    }

    @Override
    protected void end() {
        Robot.drive.stop();
    }
}