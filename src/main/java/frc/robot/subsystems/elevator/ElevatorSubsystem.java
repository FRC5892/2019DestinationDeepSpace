package frc.robot.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.DoubleSolenoidGroup;
import frc.TalonUtils;
import frc.robot.Robot;
import frc.robot.RobotMap;

import java.io.IOException;

public class ElevatorSubsystem extends Subsystem {

    private static final boolean TUNING_MODE = true;

    private static final double BRAKE_THRESHOLD = 110; // TODO set up

    public static final double BOTTOM = 0;
    public static final double HATCH_2 = 20000;
    public static final double HATCH_3 = 38000;

    private final TalonSRX winch;
    private final SpeedController winchSupport;
    private final DoubleSolenoidGroup brake;
    private final DigitalInput topLimitSwitch, bottomLimitSwitch;

    private double winchSetpoint;

    public ElevatorSubsystem() {
        winch = new TalonSRX(Robot.map.elevatorWinch);
        winch.setSensorPhase(true);
        try {
            TalonUtils.readPID(winch, "ElevatorWinch", TUNING_MODE);
        } catch (IOException e) {
            DriverStation.reportWarning("Couldn't read PID gains for elevator winch.", e.getStackTrace());
        }
        winchSupport = RobotMap.makeVictorGroup(Robot.map.elevatorWinchSupport);
        brake = RobotMap.makeDoubleSolenoidGroup(Robot.map.elevatorBrake);
        topLimitSwitch = new DigitalInput(Robot.map.elevatorTopLimitSwitch);
        bottomLimitSwitch = new DigitalInput(Robot.map.elevatorBottomLimitSwitch);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DirectElevatorControl());
    }

    public void setWinchSpeed(double speed) {
        if (speed > 0 && topLimitSwitchTriggered()) {
            winch.set(ControlMode.PercentOutput, 0);
            brake.set(Value.kReverse);
            return;
        }
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

    public void resetEncoder() {
        winch.setSelectedSensorPosition(0);
    }

    public boolean topLimitSwitchTriggered() {
        return !topLimitSwitch.get();
    }

    public boolean bottomLimitSwitchTriggered() {
        return bottomLimitSwitch.get();
    }

    @Override
    public void periodic() {
        // can't use getClosedLoopError() because it changes when we neutral the motor
        if (winchIsOnSetpoint()) {
            if (Math.abs(winch.getSelectedSensorPosition() - winchSetpoint) < BRAKE_THRESHOLD) {
                winch.neutralOutput();
                brake.set(Value.kForward);
            } else {
                winch.set(ControlMode.Position, winchSetpoint);
                brake.set(Value.kReverse);
            }
        }
        // lol discount following
        winchSupport.set(winch.getMotorOutputPercent());
    }
}