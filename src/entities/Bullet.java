package entities;

import RenderEngine.DisplayManager;
import RenderEngine.EntityRenderer;
import RenderEngine.MasterRenderer;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;

public class Bullet extends Entity{

    private Vector3f position;
    private Vector3f direction;
    private float speed;

    public Bullet(TextureModel model, Vector3f position, Vector3f direction, float speed) {
        super(model, position, 0,0,0, 1);
        this.position = position;
        this.direction = direction;
        this.speed = speed;
//        MasterRenderer.processEntity(this);
        super.increasePosition(0, 0, DisplayManager.getFrameTimeSeconds() * speed);
    }

    public Bullet getBullet() {
        return this;
    }

    public void update() {
        float deltaTime = DisplayManager.getFrameTimeSeconds();
        Vector3f deltaPosition = new Vector3f(direction);
        deltaPosition.scale(speed * deltaTime);
        super.increasePosition(0, 0, deltaPosition.z);
    }


}
