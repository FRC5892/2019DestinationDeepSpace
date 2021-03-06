package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

import static frc.MathUtils.*;

class JoystickIntakeControl extends Command {

    private static final boolean REVERSE_CODRIVER_INTAKE = true;

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

        Robot.intake.setHatchGrabbers(deadZone(Robot.oi.pilot.getRawAxis(3) - Robot.oi.pilot.getRawAxis(2) + Robot.oi.copilot.getRawAxis(5), 0.2));
        Robot.intake.setCargoGrabbers(0);

        executeWrist();
    }

    @SuppressWarnings("unused")
    private boolean intakingCargo = false;

    protected void initializeCargoMode() {
        intakingCargo = false;
    }

    private static void setAllGrabbers(double speed) {
        Robot.intake.setHatchGrabbers(speed);
        Robot.intake.setCargoGrabbers(speed);
    }

    protected void executeCargoMode() {
        Robot.intake.setPistons(Value.kForward);

        if (Robot.oi.copilot.getRawButton(1)) {
            setAllGrabbers(-1);
        } else {
            setAllGrabbers(deadZone(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3) + Robot.oi.copilot.getRawAxis(5) * (REVERSE_CODRIVER_INTAKE ? 1 : -1), 0.2));
        }//*/
        
        /*if (Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3) > 0.3) {
            Robot.intake.setHatchGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));
            Robot.intake.setCargoGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));
            intakingCargo = true;
        } else if (Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3) < -0.3) {
            Robot.intake.setHatchGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));
            Robot.intake.setCargoGrabbers(Robot.oi.pilot.getRawAxis(2) - Robot.oi.pilot.getRawAxis(3));
            intakingCargo = false;
        } else if (intakingCargo) {
            Robot.intake.setHatchGrabbers(Robot.intake.hasCargo() ? 0 : 1);
            Robot.intake.setCargoGrabbers(Robot.intake.hasCargo() ? 0 : 1);
        } else {
            Robot.intake.setHatchGrabbers(0);
            Robot.intake.setCargoGrabbers(0);
        }//*/

        executeWrist();
    }

    protected void executeWrist() {
        if (Robot.oi.pilot.getRawButton(1)) {
            Robot.intake.setWristSpeed(-MANUAL_WRIST_SPEED);
        } else if (Robot.oi.pilot.getRawButton(4)) {
            Robot.intake.setWristSpeed(MANUAL_WRIST_SPEED);
        /*} else if (Robot.oi.pilot.getPOV() == 0) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.UP_SETPOINT); // starting position
        } else if (Robot.oi.pilot.getPOV() == 180) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.DOWN_SETPOINT); // "acquisition position"
        } else if (Robot.oi.pilot.getPOV() == 270) {
            Robot.intake.setWristSetpoint(IntakeSubsystem.MID_SETPOINT); // loading station position
        */} else if (!Robot.intake.wristIsOnSetpoint()) {
            Robot.intake.setWristSpeed(0);
        }

        if (Robot.oi.pilot.getRawButton(7)) {
            Robot.intake.resetWristSensor();
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}