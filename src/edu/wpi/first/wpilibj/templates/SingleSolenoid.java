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
public class SingleSolenoid {
    Solenoid single;
    boolean toggle;

    // Constructor(s)
    SingleSolenoid(int chanSingle) {
       single = new Solenoid(chanSingle);
       toggle = true;
    }
    // Methods
    public void up(){
        single.set(true);
        System.out.println("DualS up");
    }
    public void down(){
        single.set(false);
        System.out.println("DualS down");
    }
}
