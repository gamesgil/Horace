package com.mygdx.horace.model;

import com.mygdx.horace.control.IController;

public interface IGame {
	public void setController(IController controller);
	public void init();
	public void updateStatus(String status);
	public int getScore();
	public int getLives();
}
