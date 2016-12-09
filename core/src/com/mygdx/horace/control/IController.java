package com.mygdx.horace.control;

public interface IController {
	public void updateModel(String type);
	public void updateView(String type, int value);
	public void updateView(String type, String value);
	public void destroy();
}
