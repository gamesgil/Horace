package com.mygdx.horace;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.horace.control.Controller;
import com.mygdx.horace.model.GameManager;
import com.mygdx.horace.view.GameAssets;
import com.mygdx.horace.view.GameScreen;

public class Horace extends Game {
	private GameManager gm;
	private GameScreen game;
	private Controller controller;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		GameAssets.load("horace_c", "game/horace_c.png");
		GameAssets.load("horace_l", "game/horace_l.png");
		GameAssets.load("horace_ll", "game/horace_ll.png");
		GameAssets.load("horace_r", "game/horace_r.png");
		GameAssets.load("horace_rr", "game/horace_rr.png");
		GameAssets.load("tree", "game/tree.png");
		GameAssets.load("hurdle", "game/hurdle.png");
		GameAssets.load("flag_l", "game/flag_l.png");
		GameAssets.load("flag_r", "game/flag_r.png");
		GameAssets.load("boom", "game/boom.png");
		GameAssets.load("flag_trigger", "game/flag_trigger.png");
		GameAssets.load("finish", "game/finish.png");
		GameAssets.load("end_trigger", "game/flag_trigger.png");
		GameAssets.load("sndBgMusic", "game/endless orgy.mp3");
		GameAssets.load("sndHit", "game/flyswatter2.ogg");
		GameAssets.load("sndBonus", "game/Rise07.ogg");
		GameAssets.load("sndWin", "game/Upper01.ogg");

		gm = new GameManager();
		
		controller = new Controller(gm, this);
	}

	private int counter = 0;
	private float accum = 0;
	
	@Override
	public void render () {
		controller.update(Gdx.graphics.getDeltaTime());
		counter++;
		
		accum += Gdx.graphics.getDeltaTime();
		
		if (accum > 1) {
			accum = 0;
			counter = 0;
		}
	}
	
}
