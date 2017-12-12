package hu.dtms.lwjgl.pointer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class Renderer {

    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0, 0, .33f, 1f);
    }

    public void render(SimpleModel simpleModel, ShaderProgram shaderProgram, Matrix4f transformationMatrix) {
        GL30.glBindVertexArray(simpleModel.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        shaderProgram.loadTransformationMatrix(transformationMatrix);
        GL11.glDrawElements(GL11.GL_TRIANGLES, simpleModel.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
