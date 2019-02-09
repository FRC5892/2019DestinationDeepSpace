package frc;

import java.net.InetSocketAddress;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;

public class MatchTimeServer extends WebSocketServer implements AutoCloseable {

    public MatchTimeServer() {
        super(new InetSocketAddress(5800)); // bluh bluh. wish it could be 5892.
        var gson = new Gson();
        @SuppressWarnings("resource")
        var notifier = new Notifier(() -> {
            var message = new Message();
            message.matchTime = (int) DriverStation.getInstance().getMatchTime();
            message.batteryVoltage = RobotController.getBatteryVoltage();
            var msg = gson.toJson(message);
            for (var conn : getConnections()) {
                conn.send(msg);
            }
        });
        notifier.startPeriodic(0.125);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

    @Override
    public void onMessage(WebSocket conn, String message) {}

    @Override
    public void onError(WebSocket conn, Exception ex) {
        DriverStation.reportWarning("Error with MatchTimeServer: " + ex.getMessage(), ex.getStackTrace());
    }

    @Override
    public void onStart() {}

    @SuppressWarnings("unused")
    private class Message {
        int matchTime;
        double batteryVoltage;
    }

    @Override
    public void close() {
        try {
            stop();
        } catch (Exception e) {}
    }
    
}