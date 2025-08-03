package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float  distanceFromPlayer = 50;
    private float angleAroundPlayer = 180;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 10;   //camera high/low from player
    private float yaw = 0;
    private float roll;

    private static final int MIN_PITCH = 1;
    private static final int MAX_PITCH = 90;
    private static final int MIN_ZOOM = 25;
    private static final int MAX_ZOOM = 75;

    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        calculatePitch();
        calculateZoom();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
    }

    public Vector3f getPosition() {
        return position;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance;
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public void invertPitch(){
        this.pitch = -pitch;
    }
    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
//        if (distanceFromPlayer < MIN_ZOOM) distanceFromPlayer = MIN_ZOOM;
//        else if (distanceFromPlayer > MAX_ZOOM) distanceFromPlayer = MAX_ZOOM;
    }

    private void calculatePitch() {
        if(Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
            if (pitch < MIN_PITCH) pitch = MIN_PITCH;
            else if (pitch > MAX_PITCH) pitch = MAX_PITCH;
        }
    }

    private void calculateAngleAroundPlayer() {
        if(Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

}
