package frc;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class DoubleSolenoidGroup {
    private DoubleSolenoid[] solenoids;

    public DoubleSolenoidGroup(DoubleSolenoid... solenoids) {
        this.solenoids = solenoids;
    }

    public void set(Value value) {
        for (var sol : solenoids) {
            sol.set(value);
        }
    }
}