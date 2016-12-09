package com.mygdx.horace.view.effects;

import com.badlogic.gdx.Gdx;
import com.mygdx.horace.control.IController;
import com.mygdx.horace.view.GameFont;
import com.mygdx.horace.view.MovieClip;

public class Blinker {
	private IController controller;
	
	private MovieClip clip;
	private int numOfBlinks = 0;
	private float delay = 1;
	private float counter = 0;
	private GameFont font;
	private String callbackStatus;
	
	public Blinker(MovieClip clip) {
		this.clip = clip;
	}
	
	public Blinker(GameFont font) {
		this.font = font;
	}
	
	public void destroy() {
		clip = null;
		font = null;
		numOfBlinks = 0;
	}
	
	public void init(int numOfBlinks, float delay) {
		this.numOfBlinks = numOfBlinks;
		this.delay = delay;
	}
	
	public void setController(IController controller) {
		this.controller = controller;
	}
	
	public void setCallbackStatus(String callbackStatus) {
		this.callbackStatus = callbackStatus;
	}
	
	public void update() {
		if (numOfBlinks > 0) {
			counter += Gdx.graphics.getDeltaTime();
			
			if (counter > delay) {
				blink();
			}
		}
		else if (callbackStatus != null) {
			controller.updateView(callbackStatus, "");
			
			callbackStatus = null;
		}
	}
	
	public void start() {
		if (clip != null || font != null) {
			blink();			
		}
	}

	private void blink() {
		if (clip != null) {
			clip.visible = !clip.visible;
			
			if (clip.visible) {
				numOfBlinks--;
			}
		}
		else {
			font.visible = !font.visible;
			
			if (font.visible) {
				numOfBlinks--;
			}
		}
		
		counter = 0;
	}
}
