package frc;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A {@link Trigger} that activates based on a {@link Joystick}'s POV hat.
 *
 * @author Kai Page
 */
public class POVTrigger extends Trigger {
    private final Joystick _stick;
    private final int _pov;
    private final int _angle;

    /**
     * @param stick The joystick to read from.
     * @param angle The angle to react to. See {@link Joystick::getPOV} for details.
     */
    public POVTrigger(Joystick stick, int angle) {
        this(stick, 0, angle);
    }

    /**
     * @param stick The joystick to read from.
     * @param pov The POV hat to read from.
     * @param angle The angle to react to.
     */
    public POVTrigger(Joystick stick, int pov, int angle) {
        _stick = stick;
        _pov = pov;
        _angle = angle;
    }

    @Override
    public boolean get() {
        return _stick.getPOV(_pov) == _angle;
    }
}
