package frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * Triggers based on the axis of a {@link Joystick}. Useful for a joystick's left and right triggers.
 *
 * @author Kai Page
 */
public class AnalogAxisTrigger extends Trigger {
    private final Joystick _stick;
    private final int _axis;
    private final double _threshold;

    /**
     * Initializes with a default threshold of 0.7.
     */
    public AnalogAxisTrigger(Joystick stick, int axis) {
        this(stick, axis, 0.7);
    }

    public AnalogAxisTrigger(Joystick stick, int axis, double threshold) {
        _stick = stick;
        _axis = axis;
        _threshold = threshold;
    }

    @Override
    public boolean get() {
        if (_threshold > 0) return _stick.getRawAxis(_axis) > _threshold;
        else if (_threshold < 0) return _stick.getRawAxis(_axis) < _threshold;
        return _stick.getRawAxis(_axis) != 0;
    }
}
