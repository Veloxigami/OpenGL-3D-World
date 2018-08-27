package renderEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;
/*
 * This class deals with the loading of 3d models into memory
 *  by storing positional data about the 3d model in a VAO.
 */
public class Loader {
	
	List<Integer> vaos = new ArrayList<Integer>();
	List<Integer> vbos = new ArrayList<Integer>();
	List<Integer> textures = new ArrayList<Integer>();
	
	/*
	 * takes the positions of the model's vertices, 
	 * load this data into a vao and then return this information as a RawModel object
	 */
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals, int[] indices){
		
		//create an empty VAO and store it's ID in vaoID
		int vaoID = createVAO();
		
		
		bindIndicesBuffer(indices);
		//binding indices to VAO(auto since only 1 set of indices are there) and VBO
		
		//storing the positional data of the model into 0th attribute list of the VAO
		storeDataInAttributeList(0,3,positions);
		
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);

		
		//unbinding VAO
		unbindVAO();
		
		//returning the data and information about the VAO as a RawModel object
		return new RawModel(vaoID,indices.length);
		
	}
	
	public int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	
	//for cleaning up the memory used by VAOs and VBOs after closing of the application
	public void cleanUp(){
		for(int vao: vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo: vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture: textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	
	// for creating our new empty Vertex Array Object and then return the ID
	public int createVAO(){
		// GL30 used to generate Vertex Array Object's ID
		int vaoID = GL30.glGenVertexArrays();
		
		//added in vaos list 
		vaos.add(vaoID);
		
		// binding Vertex Array with ID of vaoID... to activate our VAO we have to bind it
		GL30.glBindVertexArray(vaoID);
		
		// now the Vertex Array Object with vaoID is returned 
		return vaoID;	
	}
	
	
	//for storing the data into one of the attributelist of the VAO
	public void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data){
		/*
		 *  create a Vertex Buffer Object ID since to store data into attribute list of VAO
		 *   the data should be in a VBO
		 */
		int vboID = GL15.glGenBuffers();
		
		//adding it to the cleaning list
		vbos.add(vboID);
		
		// binding the ID to the VBO of GL_ARRAY_BUFFER kind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		//creating the buffer of data to be stored in our VBO
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		
		/*
		 * storing the bufferdata in our VBO of type Array_buffer 
		 * and the data will be used for Static_Draw
		 */
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		/*
		 * putting the VBO into our VAO
		 * (no. of the attribute list,
		 * length of each vertex,
		 * type of data,
		 * normalized data ?,
		 * distance between each type of data,
		 * should reading start from the first position)	
		*/
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		// unbinding our current VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	
	// to break the binding of Vertex Array Object we pass 0 in GL30.glBindVertexArray()
	public void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	
	public void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	
	public IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	
	//since the data needs to be stored in the VBO in the form of Float we need to create a FloatBuffer
	public FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		//flip() indicates that buffer is ready to be read 
		buffer.flip(); 
		return buffer;
	}

}
