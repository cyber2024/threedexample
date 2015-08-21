package com.ggg.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;

public class MyGame extends ApplicationAdapter {
	public PerspectiveCamera cam;
	public Model model;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public ModelBatch modelBatch;
	public Environment environment;
	public CameraInputController camController;
	public boolean loading;
	public ModelInstance ship, space;
	public Array<ModelInstance> blocks = new Array<ModelInstance>();
	public Array<ModelInstance> invaders = new Array<ModelInstance>();

	public MyGame() {
		super();
	}

	@Override
	public void create() {
		super.create();
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 7f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();
		assets.load("data/invaderscene5.g3dj", Model.class);

		loading = true;
	}
	private void doneLoading() {
		Model model = assets.get("data/invaderscene5.g3dj", Model.class);
		for (int i = 0; i < model.nodes.size; i++) {
			String id = model.nodes.get(i).id;
			ModelInstance instance = new ModelInstance(model, id);
			Node node = instance.getNode(id);

			instance.transform.set(node.globalTransform);
			node.translation.set(0,0,0);
			node.scale.set(1,1,1);
			node.rotation.idt();
			instance.calculateTransforms();

			if (id.equals("space")) {
				space = instance;
				continue;
			}

			instances.add(instance);
			Gdx.app.log("tag: ", "test");
			if (id.equals("ship")) {
				ship = instance;
				Gdx.app.log("tag: ", "Cube added");
			}
			else if (id.startsWith("block")) {
				blocks.add(instance);
				Gdx.app.log("tag: ", "Cube added");
			}
			else if (id.startsWith("invader"))
				invaders.add(instance);
		}

		loading = false;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		if (loading && assets.update())
			doneLoading();
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		if (space != null)
			modelBatch.render(space);
		modelBatch.end();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		instances.clear();
		blocks.clear();
		invaders.clear();
		assets.dispose();
		modelBatch.dispose();
	}
}
