package particles;


import RenderEngine.DisplayManager;
import entities.Camera;
import entities.Player;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Particle {
    public Vector3f getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    private Vector3f position;

    protected boolean update(Camera camera) {
        velocity.x += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        resuableChange.set(velocity);
        resuableChange.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(resuableChange, position, position);
        updateTextureCoordsInfo();
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
        elapsedTime += DisplayManager.getFrameTimeSeconds();
        return elapsedTime < lifeLength;
    }

    protected ParticleTexture getTexture() {
        return texture;
    }

    public Vector2f getTexOffset1() {
        return texOffset1;
    }

    public Vector2f getTexOffset2() {
        return texOffset2;
    }

    public float getBlend() {
        return blend;
    }

    private void updateTextureCoordsInfo() {
        float lifeFactor = elapsedTime / lifeLength;
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgression % 1;
        setTextureOffset(texOffset1, index1);
        setTextureOffset(texOffset2, index2);

    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float) column / texture.getNumberOfRows();
        offset.y = (float) row / texture.getNumberOfRows();
    }

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect,
                    float lifeLength, float scale, float rotation) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.scale = scale;
        this.rotation = rotation;
        ParticleMaster.addParticle(this);
    }

    private ParticleTexture texture;
    private Vector2f texOffset1 = new Vector2f();
    private Vector2f texOffset2 = new Vector2f();
    private float blend;

    public float getDistance() {
        return distance;
    }

    private Vector3f resuableChange = new Vector3f();
    private float distance;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float scale;
    private float rotation;

    private float elapsedTime = 0;

}
