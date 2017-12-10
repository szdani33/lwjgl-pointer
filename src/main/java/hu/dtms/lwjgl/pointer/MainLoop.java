package hu.dtms.lwjgl.pointer;

import org.lwjgl.opengl.Display;

public class MainLoop {

    private static final float[] RECTANGLE_VERTICES = {
            -.2f, .2f,
            -.2f, 0,
            0, 0,
            0, .2f
    };
    private static final int[] RECTANGLE_INDICES = {
            0, 1, 2,
            0, 2, 3
    };

    private static final float[] POINTER_VERTICES = {
            0, 0,
            0.028f, -0.092f,
            0.04f, -0.068f,
            0.066f , -0.068f
    };
    private static final int[] POINTER_INDICES = {
            0, 1, 2,
            0, 2, 3
    };

    private static Renderer renderer = new Renderer();
    private static Loader loader = new Loader();
    private static ShaderProgram shaderProgram;
    private static SimpleModel pointerModel;
    private static SimpleModel rectangleModel;

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        shaderProgram = new ShaderProgram();
        pointerModel = loader.loadSimpleModel(POINTER_VERTICES, POINTER_INDICES);
        rectangleModel = loader.loadSimpleModel(RECTANGLE_VERTICES, RECTANGLE_INDICES);
        while (!Display.isCloseRequested()) {
            renderer.prepare();
            shaderProgram.start();
            renderer.render(rectangleModel);
            renderer.render(pointerModel);
            shaderProgram.stop();
            DisplayManager.updateDisplay();
        }
        shaderProgram.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
