package com.mygdx.horace.view;

import com.mygdx.horace.control.IController;

public interface IView {
	public void setController(IController controller);
	public void update();
	public void updateStatus(String status, int value);
	public void destroy();
}
