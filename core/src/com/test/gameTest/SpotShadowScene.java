package com.test.gameTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.MoveablePCamera;
import com.test.camera.OCamera;
import com.test.camera.PCamera;
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
    FrameBuffer spotFb;
    ShaderProgram depthsp;
    ShaderProgram fullscreenProgram;
    final Matrix4 biasMat = new Matrix4(new float[]{
            0.5f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f
    });
    Mesh mesh;
    ModelObject meshObj;

    @Override
    public void create() {
        super.create();
        fullscreenProgram = loadShader("fullscreenVS.glsl", "fullscreenFS.glsl");
        shaderProgram = loadShader("defaultShadowVS.glsl", "spot-shadow-FS.glsl");
        depthsp = loadShader("defaultVS.glsl", "depth-FS.glsl");
        model.setPosition(new Vector3(0, 2, 0));
        spotlight = new Spotlight(new Vector3(0, 5, 0), new Vector3((float)-Math.PI/2, 0, 0), new Vector3(0.3f, 1.0f, 0.3f), 1, (float)Math.PI / 10, PCamera.DEFAULT_NEAR, PCamera.DEFAULT_FAR);
        spotFb = new FrameBuffer(Pixmap.Format.RGBA8888, 4096, 4096, true);

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
        mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.TexCoords(0));
        float vertexData[] = {
                1.0f,  1.0f, 0.0f, 1.0f, 1.0f, // vertex 0
                -1.0f,  1.0f, 0.0f, 0.0f, 1.0f, // vertex 1
                1.0f, -1.0f, 0.0f, 1.0f, 0.0f, // vertex 2
                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, // vertex 3
        };

        mesh.setVertices(vertexData);
        mesh.setIndices(new short[]{0, 1, 2, 2, 1, 3});
        meshObj = new ModelObject(mesh, 1, new Vector3(), new Vector3());
    }

    @Override
    public void render() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        super.render();

        spotFb.begin();

        renderDepthShader(spotlight);

        spotFb.end();

/*
        Texture tt = spotFb.getColorBufferTexture();
        fullscreenProgram.begin();
        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE0);
        tt.bind();
        fullscreenProgram.setUniformi("u_texture", 0);
        mesh.render(fullscreenProgram, GL20.GL_TRIANGLES);
        fullscreenProgram.end();
*/


        shaderProgram.begin();

        Texture sm = spotFb.getColorBufferTexture();
        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE2);
        sm.bind();
        shaderProgram.setUniformi("u_shadow_map", 2);

        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE0);
        texture.bind();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(model.getTRS()));
        shaderProgram.setUniform4fv("u_light_pos", spotlight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", spotlight.getColorAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_dir", toDirection(spotlight.getRotation()), 0, 4);
        shaderProgram.setUniformf("u_light_in", spotlight.getIntensity());
        shaderProgram.setUniformf("u_light_max_angle_cos", (float) Math.cos((double) spotlight.getMaxAngle()));
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        shaderProgram.setUniformMatrix("u_light_bias_mvp", spotlight.getProjection().mul(model.getTRS()));
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);

        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE0);
        floorTxt.bind();
        shaderProgram.setUniformMatrix("u_model", floor.getTRS());
        shaderProgram.setUniformMatrix("u_rot", floor.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(floor.getTRS()));
        shaderProgram.setUniform4fv("u_light_pos", spotlight.getPositionAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", spotlight.getColorAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_dir",toDirection(spotlight.getRotation()), 0, 4);
        shaderProgram.setUniformf("u_light_in", spotlight.getIntensity());
        shaderProgram.setUniformf("u_light_max_angle_cos", (float)Math.cos((double)spotlight.getMaxAngle()));
        shaderProgram.setUniformf("u_shininess", floor.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        shaderProgram.setUniformMatrix("u_light_bias_mvp", spotlight.getProjection().mul(floor.getTRS()));
        floor.getMesh().render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();

    }

    private void renderDepthShader(Spotlight sl) {
        depthsp.begin();
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();
        depthsp.setUniformMatrix("u_mvp", sl.getProjection().mul(model.getTRS()));
        model.getMesh().render(depthsp, GL20.GL_TRIANGLES);



        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        floorTxt.bind();
        depthsp.setUniformMatrix("u_mvp", sl.getProjection().mul(floor.getTRS()));
        floor.getMesh().render(depthsp, GL20.GL_TRIANGLES);



        depthsp.end();

    }
}
