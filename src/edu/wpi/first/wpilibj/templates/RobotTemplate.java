/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    CANJaguar motorFrontRight;
    CANJaguar motorFrontLeft;
    CANJaguar motorRearRight;
    CANJaguar motorRearLeft;
    DriverStationLCD lcd;
    RobotDrive rDrive;
    Joystick driveStick;  
    FTMMGyro gyro;
    
    boolean noGyro = false;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
         int can_error = 0;
        lcd = DriverStationLCD.getInstance();
        lcd.println(DriverStationLCD.Line.kUser2, 2, "                  ");
        lcd.updateLCD();
        boolean canInitOk = false;
        do {
            try {
                can_error = 2;
                if(motorFrontRight == null )
                    motorFrontRight = new CANJaguar(2);
                can_error = 5;
                if(motorFrontLeft == null )
                    motorFrontLeft = new CANJaguar(5);
                can_error = 9;
                if(motorRearRight == null )
                    motorRearRight = new CANJaguar(9);
                can_error = 8;
                if(motorRearLeft == null )
                    motorRearLeft = new CANJaguar(8);
            }
            catch(CANTimeoutException e) {
                System.out.println("CANBUS (id=" + can_error + ") TimeoutException\n" + e);
                lcd.println(DriverStationLCD.Line.kUser2, 2, "CANBUS ERROR: " + can_error);
                lcd.updateLCD();
            }

        } while ( canInitOk == false);
        rDrive = new RobotDrive(motorFrontLeft, motorRearLeft, motorFrontRight, motorRearRight);
        rDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        rDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        driveStick = new Joystick(1);
        gyro = new FTMMGyro(1);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        ;;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        DriveControl();
    }
     public void DriveControl() {
        double y = driveStick.getAxis(Joystick.AxisType.kY) * -1.0;  // Invert Y
        double x = driveStick.getAxis(Joystick.AxisType.kX) * -1.0; // Remove invert for gamepad
        double z = driveStick.getAxis(Joystick.AxisType.kZ);
        double a = driveStick.getAxis(Joystick.AxisType.kThrottle);

         if (driveStick.getRawButton(6) == false && driveStick.getRawButton(8) == false){
            // Non boost, aka slow mode
            y = y/2;
            //x = x/2;
            z = z/2;

        }
        mecanumDrive(x, y, z);
    }
    private double mecSumError = 0, mecLastError;
    
    public void mecanumDrive(double x, double y, double z) {
        double motor;
        z = z * -1.0;
        double t = gyro.getTurnRate() / -350.0;  // was -350
        double e = z - t;
        double a = gyro.getHeading();
        
        //System.out.println("X:" + x + "  Y:" + y+ "   Z:" + z);
        // Gyro deadband
        if(t > -0.075 && t < 0.075) {
            t = 0;
        }
        //e = e * 1; // P coeff
        motor = 1.4 * e + 0.5 * mecSumError + 0.5 * (e - mecLastError);  // was 0.35, 0.1, -0.10
        //System.out.println("M:" + motor + "   E:" + e + "   Z:" + z + "   T:" + t);
        //e = -1.0 * z;  // Uncomment to disable gyro correction
        if(noGyro == true) {
            motor = z;
        }
        rDrive.mecanumDrive_Cartesian(x, y, motor, 0); // For real bot
        mecSumError = mecSumError + e;
        if(mecSumError > 1) {
            mecSumError = 1;
        }
        if(mecSumError < -1) {
            mecSumError = -1;
        }
        mecLastError = e;
    }
    
}
