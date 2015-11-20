package com.test.gameTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.MoveablePCamera;
import com.test.camera.OCamera;
import com.test.light.DirectionalLight;

public class DirectionalShadowScene extends BaseScene {
    DirectionalLight directionalLight;
    Mesh floorMesh;
    ModelObject floor;
    Texture floorTxt;
    FrameBuffer directionalFb;
    ShaderProgram zbuffersp;
    ShaderProgram depthsp;

    @Override
    public void create() {
        super.create();
        shaderProgram = loadShader("defaultShadowVS.glsl", "directional-shadow-FS.glsl");
        zbuffersp = loadShader("defaultVS.glsl", "single-color-FS.glsl");
        depthsp = loadShader("defaultVS.glsl", "depth-FS.glsl");
        directionalLight = new DirectionalLight(new Vector3(0, 3, 0), new Vector3(1, 0, 0), 1, OCamera.DEFAULT_NEAR, OCamera.DEFAULT_FAR);
        
        directionalFb = new FrameBuffer(Pixmap.Format.RGB888, 4096, 4096, true);
        
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

        directionalFb.begin();

        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        renderZShader(directionalLight);
        renderDepthShader(directionalLight);

        directionalFb.end();

        shaderProgram.begin();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();
        shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(model.getTRS()));
        shaderProgram.setUniform4fv("u_light_dir", directionalLight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", directionalLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", directionalLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        
        Texture sm = directionalFb.getColorBufferTexture();
        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE2);
        sm.bind();
        shaderProgram.setUniformi("u_shadow_map", 2);
        
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);

        Gdx.gl20.glActiveTexture(Gdx.gl20.GL_TEXTURE0);
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

    private void renderZShader(DirectionalLight dl) {
    	zbuffersp.begin();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();
        zbuffersp.setUniformMatrix("u_mvp", dl.getViewProjection().mul(model.getTRS()));
        zbuffersp.setUniform4fv("u_ambient_color", new float[] {0, 0, 0, 0}, 0, 4);
        model.getMesh().render(zbuffersp, GL20.GL_TRIANGLES);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        floorTxt.bind();
        zbuffersp.setUniformMatrix("u_mvp", dl.getViewProjection().mul(floor.getTRS()));
        zbuffersp.setUniform4fv("u_ambient_color", new float[] {0, 0, 0, 0}, 0, 4);
        floor.getMesh().render(zbuffersp, GL20.GL_TRIANGLES);

    	zbuffersp.end();
    }

    private void renderDepthShader(DirectionalLight dl) {
    	depthsp.begin();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    	texture.bind();
    	depthsp.setUniformMatrix("u_mvp", dl.getViewProjection().mul(model.getTRS()));
        model.getMesh().render(depthsp, GL20.GL_TRIANGLES);
        
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        floorTxt.bind();
        depthsp.setUniformMatrix("u_mvp", dl.getViewProjection().mul(floor.getTRS()));
        floor.getMesh().render(depthsp, GL20.GL_TRIANGLES);
    	
    	depthsp.end();
    	
    }
}
