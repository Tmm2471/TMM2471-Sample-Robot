/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import java.lang.Thread;

/**
 *
 * @author FIRST
 */
public class CompressorControl implements Runnable {
    // private vars
    private Relay compressor;
    private DigitalInput pressureSwitch;

    // Constructor(s)
    CompressorControl(int chanCompressor, int chanPS) {
        compressor = new Relay(chanCompressor, Relay.Direction.kForward);
        pressureSwitch = new DigitalInput(chanPS);

        // Start background task
        Thread t = new Thread(this);
        t.start();
    }
    // Public Methods
    public void runner() {
       // while(true) {
            if(pressureSwitch.get() == false) {
                // low pressure, turn on compressor
                compressor.set(Relay.Value.kOn);
                //System.out.println("Compressor on");
            }
            else {
                // high pressure, turn off compressor
                compressor.set(Relay.Value.kOff);
            }
      //  }
    }
    // Private Methods
    public void run() {
        while(true) {
            runner();
            Timer.delay(0.25);
        }
    }
}
