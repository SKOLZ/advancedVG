package com.test.gameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.OCamera;

public class OCameraScene extends ApplicationAdapter {
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
	GameCamera camera;
	Texture texture;

	@Override
	public void create () {
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
		shaderProgram = new ShaderProgram(vs, fs);
		if (!shaderProgram.isCompiled()) {
			System.out.println(shaderProgram.getLog());
		}
		ModelLoader<?> loader = new ObjLoader();
		ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
		texture = new Texture("ship.png");
		spaceshipMesh = new Mesh(true,
	            data.meshes.get(0).vertices.length,
	            data.meshes.get(0).parts[0].indices.length,
	            VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
		spaceshipMesh.setVertices(data.meshes.get(0).vertices);
		spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
		camera = new MoveableOCamera();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		texture.bind();
		shaderProgram.setUniformi("u_texture", 0);
		camera.update();
		shaderProgram.begin();
		shaderProgram.setUniformMatrix("u_worldView", camera.getProjection());
		spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
		shaderProgram.end();
	}
}
