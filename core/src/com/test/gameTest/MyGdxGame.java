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

public class MyGdxGame extends ApplicationAdapter {
	Texture img;
	Mesh spaceshipMesh;
	ShaderProgram shaderProgram;
	
	@Override
	public void create () {
		img = new Texture("badlogic.jpg");
		String vs = Gdx.files.internal("defaultVS.glsl").readString();
		String fs = Gdx.files.internal("defaultFS.glsl").readString();
		shaderProgram = new ShaderProgram(vs, fs);
		System.out.println(shaderProgram.getLog());
		ModelLoader<?> loader = new ObjLoader();
		ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
		
		spaceshipMesh = new Mesh(true,
	            data.meshes.get(0).vertices.length,
	            data.meshes.get(0).parts[0].indices.length,
	            VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.Normal());
	      spaceshipMesh.setVertices(data.meshes.get(0).vertices);
	      spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	      img.bind();
	      shaderProgram.begin();
	      shaderProgram.setUniformMatrix("u_worldView", new Matrix4()); //aca trabajar
	      shaderProgram.setUniformi("u_texture", 0);
	      spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLE_FAN);
	      shaderProgram.end();
	}
}
