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

    final LightSensor light = new LightSensor(SensorPort.S4, true);
    private int WHITEVAL;
    private int BROWNVAL;
    private int BLACKVAL;
    private int blackBorder;
    private int whiteBorder;
    PrintStream printStream;
    PacketTransporter lightTransporter;

    public WrappedLightSensor(IConnection conn) {
        if (conn == null) {
            return;
        }
        lightTransporter = new PacketTransporter(conn);
        conn.RegisterTransporter(lightTransporter, UIView.LIGHT);
        printStream = new PrintStream(lightTransporter.getSendStream());

    }

    public void calibrate() {
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 1, 0);
        light.calibrateLow();
        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 3, 0);
        light.calibrateHigh();
        WHITEVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);


        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        BLACKVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        System.out.println("Zet de sensor op bruin en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 3, 0);
        BROWNVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        LCD.clear();

        updateBorders();

    }

    private void updateBorders() {
        blackBorder = (BLACKVAL + BROWNVAL) / 2;
        whiteBorder = (WHITEVAL + BROWNVAL) / 2;
    }

    public int getLightValue() {


        int ret = light.readValue();
        float rawValue = light.getNormalizedLightValue(); // I know, this is confusing


        if (printStream != null) {
            printStream.print(rawValue);
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
                return val > blackBorder && val < whiteBorder;
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
