package MainGameLoop;

import GUIs.GuiRenderer;
import GUIs.GuiTexture;
import ObjConverter.ModelData;
import ObjConverter.OBJFileLoader;
import Records.Record;
import RenderEngine.*;
import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.RawModel;
import models.TextureModel;

import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import org.lwjgl.util.vector.Vector4f;
import particles.*;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class main {

    public static void main(String[] args) throws IOException {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);
        MasterRenderer renderer = new MasterRenderer(loader);
        ParticleMaster.init(loader, renderer.getProjectionMatrix());

        FontType fontType = new FontType(loader.loadFontTexture("candara"),
                new File("res/candara.fnt"));
        GUIText text =new GUIText("Hello! This is a 3D Game", 3, fontType,
                new Vector2f(0.5f, 0.5f), 0.5f, true);


        //--------------Terrain Textures------------------------------

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
                gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));


        //--------------------------Models------------------------------------

        TextureModel plane1Model = new TextureModel(OBJLoader.loadObjModel("Plane01", loader),
                new ModelTexture(loader.loadTexture("Plane01")));
        TextureModel plane2Model = new TextureModel(OBJLoader.loadObjModel("Plane02", loader),
                new ModelTexture(loader.loadTexture("Plane02")));
        TextureModel plane3Model = new TextureModel(OBJLoader.loadObjModel("Plane03", loader),
                new ModelTexture(loader.loadTexture("Plane03")));
//        ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern1"));
//        fernTexture.setNumberOfRows(2);
//        TextureModel fern = new TextureModel(OBJLoader.loadObjModel("fern", loader),
//                fernTexture);
//        TextureModel bobble = new TextureModel(OBJLoader.loadObjModel("cherry", loader),
//                new ModelTexture(loader.loadTexture("cherry")));
//        TextureModel lamp = new TextureModel(OBJLoader.loadObjModel("lamp", loader),
//                new ModelTexture(loader.loadTexture("lamp")));
        TextureModel rockModel = new TextureModel(OBJLoader.loadObjModel("rock", loader),
                new ModelTexture(loader.loadTexture("rock")));
//        TextureModel barrelModel = new TextureModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
//                new ModelTexture(loader.loadTexture("barrel")));
        TextureModel boulderModel = new TextureModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadTexture("boulder")));
        TextureModel planetModel = new TextureModel(NormalMappedObjLoader.loadOBJ("planet", loader),
                new ModelTexture(loader.loadTexture("planet")));
        TextureModel raven = new TextureModel(OBJLoader.loadObjModel("raven", loader),
                new ModelTexture(loader.loadTexture("raven")));
        TextureModel cubeModel = new TextureModel(OBJLoader.loadObjModel("cube", loader),
                new ModelTexture(loader.loadTexture("image")));

        planetModel.getTexture().setNormalMap(loader.loadTexture("planet_normal"));
        planetModel.getTexture().setShineDamper(8);
        planetModel.getTexture().setReflectivity(0.4f);

        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);


        /*---------------------------------Entities---------------------------------------------------------*/

        List<Light> lights = new ArrayList<Light>();
        List<Terrain> terrains = new ArrayList<>();
        List<Entity> normalMapEntities = new ArrayList<Entity>();
        List<Entity> entities = new ArrayList<Entity>();

        Entity Plane1;
        Entity Plane2;
        Entity Plane3;
        Entity planet1;
        Entity planet2;
        Entity rock = null;
        Entity boulder = null;
        Entity cube = new Entity(cubeModel, new Vector3f(100, 4, -200), 0,0,0,2);

        Light planetLight = new Light(new Vector3f(100, 0, -50), new Vector3f(1.5f,1.5f,1.5f),
                new Vector3f(100.5f, 100.5f, 100.5f));

        /*---------------------------------Terrains---------------------------------------------------------*/
        Terrain terrain1 = new Terrain(0,-1, loader, texturePack, blendMap, "heightmap");
        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightmap");


        terrains.add(terrain1);
//        terrains.add(new Terrain(-1,0, loader, texturePack, blendMap, "heightmap"));
//        terrains.add(new Terrain(0,0, loader, texturePack, blendMap, "heightmap"));


        Random random = new Random(674895);

        for (int i = 0; i < 800; i++) {

//                //checks if random generated x and z coords are on the terrain
//                if (x < ter.getX() + ter.getSize() && x >= ter.getX() && z >= ter.getZ()
//                        && z < ter.getZ() + ter.getSize()) {

            if (i % 2 == 0) {
                float x = random.nextFloat() * 2000 ;
                float z = random.nextFloat() * 1000;
                float y = random.nextFloat() * 50 ;
                rock = new Entity(rockModel, new Vector3f(x, y, z), x,y,z,10);
                entities.add(rock);
                x = random.nextFloat() * 2000;
                z = random.nextFloat() * 1000;
                y = random.nextFloat() * 50 ;
                boulder = new Entity(boulderModel, new Vector3f(x, y, z), x,y,z,1);
                normalMapEntities.add(boulder);
            }
            if (i % 5 == 0) {
                float x = random.nextFloat() * 2000;
                float z = random.nextFloat() * 1000;
                float y = random.nextFloat() * 100;
                Plane1 = new Entity(plane1Model, new Vector3f(x, y, z), 0,0,0,1);
                entities.add(Plane1);
                x = random.nextFloat() * 2000;
                z = random.nextFloat() * 1000;
                y = random.nextFloat() * 100 ;
                Plane2 = new Entity(plane2Model, new Vector3f(x, y, z), 0,0,0,1);
                entities.add(Plane2);
                x = random.nextFloat() * 2000;
                z = random.nextFloat() * 1000;
                y = random.nextFloat() * 100 ;
                Plane3 = new Entity(plane3Model, new Vector3f(x, y, z), 0,0,0,1);
                entities.add(Plane3);
            }
        }
        planet1 = new Entity(planetModel, new Vector3f(500, -5, 500), 0,0,0,200);
        planet2 = new Entity(planetModel, new Vector3f(-500, -5, 500), 0,0,0,200);
        lights.add(planetLight);


        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);


        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.70f, 0.88f),
                new Vector2f( 0.2f, 0.2f));
        guis.add(gui2);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        Player player = new Player(raven, new Vector3f(200, 4, -50), 0,180,0,1f);


        Entity bullet = new Entity(cubeModel, new Vector3f(player.getPosition()), 0,0,0, 0.5f);
//        entities.add(bullet);

        Camera camera = new Camera(player);
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain1);
//        String name = JOptionPane.showInputDialog("Enter your name : ");
//**********Water Renderer Set-up************************

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<WaterTile>();
        WaterTile water = new WaterTile(75, -75, 0);
        waters.add(water);
        Record record = new Record();

        ParticleTexture particleTexture1 = new ParticleTexture(loader.loadTexture("particleAtlas"), 4, false);
        ParticleTexture particleTexture2 = new ParticleTexture(loader.loadTexture("cosmic"), 4, true);

        ParticleSystem particleSystem1 = new ParticleSystem(particleTexture1,90, 15, 0.1f, 2f);
        ParticleSystem particleSystem2 = new ParticleSystem(particleTexture2,50, 10, 0.1f, 1.5f);
//        particleSystem.randomizeRotation();
//        particleSystem.setDirection(new Vector3f(0,1,0), 0.1f);
//        particleSystem.setLifeError(0.1f);
//        particleSystem.setScaleError(0.1f);
//        particleSystem.setSpeedError(0.4f);

//        try {
//            if (record.checkName(name)) {
//                JOptionPane.showMessageDialog(null, "Welcome back, " + name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
//
//            } else {
//                JOptionPane.showMessageDialog(null, "Looks like you're not registered.", "Error", JOptionPane.ERROR_MESSAGE);
//                String username = JOptionPane.showInputDialog("Enter your Username : ");
////                record.registerPlayer(username);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
        Entity bb = new Entity(boulderModel, new Vector3f(200, 2, -80), 0,0,0,1);
        Bullet bullet1 = new Bullet(cubeModel, new Vector3f(player.getPosition()), new Vector3f(0, 20, -500), 5);

        Particle particle ;

        while(!Display.isCloseRequested()) {

            renderer.processEntity(player);
            renderer.processEntity(cube);
            renderer.processEntity(planet1);
            renderer.processEntity(planet2);
            player.move();
            camera.move();
            picker.update();
//            bullet.update();
//            if(Keyboard.isKeyDown(Keyboard.KEY_Y)) {
                particle = new Particle(particleTexture2, new Vector3f(player.getPosition()), new Vector3f(0, 2, 100),
                        0.2f, 4, 1, 0);
//            }
//            System.out.println(renderer.checkCollision(player, bb));
            if (renderer.checkCollision(player, bb)) {
                particleSystem2.generateParticles(player.getPosition());
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                player.increasePosition(0,0,5);
            }
//                new Bullet(cubeModel, new Vector3f(player.getPosition()), new Vector3f(0, 20, -500), 5);
//            }

            particleSystem1.generateParticles(new Vector3f(150, 0, -70));
//            particleSystem2.generateParticles(player.getPosition());
            ParticleMaster.update(camera);
//            for(Terrain terrain: terrains){
//                renderer.processTerrain(terrain);
//                if (player.getPosition().x < terrain.getX() + terrain.getSize() && player.getPosition().x >= terrain.getX()
//                        && player.getPosition().z >= terrain.getZ() && player.getPosition().z < terrain.getZ() + terrain.getSize()) {
//
//                }
//            }
            //For Dash
//            if(player.getPosition().z > cube.getPosition().z
//                    &&player.getPosition().z > cube.getPosition().z
//                    && player.getPosition().x > cube.getPosition().x) {
//                player.getPosition().y += 1;
//            }

//            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//              if(terrainPoint != null) {
//                lamp3.setPosition(terrainPoint);
//                light2.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y+7, terrainPoint.z));
//            }

//            Iterator<Entity> iterator = normalMapEntities.iterator();
//            while (iterator.hasNext()) {
//                Entity entity = iterator.next();
//                renderer.processNormalMapEntity(entity);
////                entity.increaseRotation(1,0,0);
//                if (renderer.checkCollision(player, entity)) {
//                    particleSystem1.generateParticles(player.getPosition());
//                    iterator.remove();
//                }
//            }
            Iterator<Entity> iterator1 = entities.iterator();
            while (iterator1.hasNext()) {
                Entity entity = iterator1.next();
                renderer.processEntity(entity);
//                entity.increaseRotation(1,0,0);
                if (renderer.checkCollision(player, entity)) {
                    particleSystem1.generateParticles(player.getPosition());
                    iterator1.remove();
                }
            }
            for (Terrain terrain : terrains) {
                renderer.processTerrain(terrain);
            }
//            for (Entity entity : entities) {
//                renderer.processEntity(entity);
//
//            }
//            for(Entity entity : normalMapEntities){
//                renderer.processNormalMapEntity(entity);
//                entity.increaseRotation(1,0,0);
////                entity.increasePosition(0,-0.005f,1f);
//            }
            renderer.render(lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1));

//            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
//                    new Vector4f(0, 1, 0, -water.getHeight()+1));

//            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//              if(terrainPoint != null) {
////                lamp3.setPosition(terrainPoint);
//                rock.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y+7, terrainPoint.z));
//            }
            ParticleMaster.renderParticles(camera);

            guiRenderer.render(guis);
            TextMaster.render();

            DisplayManager.updateDisplay();
        }

        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}
