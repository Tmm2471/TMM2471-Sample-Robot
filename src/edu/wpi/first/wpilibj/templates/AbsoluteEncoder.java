package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.AnalogChannel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author FIRST
 */
public class AbsoluteEncoder {
    private AnalogChannel encoder;
    private double offset;
    private double sensitivity;
    private double maxDegrees, lastDegrees;
    private int rollovers;

    AbsoluteEncoder(int chanEncoder){
        encoder = new AnalogChannel(chanEncoder);
        offset = 0;
        sensitivity = 72;
        maxDegrees = 360;
        lastDegrees = getDegrees();
        rollovers = 0;
    }

    public double getTotalDegrees() {
        double degrees = getDegrees();
        if(degrees > 270 && lastDegrees < 90) {
            rollovers--;
        }
        if(degrees < 90 && lastDegrees > 270) {
            rollovers++;
        }
        lastDegrees = degrees;
        return degrees + rollovers * 360;
    }
    
    public double getDegrees(){
        double degrees;
        double getVolt = encoder.getVoltage();
        degrees = getVolt * sensitivity + offset;
        return degrees;
    }
    public double getVoltage() {
        return encoder.getVoltage();
    }
    public void setOffset(double _offset){
        offset = _offset;
    }
    public void setSensitivity(double _sensitivity){
        sensitivity = _sensitivity;
    }
    public void setMaxDegrees(double _maxDegrees) {
        maxDegrees = _maxDegrees;
    }
}
