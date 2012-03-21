package penoplatinum.sensor;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import penoplatinum.util.Utils;

public class WrappedLightSensor  {

    final LightSensor light;
    private int WHITEVAL = 100;
    private int BROWNVAL = 63;
    private int BLACKVAL = -3;

    public WrappedLightSensor() {
        //TODO: move out the sensorport
        light = new LightSensor(SensorPort.S4, true);
        light.setLow(344);
        light.setHigh(508);
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

        Utils.Log(BLACKVAL + "," + BROWNVAL + "," + WHITEVAL);
        Utils.Log(light.getLow() + "," + light.getHigh());
        Utils.Sleep(10000);


    }

    public int getRawLightValue() {
        return light.readNormalizedValue();
        
    }

}
