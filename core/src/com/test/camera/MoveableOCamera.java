package com.test.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by naki on 09/09/15.
 */
public class MoveableOCamera extends OCamera {

    public float moveSpeed = 0.04f;
    public float rotSpeed = 0.02f;

    public void update() {
        checkForwardOrBack();
        checkLeftOrRight();
        checkRotation();
    }

    public void checkForwardOrBack() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveForward();
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            moveBackward();
        }
    }

    public void checkLeftOrRight() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            moveRight();
        }
    }

    public void moveForward() {
        position.add(getForward().scl(moveSpeed));
    }

    public void moveBackward() {
        position.add(getForward().scl(-moveSpeed));
    }

    public void moveLeft() {
        position.add(getLeft().scl(moveSpeed));
    }

    public void moveRight() {
        position.add(getLeft().scl(-moveSpeed));
    }

    public Vector3 getForward() {
        Vector3 forward = new Vector3(0, 0, -1);
        forward.rotate(new Vector3(1, 0, 0), rotation.x);
        forward.rotate(new Vector3(0, 1, 0), rotation.y);
        return forward;
    }

    public Vector3 getLeft() {
        Vector3 left = new Vector3(1, 0, 0).rotateRad(new Vector3(1, 0, 0), rotation.x);
        left.rotate(new Vector3(0, 1, 0), rotation.y);
        left.rotate(new Vector3(0, 0, 1), rotation.z);
        return left;
    }

    private void checkRotation() {
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            return;
        int deltaX = Gdx.input.getDeltaX();
        int deltaY = Gdx.input.getDeltaY();

        rotation.x += ((float)deltaY) * -rotSpeed;
        if(rotation.x < -0.5f * (float)Math.PI) {
            rotation.x = -0.5f * (float)Math.PI;
        }
        if(rotation.x > 0.5f * (float)Math.PI) {
            rotation.x = 0.5f * (float)Math.PI;
        }
        rotation.y += ((float)deltaX) * -rotSpeed;
        if(rotation.y < -0.5f * (float)Math.PI) {
            rotation.y = -0.5f * (float)Math.PI;
        }
        if(rotation.y > 0.5f * (float)Math.PI) {
            rotation.y = 0.5f * (float)Math.PI;
        }
    }
}
