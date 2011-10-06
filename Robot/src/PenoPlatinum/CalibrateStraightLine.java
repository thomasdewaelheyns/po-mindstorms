/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

/**
 *
 * @author MHGameWork
 */
public class CalibrateStraightLine implements IAction {

    ActionParameters parameters = new ActionParameters();

    public ActionParameters GetParameters() {
        return parameters;
    }

    public void Execute() {
        System.out.println("Hellow all");
    }
}
