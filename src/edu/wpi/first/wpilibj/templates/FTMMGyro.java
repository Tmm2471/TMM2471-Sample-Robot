/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import java.lang.Thread;
import edu.wpi.first.wpilibj.Timer;


/**
 *
 * @author FIRST
 */
public class FTMMGyro implements Runnable {
    private AnalogChannel gyro;
    private double zerovolt;
    private double time;
    private double heading;
    private double sensitivity;
    
    
    FTMMGyro(int chanGyro) {
       gyro = new AnalogChannel(chanGyro);
       calibrate();
       time = 0.01;
       heading = 0.0;
       sensitivity = 0.0035;  // For FRC provided gyro
       //sensitivity = 0.00625;  // For Sparkfun provided gyro

       Thread t = new Thread(this);
       t.start();
    }
    public void run() {
        while(true) {
            run_hook();
            heading = heading + getTurnRate()*time;

            Timer.delay(time);
        }
    }
     protected void run_hook() {
        // Children access run loop here.
        return;
    }
    public double getHeading() {
        return heading;
    }

    public void calibrate() {
        int i;
         zerovolt = gyro.getAverageVoltage();
//        for(i = 1; i <= 20; i++) {
//            zerovolt =  (zerovolt + gyro.getAverageVoltage()) / 2;
//        }
    }
    public double getTurnRate() {
        double V;
        V = gyro.getAverageVoltage();
        // To do if V within dead band average with zerovolt 
        return ((V - zerovolt) /sensitivity);
    }
    public void setSensitivity(double _sensitivity) {
         sensitivity = _sensitivity;
    }
}
