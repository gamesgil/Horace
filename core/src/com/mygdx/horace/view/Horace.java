package com.mygdx.horace.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.horace.model.Status;


public class Horace extends MovieClip {
	private boolean isPlayerInteractionEnabled = true;
	private float timeCounter = 0;
	
	public Horace(Texture texture) {
		super(texture, "horace");
	}
	
	public void setStatus(String status) {
		switch (status) {
		case Status.HURDLE:
			if (isPlayerInteractionEnabled) {
				if (Math.random() < 0.5f) {
					driftLeft();
				}
				else {
					driftRight();
				}
				
				timeCounter = 0;
				break;
			}
		}
	}
	
	public void skiDown() {
		setTexture("horace");
	}
	
	private void driftLeft() {
		slideLeft();
		
		isPlayerInteractionEnabled = false;
	}
	
	private void driftRight() {
		slideRight();
		
		isPlayerInteractionEnabled = false;
	}
	
	public void moveLeft() {
		if (isPlayerInteractionEnabled) {
			switch (getTextureName()) {
			case "horace":
				slideLeft();
				break;
				
			case "slide_left":
				swayLeft();
				break;
				
			case "sway_right":
				slideRight();
				break;
				
			case "slide_right":
				skiDown();
				break;
			}
		}
		
	}
	
	public void moveRight() {
		if (isPlayerInteractionEnabled) {
			switch (getTextureName()) {
			case "horace":
				slideRight();
				break;
				
			case "slide_right":
				swayRight();
				break;
				
			case "sway_left":
				slideLeft();
				break;
				
			case "slide_left":
				skiDown();
				break;
			}
		}
		
	}
	
	private void slideLeft() {
		setTexture("slide_left");
	}
	
	private void slideRight() {
		setTexture("slide_right");
	}
	
	private void swayLeft() {
		setTexture("sway_left");
	}
	
	private void swayRight() {
		setTexture("sway_right");
	}

	@Override
	public void update() {
		switch (getTextureName()) {
		case "sway_left":
			setY(getY() + 2);				
		case "slide_left":
			setX(getX() - 1);
			setY(getY() - 1);	
			break;
			
		case "sway_right":
			setY(getY() + 2);
		case "slide_right":
			setX(getX() + 1);
		case "horace":
			setY(getY() - 1);	
			break;
		}
		
		if (!isPlayerInteractionEnabled && timeCounter > Gdx.graphics.getFramesPerSecond()) {
			isPlayerInteractionEnabled = true;
			
			skiDown();
		}
		else {
			timeCounter++;
		}
			
	}
	
	@Override
	public void setX(float x) {
		super.setX(Math.max(xMin, Math.min(xMax, x)));
	}

	@Override
	public void setY(float y) {
		super.setY(Math.max(yMin, Math.min(yMax, y)));
	}
}
