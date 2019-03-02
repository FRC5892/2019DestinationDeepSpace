package frc;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TalonUtils {
    private TalonUtils() {
    }

    public static void readPID(TalonSRX talon, String name, boolean tuningMode) throws IOException {
        double kP, kI, kD;
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/lvuser/deploy/PID/" + name + ".txt"))) {
            kP = Double.parseDouble(reader.readLine());
            kI = Double.parseDouble(reader.readLine());
            kD = Double.parseDouble(reader.readLine());
        }
        talon.config_kP(0, kP);
        talon.config_kI(0, kI);
        talon.config_kD(0, kD);
        if (tuningMode) {
            makeShuffleboardTab(talon, name, kP, kI, kD);
        }
    }

    @SuppressWarnings("resource")
    private static void makeShuffleboardTab(TalonSRX talon, String name, double kP, double kI, double kD) {
        var tab = Shuffleboard.getTab(name);

        var kPEntry = tab.add("kP", kP).withPosition(0, 0).getEntry();
        kPEntry.setDouble(kP);
        kPEntry.addListener((evt) -> {
            talon.config_kP(0, evt.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        var kIEntry = tab.add("kI", kI).withPosition(0, 1).getEntry();
        kIEntry.setDouble(kI);
        kIEntry.addListener((evt) -> {
            talon.config_kI(0, evt.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        var kDEntry = tab.add("kD", kD).withPosition(0, 2).getEntry();
        kDEntry.setDouble(kD);
        kDEntry.addListener((evt) -> {
            talon.config_kD(0, evt.value.getDouble());
        }, EntryListenerFlags.kUpdate);

        var inputEntry = tab.add("Input", 0).withPosition(1, 0).getEntry();
        var errorEntry = tab.add("Error", 0).withPosition(1, 1).getEntry();
        var outputEntry = tab.add("Output", 0).withPosition(1, 2).getEntry();
        new Notifier(() -> {
            var error = talon.getClosedLoopError();
            inputEntry.setDouble(talon.getSelectedSensorPosition());
            errorEntry.setDouble(error);
            outputEntry.setDouble(talon.getMotorOutputPercent());
        }).startPeriodic(1.0 / 20);
    }
}