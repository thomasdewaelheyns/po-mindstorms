package penoplatinum.sensor;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import penoplatinum.Utils;
import penoplatinum.barcode.ILightSensor;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.ui.UIView;

public class WrappedLightSensor implements ILightSensor {

    final LightSensor light;
    private int WHITEVAL = 100;
    private int BROWNVAL = 58;
    private int BLACKVAL = 1;
    private int blackBorder;
    private int whiteBorder;
    PrintStream printStream;
    PacketTransporter lightTransporter;
    private final PacketTransporter commandTransporter;

    /**
     * TODO: remove this misery
     * @param conn
     * @param commandTransporter 
     */
    public WrappedLightSensor(IConnection conn, PacketTransporter commandTransporter) {
        //TODO: move out the sensorport
        light = new LightSensor(SensorPort.S4, true);
        
        if (conn != null) {
            lightTransporter = new PacketTransporter(conn);
            conn.RegisterTransporter(lightTransporter, UIView.LIGHT);
            printStream = new PrintStream(lightTransporter.getSendStream());

        }

        updateBorders();
        this.commandTransporter = commandTransporter;
    }

    public void calibrate() {
        // calibreer de lage waarde.
        Utils.Log("Zet de sensor op zwart en druk enter.");
//        commandTransporter.ReceivePacket();
        Button.waitForPress();
        LCD.drawInt(light.readValue(), 1, 0);
        light.calibrateLow();
        Sound.beep();
        Utils.Sleep(250);

        // calibreer de hoge waarde
        Utils.Log("Zet de sensor op wit en druk enter.");
//        commandTransporter.ReceivePacket();
        Button.waitForPress();
        LCD.drawInt(light.readValue(), 3, 0);
        light.calibrateHigh();
        WHITEVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(250);


        Utils.Log("Zet de sensor op zwart en druk enter.");
//        commandTransporter.ReceivePacket();
        Button.waitForPress();
        BLACKVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(250);

        Utils.Log("Zet de sensor op bruin en druk enter.");
//        commandTransporter.ReceivePacket();
        Button.waitForPress();
        LCD.drawInt(light.readValue(), 3, 0);
        BROWNVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(250);

        LCD.clear();

        updateBorders();

        Utils.Log(BLACKVAL + "," + BROWNVAL + "," + WHITEVAL);


    }

    private void updateBorders() {
        blackBorder = (BLACKVAL + BROWNVAL) / 2;
        whiteBorder = (WHITEVAL + BROWNVAL) / 2;
    }

    public int getLightValue() {

        int ret = light.readValue();
        float rawValue = light.getNormalizedLightValue(); // I know, this is confusing
        //Utils.Log(ret + "");
        if (printStream != null) {
            printStream.print((int) rawValue);
            printStream.print(",");

            printStream.print(getCurrentColor(ret).toUIViewColor());
            printStream.println();

            lightTransporter.SendPacket(UIView.LIGHT);
        }


        return ret;
    }

    public byte readValue() {
        return (byte) getLightValue();
    }

    public enum Color {

        Black, White, Brown;

        public int toUIViewColor() {
            switch (this) {
                case Black:
                    return UIView.BLACK;
                case Brown:
                    return UIView.BROWN;
                case White:
                    return UIView.WHITE;
                default:
                    throw new RuntimeException("THE IMPOSSIBLE IS POSSIBLE!");
            }
        }
    }

    public boolean isColor(Color col, double val) {

        switch (col) {
            case Brown:
                return val >= blackBorder && val <= whiteBorder;
            case Black:
                return val < blackBorder;
            case White:
                return val > whiteBorder;


        }
        throw new AssertionError("Unknown op: " + this);
    }

    public boolean isColor(Color col) {
        return isColor(col, getLightValue());
    }

    public Color getCurrentColor() {
        return getCurrentColor(getLightValue());
    }

    public Color getCurrentColor(int val) {
        if (isColor(Color.Brown, val)) {
            return Color.Brown;
        }
        if (isColor(Color.Black, val)) {
            return Color.Black;
        }
        return Color.White;


    }

    public int isBlackOrWhite(int value) {
        if (value < ((blackBorder + whiteBorder) / 2)) {
            return 0;
        } else {
            return 1;
        }
    }
}
