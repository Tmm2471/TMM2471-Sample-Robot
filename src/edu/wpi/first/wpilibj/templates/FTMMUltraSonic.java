/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
/**
 *
 * @author FIRST
 */
public class FTMMUltraSonic {
    private AnalogChannel sonicRanger;
    private double sensitivity;
    
    
    FTMMUltraSonic(int chanSonic) {
       sonicRanger = new AnalogChannel(chanSonic);
       //sensitivity = 1000 / 9.8; //FIXME: Comp Bot
       sensitivity = 1000 / 13.6; //FIXME: Practice Bot
    }

    public double getDistance() {
        double V;
        V = sonicRanger.getAverageVoltage();

        return V * sensitivity;
    }
    public void setSensitivity(double _sensitivity) {
         sensitivity = _sensitivity;
    }
}
