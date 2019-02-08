package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ClimbSubsystem extends Subsystem {

    private final SpeedController arms;

    public ClimbSubsystem() {
        arms = RobotMap.makeVictorGroup(Robot.map.climbArms);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ClimbControlCommand());
    }

    public void setArms(double speed) {
        arms.set(speed);
    }
}
