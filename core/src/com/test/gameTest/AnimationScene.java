package com.test.gameTest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.test.base.AnimatedModel;
import com.test.light.DirectionalLight;

public class AnimationScene extends BaseScene {
	
	private AnimatedModel animModel;
	private DirectionalLight directionalLight;
	private AssetManager assetManager =  new AssetManager();
	private boolean loading;
	
	private ShaderProgram animProgram;

	@Override
    public void create() {
        super.create();
        loadShader("defaultVS.glsl", "directional-phong-FS.glsl");
        directionalLight = new DirectionalLight(new Vector3(0, 3, 0), new Vector3(0.3f, 1.0f, 0.3f), 1);
        assetManager.load("Dave.g3db", Model.class);
        loading = true;
        
        String vs = Gdx.files.internal("animVS.glsl").readString();
        String fs = Gdx.files.internal("directional-phong-FS.glsl").readString();
        animProgram = new ShaderProgram(vs, fs);
        if (!animProgram.isCompiled()) {
            System.out.println(animProgram.getLog());
        }
	}

    @Override
    public void render() {
    	super.render();
    	if (loading && assetManager.update()) {
    		loading = false;
    		Model model = assetManager.get("Dave.g3db", Model.class);
    		Texture tex = new Texture(Gdx.files.internal("uv_dave_mapeo.jpg"));
    		
    		ModelInstance instance = new ModelInstance(model);

			animModel = new AnimatedModel(instance, tex);
			animModel.GetModel().transform.translate(0, 0, 0);
			animModel.GetModel().transform.scale(0.2f, 0.2f, 0.2f);
			animModel.setShininess(5);
    	}
    	if(animModel == null) return;
    	animModel.getAnimationController().update(Gdx.graphics.getDeltaTime());
        
    	shaderProgram.begin();
    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        texture.bind();
    	shaderProgram.setUniformMatrix("u_model", model.getTRS());
        shaderProgram.setUniformMatrix("u_rot", model.getRotationMatrix());
        shaderProgram.setUniformMatrix("u_mvp", model.getTRS().mul(camera.getProjection()));
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniform4fv("u_light_dir", directionalLight.getRotationAsV4(), 0, 4);
        shaderProgram.setUniform4fv("u_light_c", directionalLight.getColorAsV4(), 0, 4);
        shaderProgram.setUniformf("u_light_in", directionalLight.getIntensity());
        shaderProgram.setUniformf("u_shininess", model.getShininess());
        shaderProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        
        shaderProgram.end();
        
        animProgram.begin();
        
    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        animModel.getTexture().bind();
        animProgram.setUniform4fv("u_camera_pos", camera.getPositionAsV4(), 0, 4);
        animProgram.setUniformMatrix("u_model", animModel.getTRS());
//            shaderProgram.setUniformMatrix("u_rot", animModel.getRotationMatrix());
        animProgram.setUniformi("u_texture", 0);
        animProgram.setUniformf("u_shininess", animModel.getShininess());

        animProgram.setUniform4fv("u_light_dir", directionalLight.getRotationAsV4(), 0, 4);
        animProgram.setUniform4fv("u_light_c", directionalLight.getColorAsV4(), 0, 4);
        animProgram.setUniformf("u_light_in", directionalLight.getIntensity());
        
        Array<Renderable> renderables = animModel.getRenderables();
        
        Matrix4 idtMatrix = new Matrix4().idt();
        float[] bones = new float[32 * 16];
        for (int i = 0; i < bones.length; i++)
            bones[i] = idtMatrix.val[i % 16];
        for (Renderable render : renderables) {
        	animProgram.setUniformMatrix("u_mvp", camera.getProjection().mul(render.worldTransform));
            for (int i = 0; i < bones.length; i++) {
                final int idx = i / 16;
                bones[i] = (render.bones == null || idx >= render.bones.length || render.bones[idx] == null) ?
                        idtMatrix.val[i % 16] : render.bones[idx].val[i % 16];
            }
            animProgram.setUniformMatrix4fv("u_bones", bones, 0, bones.length);
            render.mesh.render(animProgram, render.primitiveType, render.meshPartOffset, render.meshPartSize);

        }
        
        animProgram.end();
    }	
}
