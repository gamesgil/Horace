package com.mygdx.horace.model;

import com.mygdx.horace.control.IController;

public class GameManager implements IGame {
	private IController controller;
	private int currentLevel;
	private Level[] levels;
	private int score;
	private int lives;
	
	public GameManager()
	{
		setupLevels();
	}

	private void setupLevels() {
		levels = new Level[1];
		
		for (int i = 0; i < levels.length; i++) {
			levels[i] = new Level(i);
			levels[i].setSpeed(i + 1);
		}
	}
	
	public void init()
	{
		score = 0;
		lives = 1;
		
		controller.updateView(Status.LIVES, lives);
		controller.updateView(Status.SCORE, score);
	}
	
	public Level getLevel(int id)
	{
		return levels[id];
	}

	@Override
	public int getLives() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateStatus(String status) {
		switch (status)
		{
		case Status.HIT:
			lives--;
			
			controller.updateView(Status.LIVES, lives);
			
			if (lives <= 0) {
				controller.updateView(Status.GAME_OVER, 0);
			}
			break;
			
		case Status.FLAG:
			score++;
			
			controller.updateView(Status.SCORE, score);
			break;
			
		case Status.CROSSED_THE_LINE:
			score += 10;
			
			controller.updateView(Status.SCORE, score);
			break;
		}
	}

	@Override
	public void setController(IController controller) {
		this.controller = controller;
	}
}
