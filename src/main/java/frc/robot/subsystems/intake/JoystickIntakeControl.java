package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickIntakeControl extends Command {

    JoystickIntakeControl() {
        requires(Robot.intake);
    }

    private static final double MANUAL_WRIST_SPEED = 0.7;

    @Override
    protected void initialize() {
        if (Robot.intake.cargoMode) {
            initializeCargoMode();
        } else {
            initializeHatchMode();
        }
    }

    @Override
    protected void execute() {
        if (Robot.oi.pilot.getButtonCount() == 0) {
            Robot.intake.setHatchGrabbers(0);
            Robot.intake.setCargoGrabbers(0);
            Robot.intake.setWristSpeed(0);
            return;
        }

        if (Robot.oi.copilot.getRawButtonPressed(5) && Robot.intake.cargoMode) {
            initializeHatchMode();
            Robot.intake.cargoMode = false;
        } else if (Robot.oi.copilot.getRawButtonPressed(6) && !Robot.intake.cargoMode) {
            initializeCargoMode();
            Robot.intake.cargoMode = true;
        }

        if (Robot.intake.cargoMode) {
            executeCargoMode();
        } else {
            executeHatchMode();
        }
    }

    protected void initializeHatchMode() {}

    protected void executeHatchMode() {
        Robot.intake.setPistons(Value.kReverse);

        Robot.intake.setHatchGrabbers(Robot.oi.pilot.getRawAxis(3) - Robot.oi.pilot.getRawAxis(2));
        Robot.intake.setCargoGrabbers(0);

        executeWrist();
    }

    protected void initializeCargoMode() {}

    protected void executeCargoMode() {
        Robot.intake.setPistons(Value.kForward);

        Robot.intake.setHatchGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));
        Robot.intake.setCargoGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));

        executeWrist();
    }

    private static enum WristTarget {
        none,
        top,
        bottom
    }

    private WristTarget wristTarget = WristTarget.none;

    protected void executeWrist() {
        if (Robot.oi.pilot.getRawButton(1)) {
            Robot.intake.setWristSpeed(-MANUAL_WRIST_SPEED);
            wristTarget = WristTarget.none;
        } else if (Robot.oi.pilot.getRawButton(4)) {
            Robot.intake.setWristSpeed(MANUAL_WRIST_SPEED);
            wristTarget = WristTarget.none;
        /*} else if (Robot.oi.pilot.getPOV() == 0) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.UP_SETPOINT); // starting position
        } else if (Robot.oi.pilot.getPOV() == 180) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.DOWN_SETPOINT); // "acquisition position"
        } else if (Robot.oi.pilot.getPOV() == 270) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.MID_SETPOINT); // loading station position
        } else if (!Robot.intake.wristIsOnSetpoint()) {
            Robot.intake.setWristSpeed(0);*/
        } else if (Robot.oi.pilot.getPOV() == 0) {
            wristTarget = WristTarget.top;
        } else if (Robot.oi.pilot.getPOV() == 180) {
            wristTarget = WristTarget.bottom;
        } else {
            if (wristTarget == WristTarget.none) {
                Robot.intake.setWristSpeed(0);
            }
        }

        switch (wristTarget) {
            case none:
              break;
            case top:
              if (Robot.intake.wristIsAtTop()) {
                  Robot.intake.setWristSpeed(0);
              } else {
                  Robot.intake.setWristSpeed(MANUAL_WRIST_SPEED);
              }
              break;
            case bottom:
              if (Robot.intake.wristIsAtBottom()) {
                  Robot.intake.setWristSpeed(0);
              } else {
                  Robot.intake.setWristSpeed(-MANUAL_WRIST_SPEED);
              }
              break;
        }

        if (Robot.oi.pilot.getRawButton(3)) {
            Robot.intake.resetWristSensor();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}