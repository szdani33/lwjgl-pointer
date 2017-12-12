package hu.dtms.lwjgl.pointer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class MainLoop {
    private static final float SPEED = 0.005f;

    private static final float[] RECTANGLE_VERTICES = {
            -.1f, .25f,
            -.1f, .05f,
            .1f, .05f,
            .1f, .25f
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
    private static final Matrix4f IDENTITY_MATRIX;

    private static Renderer renderer = new Renderer();
    private static Loader loader = new Loader();
    private static ShaderProgram shaderProgram;
    private static SimpleModel pointerModel;
    private static SimpleModel rectangleModel;

    static {
        IDENTITY_MATRIX = new Matrix4f();
        IDENTITY_MATRIX.setIdentity();
    }

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        handleMouse();
                        handleKeyboard();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void handleMouse() {
                while (Mouse.next()) {
                    int eventDX = Mouse.getEventDX();
                    System.out.println(eventDX);
                }
            }

            private void handleKeyboard() {
                while (Keyboard.next()) {
                    int eventKey = Keyboard.getEventKey();
                    boolean eventKeyState = Keyboard.getEventKeyState();
                    System.out.println(System.currentTimeMillis() + ": " + eventKey + ", " + eventKeyState);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        shaderProgram = new ShaderProgram();
        pointerModel = loader.loadSimpleModel(POINTER_VERTICES, POINTER_INDICES);
        rectangleModel = loader.loadSimpleModel(RECTANGLE_VERTICES, RECTANGLE_INDICES);
        boolean increase = true;
        Matrix4f pointerTransformationMatrix = new Matrix4f();
        pointerTransformationMatrix.setIdentity();
        while (!Display.isCloseRequested()) {
            renderer.prepare();
            shaderProgram.start();
            renderer.render(rectangleModel, shaderProgram, IDENTITY_MATRIX);
            increase = getDirection(pointerTransformationMatrix.m30, increase);
            updatePointerMatrix(pointerTransformationMatrix, increase);
            renderer.render(pointerModel, shaderProgram, pointerTransformationMatrix);
            shaderProgram.stop();
            DisplayManager.updateDisplay();
        }
        shaderProgram.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static boolean getDirection(float m03, boolean increase) {
        if (m03 < -.3f && !increase || m03 > .3f && increase) {
            return !increase;
        }
        return increase;
    }

    private static void updatePointerMatrix(Matrix4f pointerTransformationMatrix, boolean increase) {
        if (increase) {
            pointerTransformationMatrix.translate(new Vector2f(SPEED, 0));
        } else {
            pointerTransformationMatrix.translate(new Vector2f(SPEED * -1, 0));
        }
    }
}
