/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Solenoid;
import java.lang.Thread;

/**
 *
 * @author FIRST
 */
public class DualSolenoid {
    // Private vars
    Solenoid high;
    Solenoid low;
    boolean toggle;

    // Constructor(s)
    DualSolenoid(int chanHigh, int chanLow) {
       high = new Solenoid(chanHigh);
       low = new Solenoid(chanLow);
       toggle = true;
    }
    // Methods
    public void up(){
        high.set(false);
        low.set(true);
        System.out.println("DualS up");
    }
    public void down(){
        high.set(true);
        low.set(false);
        System.out.println("DualS down");
    }

}
