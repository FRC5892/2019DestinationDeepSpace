package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickClimbControl extends Command {

    JoystickClimbControl() {
        requires(Robot.climb);
    }

    private static final double ARMS_SPEED = 1;

    @Override
    protected void execute() {
        if (Robot.oi.copilot.getPOV() == 0) {
            Robot.climb.setArms(-ARMS_SPEED);
        } else if (Robot.oi.copilot.getPOV() == 180) {
            Robot.climb.setArms(ARMS_SPEED);
        }

        if (Robot.oi.copilot.getRawAxis(2) < 0.5) {
            Robot.climb.setPistons(Value.kReverse);
        } else if (Robot.oi.copilot.getRawAxis(2) > 0.5) {
            Robot.climb.setPistons(Value.kForward);
        } else {
            Robot.climb.setPistons(Value.kOff);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.climb.setArms(0);
        Robot.climb.setPistons(Value.kOff);
    }
}
