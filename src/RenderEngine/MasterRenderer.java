package RenderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import normalMappingRenderer.NormalMappingRenderer;
import particles.Particle;
import shaders.StaticShader;
import shaders.TerrainShader;
import SkyBox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {

    private static final float FOV = 80;
    private static final float NEAR_PLANE = 1.0f;
    private static final float FAR_PLANE = 50000;

    public static final float RED = 0.1f;
    public static final float GREEN = 0.4f;
    public static final float BLUE = 0.2f;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private NormalMappingRenderer normalMapRenderer;

    private SkyboxRenderer skyboxRenderer;

    private static Map<TextureModel, List<Entity>> entities = new HashMap<TextureModel, List<Entity>>();
    private Map<TextureModel, List<Entity>> normalMapEntities = new HashMap<TextureModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights,
                            Camera camera, Vector4f clipPlane) {
        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }
        for (Entity entity : entities) {
            processEntity(entity);

        }
        for(Entity entity : normalEntities){
            processNormalMapEntity(entity);
            entity.increaseRotation(0,1,0);
//            entity.increasePosition(0,0,1f);
        }
        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        prepare();
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(RED, GREEN, BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera, RED, GREEN, BLUE);
        terrains.clear();
        entities.clear();
        normalMapEntities.clear();
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TextureModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public boolean checkCollision(Entity player, Entity entity) {
        float playerX = player.getPosition().x;
        float playerY = player.getPosition().y;
        float playerZ = player.getPosition().z;
        float playerScale = player.getScale();

        float entityX = entity.getPosition().x;
        float entityY = entity.getPosition().y;
        float entityZ = entity.getPosition().z;
        float entityScale = entity.getScale();

        return playerX - playerScale < entityX + entityScale
                && playerX + playerScale > entityX - entityScale
                && playerY - playerScale < entityY + entityScale
                && playerY + playerScale > entityY - entityScale
                && playerZ - playerScale < entityZ + entityScale
                && playerZ + playerScale > entityZ - entityScale;
    }

    public boolean checkCollision(Entity player, Particle particle) {
        float playerX = player.getPosition().x;
        float playerY = player.getPosition().y;
        float playerZ = player.getPosition().z;
        float playerScale = player.getScale();

        float entityX = particle.getPosition().x;
        float entityY = particle.getPosition().y;
        float entityZ = particle.getPosition().z;
        float entityScale = particle.getScale();

        return playerX - playerScale < entityX + entityScale
                && playerX + playerScale > entityX - entityScale
                && playerY - playerScale < entityY + entityScale
                && playerY + playerScale > entityY - entityScale
                && playerZ - playerScale < entityZ + entityScale
                && playerZ + playerScale > entityZ - entityScale;
    }

    public void processNormalMapEntity(Entity entity) {
        TextureModel entityModel = entity.getModel();
        List<Entity> batch = normalMapEntities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            normalMapEntities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
        normalMapRenderer.cleanUp();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

}
