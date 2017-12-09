package hu.dtms.lwjgl.pointer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private static final int VERTEX_SIZE = 2;
    private List<Integer> vaoIds = new ArrayList<>();
    private List<Integer> vboIds = new ArrayList<>();

    public SimpleModel loadSimpleModel(float[] vertices) {
        int vaoId = createVAO();
        bindVAO(vaoId);
        storeDataInVAO(0, vertices, VERTEX_SIZE);
        unbindVAO();
        return new SimpleModel(vaoId, vertices.length / VERTEX_SIZE);
    }

    public void cleanUp() {
        for (int vaoId : vaoIds) {
            GL30.glDeleteVertexArrays(vaoId);
        }
        for (int vboId : vboIds) {
            GL15.glDeleteBuffers(vboId);
        }
    }

    private void storeDataInVAO(int attributeIndex, float[] data, int vertexSize) {
        int vboId = createVBO();
        bindVBO(vboId);
        bufferDataInVBO(data);
        addVBOToVAO(attributeIndex, vertexSize);
        unbindVBO();
    }

    private void addVBOToVAO(int attributeIndex, int vertexSize) {
        GL20.glVertexAttribPointer(attributeIndex, vertexSize, GL11.GL_FLOAT, false, 0, 0);
    }

    private void bufferDataInVBO(float[] data) {
        FloatBuffer floatBuffer = createFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    private void unbindVBO() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindVBO(int vboId) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
    }

    private int createVBO() {
        int vboId = GL15.glGenBuffers();
        vboIds.add(vboId);
        return vboId;
    }

    private void bindVAO(int vaoId) {
        GL30.glBindVertexArray(vaoId);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private int createVAO() {
        int vaoId = GL30.glGenVertexArrays();
        vaoIds.add(vaoId);
        return vaoId;
    }
}
