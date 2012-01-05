/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author FIRST
 */
public class MotorWithAbsEncoder implements Runnable {
    AbsoluteEncoder encoder;
    private double targetDegrees;
    private double P;
    private double deltaDistance;
    private double upSpeed, downSpeed, ignoreEncoderSpeed = 0;
    private double softMinDegrees, softMaxDegrees;
    private double  vel = 0, lastDist;
    protected double setSpeed = 0, error;
    protected double period = 0.2;
    private boolean ignoreEncoder = false;

    private CANJaguar motor;
    MotorWithAbsEncoder(int chanMotor, int chanEncoder) {
        softMinDegrees = 35;
        softMaxDegrees = 234;
        try {
            motor = new CANJaguar(chanMotor);
        }
        catch(CANTimeoutException e) {
            System.out.println("CANBUS TimeoutException\n" + e);
        }
        encoder = new AbsoluteEncoder(chanEncoder);
        P = 0.00012;
        deltaDistance = 40;
        targetDegrees = encoder.getDegrees();
        lastDist = encoder.getDegrees();
       Thread t = new Thread(this);
       t.setPriority(4);
       t.start();
    }

    public void setDegrees(double _targetDegrees) {
        targetDegrees = _targetDegrees;
    }

    public double getDegrees() {
        return encoder.getDegrees();
    }

     public void run() {
         double dist;
        while(true) {
            run_hook();

            // Handle overflow
//            if (targetDegrees > 360) {
//                targetDegrees = targetDegrees - 360;
//            }
//            // Handle underflow
//            if (targetDegrees < 0) {
//                targetDegrees = targetDegrees + 360;
//            }

            // Handle max soft limit
            if (targetDegrees > softMaxDegrees) {
                targetDegrees = softMaxDegrees;
            }
            // Mandle min soft limit
            if (targetDegrees < softMinDegrees) {
                targetDegrees = softMinDegrees;
            }
             dist = encoder.getDegrees();
             vel = (dist - lastDist) / period;
             vel = vel / 300;  // Normalize to -1..0..1
             error = (targetDegrees - dist) / deltaDistance;  // Normalize closer to -5..0..5

            if(error > -0.25 && error < 0.25) {
                error = 0.75 * error;
            }
            if(error > -0.05 && error < 0.05) {
                error = 0;
            }
// Forget maxDegrees
//            while (error > 0.5 *maxDegrees) {
//                //System.out.println("Fix overflow error: " + error + "  T: " + targetDegrees + "  D: " + encoder.getDegrees());
//                error = error - maxDegrees;
//            }
//            while (error < -0.5 * maxDegrees) {
//                //System.out.println("Fix underflow error: " + error + "  T: " + targetDegrees + "  D: " + encoder.getDegrees());
//                error = error + maxDegrees;
//            }
            if(error > 0) {
                if(setSpeed > 0.08) {
                    setSpeed = setSpeed + 0.2 * (error - vel);
                }
                else {
                    setSpeed = 0.08 + 0.2 * (error - vel);
                }
            }
            else {
                if(setSpeed <  -0.08) {
                    setSpeed = setSpeed + 0.2 * (error - vel);
                }
                else {
                    setSpeed = -0.08 + 0.2 * (error - vel);
                }
            }
            lastDist = dist;

            if ((setSpeed > upSpeed && upSpeed > 0)  || (setSpeed < upSpeed && upSpeed < 0) ){  // going towards limitUp (For the yays)
                setSpeed = upSpeed;
            }
            if ((setSpeed > downSpeed && downSpeed > 0)  || (setSpeed < downSpeed && downSpeed < 0) ){  // going towards limitDown (For the nays)
                setSpeed = downSpeed;
            }

            if(ignoreEncoder == false) {
                motor.set(setSpeed * -1.0);
            }
            else {
                motor.set(ignoreEncoderSpeed);
                //System.out.println("MI  " + ignoreEncoderSpeed);
            }

            Timer.delay(period);
        }
    }
    protected void run_hook() {
    }
    public void moveUp() {
        setDegrees(encoder.getDegrees() + deltaDistance);
    }
    public void moveDown() {
        setDegrees(encoder.getDegrees() - deltaDistance);
    }
    public void setSpeed(double _speed) {
        setDegrees(encoder.getDegrees() + deltaDistance * _speed);
        ignoreEncoderSpeed = _speed;
    }
    public void setP(double _P){
        P = _P;
    }

    public void setPeriod(double _period){
        period = _period;
    }
    public void stop() {
        setSpeed = 0;
        ignoreEncoderSpeed = 0;
    }
    public void setUpSpeed(double speed) {
        upSpeed = speed;
    }
    public void setDownSpeed(double speed) {
        downSpeed = speed;
    }

    public void setOffset(double _offset) {
        encoder.setOffset(_offset);
    }

    public void setSensitivity(double _sensitivity) {
        encoder.setSensitivity(_sensitivity);
    }
    public void setDeltaDistance(double _deltaDistance) {
        deltaDistance = _deltaDistance;
    }
    public void setSoftMinDegrees(double _minDegrees) {
        softMinDegrees = _minDegrees;
    }
    public void setSoftMaxDegrees(double _maxDegrees) {
        softMaxDegrees = _maxDegrees;
    }
    public double wristDegrees() {
        return encoder.getDegrees();
    }

    public void setIgnoreEncoder(boolean _ignoreEncoder) {
        ignoreEncoder = _ignoreEncoder;
    }
    public void printDebug() {
       // System.out.println("Error: " + error + "   T:"  +  targetDegrees + "   D:" + encoder.getDegrees() +  "  Spd:" + setSpeed + "  Vel:" + vel);
        System.out.println("   WristEncoder:" + encoder.getDegrees());
//        System.out.println("Motorspeed: " + ignoreEncoderSpeed);
    }

}
