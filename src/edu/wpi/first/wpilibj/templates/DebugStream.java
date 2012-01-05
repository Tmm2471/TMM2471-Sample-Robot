
// Class writes error reports with a time stamp to a text file in CSV format

package edu.wpi.first.wpilibj.templates;

import javax.microedition.io.Connector;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.*;

// @author Ryan Berg (c) 2011

public class DebugStream {

    PrintStream errorStream;
    Tick tick;
    
    public DebugStream(String file_name) throws IOException {
        FileConnection fc = (FileConnection) Connector.open(file_name);
        fc.create();
        errorStream = new PrintStream(fc.openOutputStream());

        tick = Tick.getInstance();
    }
    
    public void write(String message) {
        int currentTick = tick.getTick();
        errorStream.println(currentTick + "," + message);
    }
}