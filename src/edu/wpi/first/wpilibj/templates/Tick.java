/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author FIRST
 */
public class Tick {
    private int tick;
    private static Tick singleton;

    private Tick() {
        tick = 0;
    }

    public static  Tick getInstance() {
        if(singleton == null) {
            singleton = new Tick();
        }
        return singleton;
    }

    public void tock() {
        tick = tick + 1;
    }

    public int getTick() {
        return tick;
    }

    public double getPeriod() {
        return 0.01;
    }
}
