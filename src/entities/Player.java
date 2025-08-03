package entities;

import RenderEngine.DisplayManager;
import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;


public class Player extends Entity{

    private static final float RUN_SPEED = 40;  //unit/s
    private static final float TURN_SPEED = 50;    //degree/s

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean IsinAir = false;

    public static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;

    private static final float Terrain_Height = 0;
    public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move() {
        checkInputs();
//        super.increaseRotation(0, 0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds());
        super.increasePosition(currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0, 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0 ,dz);

        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        if(super.getPosition().y < Terrain_Height) {
            upwardsSpeed = 0;
            IsinAir = false;
            super.getPosition().y = Terrain_Height;
        }

    }

    private void jump() {
        if(!IsinAir) {
            this.upwardsSpeed = JUMP_POWER;
            IsinAir = true;
        }

    }

    public void checkInputs() {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.currentSpeed = -RUN_SPEED;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.currentSpeed = RUN_SPEED;
        }
        else {
            this.currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();
        }
    }
}
