package frc;

public class MathUtils {
    private MathUtils() {};

    public static double deadZone(double input, double minMagnitude) {
        if (Math.abs(input) < minMagnitude) {
            return 0;
        }
        return input;
    }
}