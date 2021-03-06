/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.HEROicPIDController;
import frc.robot.Robot;

/**
 * An example command.  You can replace me with your own command.
 */
public class AlignWithVisionTargets extends Command {

    private static final boolean DEBUG_MODE = false;

    private static NetworkTableEntry xCenter, size, visible, robotTimestamp, lastFrame; //, xCenterRight, sizeRight, visibleRight;

    private final double turnTolerance, moveTolerance;

    private final VisionTurnController turnController = new VisionTurnController();
    private final VisionMoveController moveController = new VisionMoveController();

    @SuppressWarnings("resource")
    public AlignWithVisionTargets(double turnTolerance, double moveTolerance) {
        requires(Robot.drive);
        this.turnTolerance = turnTolerance;
        this.moveTolerance = moveTolerance;

        if (xCenter == null) {
            var table = NetworkTableInstance.getDefault().getTable("Vision");
            xCenter = table.getEntry("xCenter");
            size = table.getEntry("size");
            visible = table.getEntry("visible");
            robotTimestamp = table.getEntry("robotTimestamp");
            lastFrame = table.getEntry("lastFrame");
            new Notifier(() -> {
                robotTimestamp.setDouble(Timer.getFPGATimestamp());
            }).startPeriodic(1.0 / 30);
        }
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        moveController.enable();
        turnController.enable();
    }

    @Override
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return moveTolerance >= 0 && Math.abs(moveController.getError()) <= moveTolerance &&
                turnTolerance >= 0 && Math.abs(turnController.getError()) <= turnTolerance;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        moveController.disable();
        turnController.disable();
        Robot.drive.stop();
    }

    double move;
    double turn;

    void setMove(double move) {
        this.move = move;
        Robot.drive.arcadeDrive(move, turn);
    }

    void setTurn(double turn) {
        this.turn = turn;
        Robot.drive.arcadeDrive(move, turn);
    }

    private static boolean seesTarget() {
        if (!(lastFrame.getDouble(0) < 0.5 + Timer.getFPGATimestamp())) {
            System.out.println("the pi is ded");
        }
        return visible.getBoolean(false) && lastFrame.getDouble(0) < 0.5 + Timer.getFPGATimestamp();
    }

    private class VisionMoveController extends HEROicPIDController {

        VisionMoveController() {
            super(DEBUG_MODE);
            setSetpoint(34000);
            setOutputLimits(0.75);
        }

        @Override
        public double getPIDInput(ComputationSkipper skipper) {
            if (!seesTarget()) {
                throw skipper;
            } else {
                return size.getDouble(getSetpoint());
            }
        }

        @Override
        public void usePIDOutput(double output) {
            setMove(output);
        }

        @Override
        public void onSkipComputation() {
            setMove(-0.35);
        }

    }

    private class VisionTurnController extends HEROicPIDController {

        VisionTurnController() {
            super(DEBUG_MODE);
            setSetpoint(120);
            setOutputLimits(0.75);
        }

        @Override
        public double getPIDInput(ComputationSkipper skipper) {
            if (!seesTarget() || size.getDouble(0) > 30000) {
                throw skipper;
            } else {
                return xCenter.getDouble(getSetpoint());
            }
        }

        //private static final double ERROR_TURN = 0.5;

        @Override
        public void usePIDOutput(double output) {
            setTurn(-output);
        }

        @Override
        public void onSkipComputation() {
            setTurn(0);
        }
    }
}
