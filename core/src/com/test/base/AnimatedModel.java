package com.test.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AnimatedModel extends ModelObject {
	
	private ModelInstance model;
	private Texture texture;
	private AnimationController controller;
	private float[] bones;
	
	public AnimatedModel(ModelInstance model, Texture texture) {
		super(null, 3, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
		this.model = model;
		this.texture = texture;
		this.controller = new AnimationController(model);
		
		controller.animate(model.animations.get(0).id, -1, 1f, null, 0.2f);
	}
	
	public void setBones(float[] bones) {
		this.bones = bones;
	}
	
	public Array<Renderable> getRenderables() {
		Array<Renderable> renderables = new Array<Renderable>();
		Pool<Renderable> pool = new Pool<Renderable>() {
			@Override
			protected Renderable newObject() {
				return new Renderable();
			}

			@Override
			public Renderable obtain() {
				Renderable renderable = super.obtain();
				renderable.material = null;
				renderable.mesh = null;
				renderable.shader = null;
				return renderable;
			}
		};
		model.getRenderables(renderables, pool);
		return renderables;
	}
	
	@Override
	public Matrix4 getTRS() {
		return	model.transform;
	}
	
	public ModelInstance GetModel() {
		return model;
	}
	
	public AnimationController getAnimationController() {
		return controller;
	}

	public Texture getTexture() {
		return texture;
	}
}
