package com.test.gameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.MoveablePCamera;
import com.test.light.PointLight;

public class PointLightScene extends BaseScene {
    PointLight pointLight;

    @Override
    public void create () {
        super.create();
        loadShader("defaultVS.glsl", "point-phong-FS.glsl");
        pointLight = new PointLight(new Vector3(0, 3, 0), new Vector3(0.3f, 1.0f, 0.3f), 1);
    }

    @Override
    public void render () {
        super.render();
        texture.bind();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", model.getTRS().mul(camera.getProjection()));
        shaderProgram.setUniform4fv("u_light_pos", pointLight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", pointLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", pointLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
