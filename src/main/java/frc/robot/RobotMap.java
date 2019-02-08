package frc.robot;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class RobotMap {
    /* CAN */
    public int[] driveLeft;
    public int[] driveRight;

    /* PWM */

    /* DIO */

    public RobotMap(boolean isCompetition) {
        try {
            var root = new JsonParser().parse(new FileReader(isCompetition ? 
                "/home/lvuser/deploy/RobotMap/competition.json" :
                "/home/lvuser/deploy/RobotMap/practice.json")).getAsJsonObject();
            
            var can = root.getAsJsonObject("CAN");
            driveLeft = jsonArrayToIntArray(can.getAsJsonArray("driveLeft"));
            driveRight = jsonArrayToIntArray(can.getAsJsonArray("driveRight"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] jsonArrayToIntArray(JsonArray jsonArray) {
        int[] ret = new int[jsonArray.size()];
        for (var i=0; i<ret.length; i++) {
            ret[i] = jsonArray.get(i).getAsInt();
        }
        return ret;
    }
}