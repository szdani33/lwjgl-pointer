package hu.dtms.lwjgl.pointer;

import org.lwjgl.opengl.Display;

public class MainLoop {
    private static final float[] VERTICES = {
            0, 0,
            0.028f, -0.092f,
            0.04f, -0.068f,
            0.066f , -0.068f
    };
    private static final int[] INDICES = {
            0, 1, 2,
            0, 2, 3
    };

    private static Renderer renderer = new Renderer();
    private static Loader loader = new Loader();
    private static ShaderProgram shaderProgram;
    private static SimpleModel simpleModel;

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        shaderProgram = new ShaderProgram();
        simpleModel = loader.loadSimpleModel(VERTICES, INDICES);
        while (!Display.isCloseRequested()) {
            renderer.prepare();
            shaderProgram.start();
            renderer.render(simpleModel);
            shaderProgram.stop();
            DisplayManager.updateDisplay();
        }
        shaderProgram.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
