package penoplatinum.barcode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Thomas
 */
public class TranslateTest {

    private ArrayList<Integer> barcodeInput;

    public static void main(String[] Args) {
        ArrayList<Integer> testInput = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            testInput.add(0);
        }

        for (int i = 7; i < 14; i++) {
            testInput.add(93);
        }

        for (int i = 14; i < 21; i++) {
            testInput.add(33);
        }
        for (int i = 21; i < 28; i++) {
            testInput.add(89);
        };
        for (int i = 28; i < 35; i++) {
            testInput.add(0);
        }
        for (int i = 35; i < 42; i++) {
            testInput.add(100);
        }
        for (int i = 42; i < 49; i++) {
            testInput.add(0);
        }

        TranslateTest test = new TranslateTest();
        test.barcodeInput = testInput;


        test.testReader();


    }

    public void testTranslate() {
        BarcodeInterpreter interpreter = new BarcodeInterpreter();


        System.out.print("" + interpreter.translate(barcodeInput));
    }

    public void testReader() {

        byte[] input = new byte[barcodeInput.size() + 30];

        for (int i = 0; i < 15; i++) {
            input[i] = (70);
        }
        for (int i = 15; i < 14 + barcodeInput.size(); i++) {
            input[i] = barcodeInput.get(i - 15).byteValue();
        }
        for (int i = input.length - 15; i <  input.length; i++) {
            input[i] = (70);
        }

        BarcodeReader reader = new BarcodeReader(new lightSensorSim(input));

        System.out.print(""+reader.read());

    }
}
