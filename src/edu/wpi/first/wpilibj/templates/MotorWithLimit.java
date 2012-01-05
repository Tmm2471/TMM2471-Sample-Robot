/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.lang.Thread;

/**
 *
 * @author FIRST
 */
public class MotorWithLimit implements Runnable {
    protected DigitalInput limitDown;
    protected DigitalInput limitUp;
    protected CANJaguar motor;
    protected CANJaguar motorB;
    private double upSpeed, downSpeed;
    protected double setSpeed;
    protected double period;
    private boolean ignoreLimit;
    private Thread t;


    MotorWithLimit(int chanLimitUp, int chanLimitDown, int chanMotor, int chanMotorB) {
       int chan = 0;
       limitDown = new DigitalInput(chanLimitDown);
       limitUp = new DigitalInput(chanLimitUp);

       try {
           chan = chanMotorB;
           motorB = new CANJaguar(chanMotorB);
           chan = chanMotor;
           motor = new CANJaguar(chanMotor);
        }
        catch(CANTimeoutException e) {
            System.out.println("\n\n\n********************************************\n\n");
            System.out.println("CANBUS TimeoutException!  CHAN:" + chan + "\n" + e);
            System.out.println("\n\n**********************************************\n\n\n\n");
        }
       upSpeed = 0.10;
       downSpeed = -0.10;
       setSpeed = 0.0;
       period = 0.05;
       ignoreLimit = false;

       t = new Thread(this);
       t.setPriority(4);
       t.start();
    }
    public void run() {
        while(true) {
            runner();
            Timer.delay(period);
        }
    }

    public void runner() {
        run_hook();

        if ((setSpeed > 0 && upSpeed > 0)  || (setSpeed < 0 && upSpeed < 0) ){  // going towards limitUp (For the yays)
            if (limitUp.get() == false  && ignoreLimit == false) {
                setSpeed = 0;
            }
        }
        if ((setSpeed < 0 && upSpeed > 0)  || (setSpeed > 0 && upSpeed < 0) ){  // going towards limitDown (For the nays)
            if (limitDown.get() == true && ignoreLimit == false) {
                setSpeed = 0;
            }
        }
        if ((setSpeed > upSpeed && upSpeed > 0)  || (setSpeed < upSpeed && upSpeed < 0) ){  // going towards limitUp (For the yays)
            setSpeed = upSpeed;
        }
        if ((setSpeed > downSpeed && downSpeed > 0)  || (setSpeed < downSpeed && downSpeed < 0) ){  // going towards limitDown (For the nays)
            setSpeed = downSpeed;
        }

        try {
            if(motor == null) {
                System.out.println("motor is null");
            }
            motor.setX(setSpeed); //, (byte) 10);
            motorB.setX(setSpeed); //, (byte) 10);
            //CANJaguar.updateSyncGroup( (byte) 10);
        }
        catch (CANTimeoutException e) {
            System.out.println("CAN Timeout in MotorLimit.run()");
        }
    }
    protected void run_hook() {
        // Children access run loop here.
        System.out.println("Parent hook");
        return;
    }
    public void setPeriod(double _period){
        period = _period;
    }
    public void moveUp() {
        setSpeed = upSpeed;
       
    }
    public void moveDown() {
        setSpeed = downSpeed;
       
    }
    public void stop() {
        setSpeed = 0;
    }
    public void setUpSpeed(double speed) {
        upSpeed = speed;
    }
    public void setDownSpeed(double speed) {
        downSpeed = speed;
    }
     public void setSpeed(double speed) {
        if ((speed > upSpeed && upSpeed > 0)  || (speed < upSpeed && upSpeed < 0) ){  // going towards limitUp (For the yays)
            speed = upSpeed;
       }
       if ((speed > downSpeed && downSpeed > 0)  || (speed < downSpeed && downSpeed < 0) ){  // going towards limitDown (For the nays)
            speed = downSpeed;
       }
        setSpeed = speed;
    }

     public void setIgnoreLimit(boolean _ignore) {
         ignoreLimit = _ignore;
     }

}
