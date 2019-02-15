package frc;

import java.net.InetSocketAddress;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Robot;

public class MatchTimeServer extends WebSocketServer implements AutoCloseable {

    private static Notifier starter = new Notifier(() -> {
        new MatchTimeServer().start();
    });
    private Notifier pinger;

    private MatchTimeServer() {
        super(new InetSocketAddress(5800)); // bluh bluh. wish it could be 5892.
        var gson = new Gson();
        pinger = new Notifier(() -> {
            var message = new Message();
            message.matchTime = (int) DriverStation.getInstance().getMatchTime();
            message.batteryVoltage = RobotController.getBatteryVoltage();

            message.infos.slowDrive = Robot.drive.manualSlow;

            message.warnings.brownedOut = RobotController.isBrownedOut();
            
            var msg = gson.toJson(message);
            for (var conn : getConnections()) {
                conn.send(msg);
            }
        });
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onError(WebSocket conn, Exception ex) {
        DriverStation.reportWarning("Error with MatchTimeServer: " + ex.toString(), ex.getStackTrace());
    }

    @Override
    public void onStart() {
        starter.close();
        pinger.startPeriodic(0.125);
    }

    @SuppressWarnings("unused")
    private class Message {
        int matchTime;
        double batteryVoltage;
        Infos infos = new Infos();
        Warnings warnings = new Warnings();

        private class Infos {
            boolean slowDrive;
        }

        private class Warnings {
            boolean brownedOut;
        }
    }

    // this sometimes lets it free up the port when we redeploy the code.
    // other times it just. doesn't.
    // and it's so stupid.
    @Override
    public void close() {
        try {
            stop();
        } catch (Exception e) {}
    }

    public static void startStarting() {
        starter.startPeriodic(2);
    }
    
}