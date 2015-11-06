package com.test.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;

import java.util.LinkedList;
import java.util.List;

public class MoveablePCamera extends PCamera {
    public float moveSpeed = 0.04f;
    public float rotSpeed = 0.02f;
    public int i = 0;
    public int MAX = 100;
    public List<ModelObject> models = new LinkedList<ModelObject>();

    public void addModel(ModelObject model) {
        models.add(model);
    }

    public void update() {
        i++;
        if(i == MAX) {
            i = 0;
            System.out.println("Rotation: " + rotation);
            System.out.println("Position: " + position);
            System.out.println("Models: ");
            for(ModelObject model: models) {
                System.out.println("Rotation: " + model.getRotation());
                System.out.println("Position: " + model.getPosition());
            }
        }
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
        Vector3 forward = new Vector3(0, 0, 1);
        forward.rotateRad(new Vector3(1, 0, 0), rotation.x);
        forward.rotateRad(new Vector3(0, 1, 0), rotation.y);
        return forward;
    }

    public Vector3 getLeft() {
        return getForward().crs(UP_VECTOR);
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

