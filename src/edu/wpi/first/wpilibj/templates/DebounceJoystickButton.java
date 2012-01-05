/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author FIRST
 */
public class DebounceJoystickButton {
    Joystick stick;
    int button;
    Tick tick;
    int startTick, timeout;

    DebounceJoystickButton(Joystick _stick, int _button) {
        stick = _stick;
        button = _button;
        tick = Tick.getInstance();
        startTick = 0;
        timeout = 30;
    }

    public boolean getButton() {
        if (stick.getRawButton(button) == true && tick.getTick() - startTick > timeout) {
           startTick = tick.getTick();
           return true;
        }
        return false;
    }
}
