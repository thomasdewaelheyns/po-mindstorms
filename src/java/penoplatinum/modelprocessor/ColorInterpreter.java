/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.modelprocessor;

import penoplatinum.simulator.Model;

/**
 *
 * @author Daniel
 */
public class ColorInterpreter {
    
    private int blackBorder= 30;
    private int whiteBorder = 85;
    private Model model;
    
    public ColorInterpreter(){
    }
    
    public ColorInterpreter setModel(Model m){
        this.model = m;
        return this;
    }
    
    public int getLightValue() {
      // SERIOUSLY????? trying to create amazing bugs?
//        if(this.model == null){
//            return 70;
//        }
        Buffer temp = this.model.getLightValueBuffer();
        return temp.get(temp.getSize()-1);
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
