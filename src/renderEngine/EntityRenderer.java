package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Shaders.StaticShader;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import textures.ModelTexture;
import toolBox.Maths;

public class EntityRenderer {
	
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	// preparing the stage or the background 
	
	
	public void render(Map<TexturedModel, List<Entity>> entities){
		for(TexturedModel model: entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity: batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertextCount(),GL11.GL_UNSIGNED_INT,0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		// bind vao to use it
		GL30.glBindVertexArray(rawModel.getVaoID());
		//we need to activate the attribute list in which we stored our VBO
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		if(texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());		
		/*
		 * now we render 
		 * what is it rendering,
		 * no. of vertices to render,
		 * type of data given for draw, 
		 * where to start in the array ()offset
		 */
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(),
				entity.getRotX(),
				entity.getRotY(), 
				entity.getRotZ(), 
				entity.getScale());	
		shader.loadTransformationMatrix(transformationMatrix);
	}

	private void unbindTexturedModel(){
				MasterRenderer.enableCulling();
		// after using VAO attribute list we will disable our attribute list
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(1);
				GL20.glDisableVertexAttribArray(2);
				// and now we unbind our VAO
				GL30.glBindVertexArray(0);		
	}	
	
}
