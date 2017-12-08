package hu.dtms.lwjgl.pointer;

import org.lwjgl.opengl.Display;

public class MainLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        while (!Display.isCloseRequested()) {
            DisplayManager.updateDisplay();
        }
        DisplayManager.closeDisplay();
    }
}
