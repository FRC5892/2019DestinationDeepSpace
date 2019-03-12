package frc;

import com.google.gson.Gson;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Robot;
import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MatchTimeServer extends WebSocketServer implements AutoCloseable {

    public static final Message.Settings receivableSettings = new Message().new Settings();

    private static Notifier starter = new Notifier(() -> {
        new MatchTimeServer().start();
    });
    private Notifier pinger;

    private static final Gson gson = new Gson();

    private MatchTimeServer() {
        super(new InetSocketAddress(5800)); // bluh bluh. wish it could be 5892.
        pinger = new Notifier(() -> {
            var message = new Message();
            message.matchTime = (int) DriverStation.getInstance().getMatchTime();
            message.batteryVoltage = RobotController.getBatteryVoltage();
            message.pressureReading = Robot.pressureSensor.getValue();

            message.infos.slowDrive = Robot.drive.manualSlow;
            message.infos.hasHatch = Robot.intake.hasHatch();
            message.infos.hasCargo = Robot.intake.hasCargo();

            message.warnings.brownedOut = RobotController.isBrownedOut();

            message.settings = receivableSettings;

            var msg = gson.toJson(message);
            for (var conn : getConnections()) {
                try {
                    conn.send(msg);
                } catch (WebsocketNotConnectedException ignore) {
                }
            }
        });
    }

    public static class Message {
        public int matchTime;
        public double batteryVoltage;
        public int pressureReading;

        public Infos infos = new Infos();
        public Warnings warnings = new Warnings();

        public Settings settings = new Settings();

        public class Infos {
            public boolean slowDrive;
            public boolean hasHatch;
            public boolean hasCargo;
        }

        public class Warnings {
            public boolean brownedOut;
        }

        public class Settings {
            public String autonSide;
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            var incoming = gson.fromJson(message, IncomingMessage.class);
            if (incoming.data != null) {
                switch (incoming.name) {
                    case "auton-side":
                      receivableSettings.autonSide = incoming.data;
                      break;
                }
            }
        } catch (Exception ignore) {
        }
    }

    private class IncomingMessage {
        String name;
        String data;
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        DriverStation.reportWarning("Error with MatchTimeServer: " + ex.toString(), ex.getStackTrace());
    }

    @Override
    public void onStart() {
        starter.close();
        pinger.startPeriodic(0.125);

        receivableSettings.autonSide = "none";
    }

    // this sometimes lets it free up the port when we redeploy the code.
    // other times it just. doesn't.
    // and it's so stupid.
    @Override
    public void close() {
        try {
            stop();
        } catch (Exception ignore) {
        }
    }

    public static void startStarting() {
        starter.startPeriodic(2);
    }

}