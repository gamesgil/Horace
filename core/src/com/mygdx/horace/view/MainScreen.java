package com.mygdx.horace.view;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.mygdx.horace.control.IController;
import com.mygdx.horace.model.Status;
import com.mygdx.horace.view.effects.Blinker;

public class MainScreen implements Screen, IView, InputProcessor {
	private final int TILE_SIZE = 64;
	private final float GAME_WORLD_WIDTH = TILE_SIZE * 9;
	private final float GAME_WORLD_HEIGHT = TILE_SIZE * 6;
	
	private IController controller;
	private Renderer renderer;
	private OrthographicCamera camera;
	private Blinker blinker;
	
	public MainScreen(SpriteBatch batch) {
		renderer = new Renderer(batch);
	}
	
	@Override
	public void show() {
		Gdx.app.error("", "show");
		float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		camera = new OrthographicCamera(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
		camera.position.set(GAME_WORLD_WIDTH / 2, GAME_WORLD_HEIGHT / 2, 0);
		
		GameFont font = new GameFont("fonts/c64.fnt");
		renderer.add("ttlGame", font);
		font.setText("GIL GOES SKIING");
		font.x = (int)(GAME_WORLD_WIDTH - font.getBounds().width) / 2;
		font.y = (int)(GAME_WORLD_HEIGHT + font.getBounds().height) / 2;
		
		font = new GameFont("fonts/c64.fnt");
		renderer.add("ttlCopyright", font);
		font.setText("COPYRIGHT 2015");
		font.setScale(0.5f, 0.5f);
		font.x = (int)(GAME_WORLD_WIDTH - font.getBounds().width) / 2;
		font.y = renderer.getTextField("ttlGame").y - (int)renderer.getTextField("ttlGame").getBounds().height * 2;
		
		font = new GameFont("fonts/c64.fnt");
		renderer.add("ttlClick", font);
		font.setText("click anywhere to start");
		font.setScale(0.5f, 0.5f);
		font.x = 0;
		font.y = (int)font.getBounds().height;
		
		blinker = new Blinker(font);
		blinker.init(5, 1);
		blinker.start();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		if (camera != null) {
			Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			if (blinker != null) {
				blinker.update();			
			}
			
			if (renderer != null) {
				renderer.render(camera);
			}
		}
		
		if (Gdx.input.isTouched()) {
			//Gdx.input.setInputProcessor(null);
			//controller.updateModel(Status.START_GAME);
		}
	}
	
	@Override
	public void update() {
		
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		System.out.println("touchDown: " + button);

		controller.updateModel(Status.START_GAME);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		System.out.println(button);
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setController(IController controller) {
		this.controller = controller;
	}

	@Override
	public void updateStatus(String status, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		blinker.destroy();
		renderer.destroy();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
