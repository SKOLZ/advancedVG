package com.test.gameTest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.test.base.ModelObject;
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.MoveablePCamera;
import com.test.light.DirectionalLight;

/**
 * Created by naki on 27/09/15.
 */
public class BaseScene extends ApplicationAdapter {
    Mesh spaceshipMesh;
    ShaderProgram shaderProgram;
    GameCamera camera;
    Texture texture;
    ModelObject model;
    ModelLoader<?> loader;

    @Override
    public void create() {
        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl20.glDepthFunc(GL20.GL_LESS);

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_ONE, GL20.GL_ZERO);
    	
        loader = new ObjLoader();
        ModelData data = loader.loadModelData(Gdx.files.internal("ship.obj"));
        texture = new Texture("ship.png");
        spaceshipMesh = new Mesh(true,
                data.meshes.get(0).vertices.length,
                data.meshes.get(0).parts[0].indices.length,
                VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        spaceshipMesh.setVertices(data.meshes.get(0).vertices);
        spaceshipMesh.setIndices(data.meshes.get(0).parts[0].indices);
        model = new ModelObject(spaceshipMesh, 0.6f, new Vector3(0.5f, 0.5f, 0), new Vector3(0, 0, 0));
        camera = new MoveablePCamera();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
    }

    public ShaderProgram loadShader(String vshader, String fshader) {
        String vs = Gdx.files.internal(vshader).readString();
        String fs = Gdx.files.internal(fshader).readString();
        ShaderProgram sp = new ShaderProgram(vs, fs);
        if (!sp.isCompiled()) {
            System.out.println(sp.getLog());
        }
        return sp;
    }
}
