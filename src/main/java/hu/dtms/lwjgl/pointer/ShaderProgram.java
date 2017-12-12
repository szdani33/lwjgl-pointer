package hu.dtms.lwjgl.pointer;

import org.apache.commons.io.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;

public class ShaderProgram {
    private static final int SHADER_INFO_LOG_MAX_LENGTH = 1000;
    private static final String SHADER_FILE_LOCATION = "src/main/resources/shaders/";
    private static final String VERTEX_SHADER_FILE = SHADER_FILE_LOCATION + "vertexShader.txt";
    private static final String FRAGMENT_SHADER_FILE = SHADER_FILE_LOCATION + "fragmentShader.txt";
    private static final String TRANSFORMATION_MATRIX_NAME = "transformationMatrix";

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private final int shaderProgramId;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    private final int transformationMatrixLocation;

    public ShaderProgram() {
        vertexShaderId = loadShader(VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);
        shaderProgramId = createShaderProgram(vertexShaderId, fragmentShaderId);
        transformationMatrixLocation = GL20.glGetUniformLocation(shaderProgramId, TRANSFORMATION_MATRIX_NAME);
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        transformationMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(transformationMatrixLocation, false, matrixBuffer);
    }

    public void start() {
        GL20.glUseProgram(shaderProgramId);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(shaderProgramId, vertexShaderId);
        GL20.glDetachShader(shaderProgramId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(shaderProgramId);
    }

    private int createShaderProgram(int vertexShaderId, int fragmentShaderId) {
        int shaderProgramId = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgramId, vertexShaderId);
        GL20.glAttachShader(shaderProgramId, fragmentShaderId);
        GL20.glBindAttribLocation(shaderProgramId, 0, "vertex");
        GL20.glLinkProgram(shaderProgramId);
        GL20.glValidateProgram(shaderProgramId);
        return shaderProgramId;
    }

    private int loadShader(String shaderSourceFile, int shaderType) {
        String shaderSource = readShaderFile(shaderSourceFile);
        return compileShader(shaderType, shaderSource);
    }

    private int compileShader(int shaderType, String shaderSource) {
        int shaderId = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            handleShaderCompileError(shaderId);
        }
        return shaderId;
    }

    private void handleShaderCompileError(int shaderId) {
        System.out.println(GL20.glGetShaderInfoLog(shaderId, SHADER_INFO_LOG_MAX_LENGTH));
        System.err.println("Could not compile shader!");
        System.err.println(GL20.glGetShaderi(shaderId, 500));
        System.exit(-1);
    }

    private String readShaderFile(String shaderSourceFile) {
        try {
            return FileUtils.readFileToString(new File(shaderSourceFile), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
