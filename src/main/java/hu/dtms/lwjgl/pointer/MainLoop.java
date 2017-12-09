package hu.dtms.lwjgl.pointer;

import org.lwjgl.opengl.Display;

public class MainLoop {
    private static final float[] VERTICES = {
            0, 0,
            0.1f, -0.1f,
            0.1f, 0
    };

    private static Renderer renderer = new Renderer();
    private static Loader loader = new Loader();
    private static SimpleModel simpleModel;

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        simpleModel = loader.loadSimpleModel(VERTICES);
        while (!Display.isCloseRequested()) {
            renderer.prepare();
            renderer.render(simpleModel);
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
