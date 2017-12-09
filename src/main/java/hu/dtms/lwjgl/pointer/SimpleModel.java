package hu.dtms.lwjgl.pointer;

public class SimpleModel {

    private final int vaoId;
    private final int vertexCount;

    public SimpleModel(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getIndexCount() {
        return vertexCount;
    }
}
