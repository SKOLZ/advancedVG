package com.test.gameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.test.base.ModelObject;
import com.test.camera.GameCamera;
import com.test.camera.MoveablePCamera;
import com.test.light.DirectionalLight;
import com.test.light.PointLight;

public class DirectionalShadowScene extends BaseScene {
    DirectionalLight directionalLight;
    Mesh floorMesh;
    ModelObject floor;
    Texture floorTxt;

    @Override
    public void create() {
        super.create();
        loadShader("defaultVS.glsl", "directional-phong-FS.glsl");
        directionalLight = new DirectionalLight(new Vector3(0, 3, 0), new Vector3(0.3f, 1.0f, 0.3f), 1);
        ModelLoader<?> cubeLoader = new ObjLoader();
        ModelData floorData = cubeLoader.loadModelData(Gdx.files.internal("box.obj"));
        floorTxt = new Texture("texture.png");
        floorMesh = new Mesh(true,
                floorData.meshes.get(0).vertices.length,
                floorData.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        floorMesh.setVertices(floorData.meshes.get(0).vertices);
        floorMesh.setIndices(floorData.meshes.get(0).parts[0].indices);
        floor = new ModelObject(floorMesh, 0.6f, new Vector3(-150, -2, -150), new Vector3(0, 0, 0), new Vector3(3000f, 0.1f, 3000f));
        ((MoveablePCamera)camera).addModel(model);
    }

    @Override
    public void render() {
        super.render();

        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(model.getTRS()));
        shaderProgram.setUniform4fv("u_light_dir", directionalLight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", directionalLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", directionalLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);

        floorTxt.bind();
        shaderProgram.setUniformMatrix("u_model", floor.getTRS());
        shaderProgram.setUniformMatrix("u_rot", floor.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(floor.getTRS()));
        shaderProgram.setUniform4fv("u_light_dir", directionalLight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", directionalLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", directionalLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", floor.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        floor.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
