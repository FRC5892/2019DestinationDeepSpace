package frc.robot.subsystems.elevator;

import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.TalonUtils;
import frc.robot.Robot;

public class ElevatorSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = false;

    private final TalonSRX winch;

    public ElevatorSubsystem() {
        winch = new TalonSRX(Robot.map.elevatorWinch);
        try {
            TalonUtils.readPID(winch, "ElevatorWinch", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for elevator winch.", e.getStackTrace());
        }
    }

    @Override
    protected void initDefaultCommand() {
    }

    public void setWinchSetpoint(double target) {
        winch.set(ControlMode.Position, target);
    }
}
