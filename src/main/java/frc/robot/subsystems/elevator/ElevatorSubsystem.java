package frc.robot.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;

public class ElevatorSubsystem extends Subsystem {

    private final TalonSRX winch;

    public ElevatorSubsystem() {
        winch = new TalonSRX(Robot.map.elevatorWinch);
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void setWinchSetpoint(double target) {
        winch.set(ControlMode.Position, target);
    }
}
