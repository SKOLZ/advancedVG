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
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.test.camera.GameCamera;
import com.test.camera.MoveableOCamera;
import com.test.camera.OCamera;

public class PCameraScene extends BaseScene {

    @Override
    public void create () {
        super.create();
        shaderProgram = loadShader("defaultVS.glsl", "defaultFS.glsl");
    }

    @Override
    public void render () {
        super.render();
        texture.bind();
        shaderProgram.begin();
        shaderProgram.setUniformMatrix("u_mvp", camera.getProjection());
        spaceshipMesh.render(shaderProgram, GL20.GL_TRIANGLES);
        shaderProgram.end();
    }
}
