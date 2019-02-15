package frc.robot.subsystems.elevator;

import java.io.IOException;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ElevatorSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = false;

    private static final double BRAKE_THRESHOLD = 0; // TODO set up

    private final TalonSRX winch;
    private final SpeedController winchSupport;
    private final DoubleSolenoidGroup brake;

    private double winchSetpoint;

    public ElevatorSubsystem() {
        winch = new TalonSRX(Robot.map.elevatorWinch);
        try {
            TalonUtils.readPID(winch, "ElevatorWinch", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for elevator winch.", e.getStackTrace());
        }
        winchSupport = RobotMap.makeVictorGroup(Robot.map.elevatorWinchSupport);
        brake = RobotMap.makeDoubleSolenoidGroup(Robot.map.elevatorBrake);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DirectElevatorControl());
    }

    public void setWinchSpeed(double speed) {
        winch.set(ControlMode.PercentOutput, speed);
        brake.set((speed == 0) ? Value.kForward : Value.kReverse);
    }

    public void setWinchSetpoint(double target) {
        winchSetpoint = target;
        winch.set(ControlMode.Position, target);
    }

    public boolean winchIsOnSetpoint() {
        return winch.getControlMode() != ControlMode.PercentOutput;
    }

    @Override
    public void periodic() {
        // can't use getClosedLoopError() because it changes when we neutral the motor
        if (winchIsOnSetpoint()) {
            if (Math.abs(winch.getSelectedSensorPosition() - winchSetpoint) < BRAKE_THRESHOLD) {
                winch.neutralOutput();
            } else {
                winch.set(ControlMode.Position, winchSetpoint);
            }
        }
        // lol discount following
        winchSupport.set(winch.getMotorOutputPercent());
    }
}