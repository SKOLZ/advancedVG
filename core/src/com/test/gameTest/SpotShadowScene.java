package com.test.gameTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.MoveablePCamera;
import com.test.light.DirectionalLight;
import com.test.light.Spotlight;

/**
 * Created by naki on 06/11/15.
 */
public class SpotShadowScene extends BaseScene{
    Spotlight spotlight;
    Mesh floorMesh;
    ModelObject floor;
    Texture floorTxt;

    @Override
    public void create() {
        super.create();
        shaderProgram = loadShader("defaultVS.glsl", "spot-phong-FS.glsl");
        spotlight = new Spotlight(new Vector3(0, 3, 0), new Vector3(0, (float)Math.PI/4 + 0.3f, 0), new Vector3(0.3f, 1.0f, 0.3f), 1, (float)Math.PI / 4);
        ModelLoader<?> cubeLoader = new ObjLoader();
        ModelData floorData = cubeLoader.loadModelData(Gdx.files.internal("box.obj"));
        floorTxt = new Texture("texture.png");
        floorMesh = new Mesh(true,
                floorData.meshes.get(0).vertices.length,
                floorData.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        floorMesh.setVertices(floorData.meshes.get(0).vertices);
        floorMesh.setIndices(floorData.meshes.get(0).parts[0].indices);
        floor = new ModelObject(floorMesh, 0.2f, new Vector3(-150, -2, -150), new Vector3(0, 0, 0), new Vector3(3000f, 0.1f, 3000f));
        ((MoveablePCamera)camera).addModel(model);
    }

    @Override
    public void render() {
        super.render();

        shaderProgram.begin();
        texture.bind();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(model.getTRS()));
        shaderProgram.setUniform4fv("u_light_pos", spotlight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", spotlight.getColorAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_dir", spotlight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", spotlight.getIntensity());
        shaderProgram.setUniformf("u_light_max_angle_cos", (float)Math.cos((double)spotlight.getMaxAngle()));
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);

        floorTxt.bind();
        shaderProgram.setUniformMatrix("u_model", floor.getTRS());
        shaderProgram.setUniformMatrix("u_rot", floor.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(floor.getTRS()));
        shaderProgram.setUniform4fv("u_light_pos", spotlight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", spotlight.getColorAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_dir", spotlight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", spotlight.getIntensity());
        shaderProgram.setUniformf("u_light_max_angle_cos", (float)Math.cos((double)spotlight.getMaxAngle()));
        shaderProgram.setUniformf("u_shininess", floor.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        floor.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
