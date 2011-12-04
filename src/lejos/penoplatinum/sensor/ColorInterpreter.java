/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.sensor;

import penoplatinum.simulator.Model;

/**
 *
 * @author: Team Platinum
 */
public class ColorInterpreter {
    
    private int blackBorder= 30;
    private int whiteBorder = 85;
    private Model model;
    
    public ColorInterpreter(Model model){
        this.model = model;
    }
    
    public int getLightValue() {

        return this.model.getSensorValue(this.model.S4);
    }

    public byte readValue() {
        return (byte) getLightValue();
    }

    public enum Color {

        Black, White, Brown;
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
