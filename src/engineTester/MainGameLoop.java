package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Shaders.StaticShader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import renderEngine.EntityRenderer;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args){
		
	
		
		  DisplayManager.createDisplay();
		  Loader loader = new Loader();		
		 
		  
		  
		  RawModel model = OBJLoader.loadObjModel("stanfordBunny", loader);
		  TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("dragon")));
		  ModelTexture texture = texturedModel.getTexture();
		  texture.setShineDamper(10);
		  texture.setReflectivity(1);		  
		  Entity entity = new Entity(texturedModel, new Vector3f(2,0,-77),0,0,0,1);
		  
		  Player player = new Player(texturedModel, new Vector3f(2,0,50),0,90,0,1);
		  
		  Light light = new Light(new Vector3f(0,800,1000), new Vector3f(1,1,1));
		  
		  
		  RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		  TexturedModel grassTexturedModel = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
		  List<Entity> grassList = new ArrayList<Entity>();
		  grassTexturedModel.getTexture().setHasTransparency(true);
		  grassTexturedModel.getTexture().setUseFakeLighting(true);
		  
		  RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
		  TexturedModel treeTexturedModel = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("tree")));
		  List<Entity> treesList = new ArrayList<Entity>();
		  Random rand = new Random();
		  
		  for(int i=0;i<2000;i++){
			  float xt = rand.nextFloat() *400 -200;
			  float zt = rand.nextFloat() *400 -200;
			  float xg = rand.nextFloat() *400 -200;
			  float zg = rand.nextFloat() *400 -200;
			  treesList.add(new Entity(treeTexturedModel, new Vector3f(xt,0,zt),0,0,0,1));
			  grassList.add(new Entity(grassTexturedModel,new Vector3f(xg,0,zg),0,0,0,1));
		  }
		  
		  TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		  TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		  TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		  TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		  TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				  gTexture, bTexture);
		  TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		  
		  Terrain terrain = new Terrain(-200, -200, loader, texturePack, blendMap);
		  //Terrain terrain2 = new Terrain(-1,-1,loader,texturePack,blendMap);
		  
		  Camera camera = new Camera(player);
		  
		  MasterRenderer renderer = new MasterRenderer();
	       
		  while(!Display.isCloseRequested()){
			  	entity.increaseRotation(0, 1, 0);
	        	entity.setScale(3);
	        	camera.move();
	        	player.move();
	        	renderer.processEntity(player);
	        	renderer.processTerrain(terrain);
	        	//renderer.processTerrain(terrain2);
	        	renderer.processEntity(entity);
	        	for(Entity tree: treesList){
	        		renderer.processEntity(tree);
	        	}
	        	for(Entity grass: grassList){
	        		renderer.processEntity(grass);
	        		grass.setScale(0.5f);
	        	}
	        	renderer.render(light, camera);
	            DisplayManager.updateDisplay();
	         
		  }
	        
		  renderer.cleanUp();
	      loader.cleanUp();
	      DisplayManager.closeDisplay();
	        
	}
	
}
