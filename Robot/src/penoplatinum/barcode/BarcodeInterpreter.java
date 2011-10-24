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
public class BarcodeInterpreter {

    public static int BlackBrownBorder = 30;
    public static int BrownWhiteBorder = 90;

    public int translate(ArrayList<Integer> list) {
        if(list.size()<7){return -1;}
        int val = 0;
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            for (int j = (i * list.size()) / 7; j < (i + 1) * list.size() / 7; j++) {
                sum += list.get(j);
            }
            int averageValue = sum / (((i + 1) * list.size() / 7) - ((i * list.size()) / 7));
            val*=2;
            if (isColor(averageValue) != 0) {
                val+=1;
            } else {
            }
        }
        //return correct(val); // here we can add a hamming correction function
        return val;
    }

    public int correct(int value) {
        BarcodeData temp = new BarcodeData();
        byte corrected = (byte) (temp.getBarcodesRepair(value) / 8);
        return corrected;

    }

    private int isColor(int value) {
        if (value < ((BlackBrownBorder + BrownWhiteBorder) / 2)) {
            return 0;
        } else {
            return 1;
        }
    }
}
