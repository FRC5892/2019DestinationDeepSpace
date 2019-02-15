package frc.robot.subsystems.climb;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ClimbSubsystem extends Subsystem {

    private final SpeedController arms;
    private final DoubleSolenoidGroup pistons;

    public ClimbSubsystem() {
        arms = RobotMap.makeVictorGroup(Robot.map.climbArms);
        pistons = RobotMap.makeDoubleSolenoidGroup(Robot.map.climbPistons);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickClimbControl());
    }

    public void setArms(double speed) {
        arms.set(speed);
    }

    public void setPistons(Value value) {
        pistons.set(value);
    }
}
