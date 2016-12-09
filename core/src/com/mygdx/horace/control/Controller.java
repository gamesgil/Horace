package com.mygdx.horace.control;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.horace.model.GameManager;
import com.mygdx.horace.model.IGame;
import com.mygdx.horace.model.Screens;
import com.mygdx.horace.model.Status;
import com.mygdx.horace.view.GameScreen;
import com.mygdx.horace.view.IView;
import com.mygdx.horace.view.MainScreen;

public class Controller implements IController {
	private IGame game;
	private IView view;
	private Game screenManager;
	private SpriteBatch batch;
	
	public Controller(IGame game, Game screenManager)
	{
		this.game = game;
		this.screenManager = screenManager;
		
		batch = new SpriteBatch();
		
		setScreen(Screens.MAIN);
		
		this.game.setController(this);
		this.view.setController(this);
		
		this.game.init();
	}
	
	@Override
	public void updateModel(String status) {
		game.updateStatus(status);
		
		switch (status) {
		case Status.START_GAME:
			setScreen(Screens.GAME);
			break;
			
		case Status.GAME_OVER:
			setScreen(Screens.MAIN);
			break;
		}
	}
	
	private void setScreen(String screen) {
		if (view != null) {
			((Screen)view).hide();
			//((Screen)view).dispose();
			view.destroy();
			
			view = null;
		}
		
		batch.dispose();
		batch = new SpriteBatch();
		
		switch (screen) {
		case Screens.MAIN:
			view = new MainScreen(batch);
			break;
			
		case Screens.GAME:
			view = new GameScreen(batch, ((GameManager)game).getLevel(0).getLayout());
			break;
		}
		
		if (view != null) {
			view.setController(this);
		}
		
		screenManager.setScreen((Screen)view);
	}

	@Override
	public void updateView(String status, int value) {
		view.updateStatus(status, value);
	}

	@Override
	public void updateView(String status, String value) {
		//view.updateStatus(status, value);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		view.destroy();
	}

	public void update(float deltaTime) {
		if (view != null) {
			((Screen)view).render(deltaTime);
		}
	}

}
