package Shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolBox.Maths;

public class TerrainShader extends ShaderProgram {
	
	private final static String VERTEX_SHADER = "src/Shaders/terrainVertexShader.txt";
	private final static String FRAGMENT_SHADER = "src/Shaders/terrainFragmentShader.txt";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	
	public TerrainShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLoaction("transformationMatrix");
		location_projectionMatrix = super.getUniformLoaction("projectionMatrix");
		location_viewMatrix = super.getUniformLoaction("viewMatrix");
		location_lightPosition = super.getUniformLoaction("lightPosition");
		location_lightColour = super.getUniformLoaction("lightColour");
		location_shineDamper = super.getUniformLoaction("shineDamper");
		location_reflectivity = super.getUniformLoaction("reflectivity");
		location_skyColour = super.getUniformLoaction("skyColour");
		location_backgroundTexture = super.getUniformLoaction("background");
		location_rTexture = super.getUniformLoaction("rTexture");
		location_gTexture = super.getUniformLoaction("gTexture");
		location_bTexture = super.getUniformLoaction("bTexture");
		location_blendMap = super.getUniformLoaction("blendMap");
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}

	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());;
		super.loadVector(location_lightColour, light.getColours());
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
