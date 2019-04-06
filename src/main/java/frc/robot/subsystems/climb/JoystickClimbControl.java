package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

class JoystickClimbControl extends Command {

    JoystickClimbControl() {
        requires(Robot.climb);
    }

    @Override
    protected void initialize() {
        Robot.climb.setPistons(Value.kReverse);
    }

    @Override
    protected void execute() {
        Robot.climb.setArms(Robot.oi.copilot.getRawAxis(3) - Robot.oi.copilot.getRawAxis(2));
        switch (Robot.oi.copilot.getPOV()) {
            case 0:
                Robot.climb.setPistons(Value.kReverse);
                break;
            case 180:
                Robot.climb.setPistons(Value.kForward);
                break;
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
