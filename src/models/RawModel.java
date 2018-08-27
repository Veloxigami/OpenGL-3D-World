package models;

/*
 * Used to represent the 3D model stored in memory
 */

public class RawModel {

	private int vaoID; // ID of the VAO
	private int vertextCount; // no. of vertices in the model
	
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertextCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertextCount() {
		return vertextCount;
	}
	
	
	
}
