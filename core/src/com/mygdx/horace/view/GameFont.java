package com.mygdx.horace.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;


public class GameFont extends BitmapFont {
	public int x;
	public int y;
	public String text;
	public boolean visible;
	
	private GlyphLayout layout;
	
	public GameFont(String path) {
		super(Gdx.files.internal(path));
		setColor(0, 0, 0, 1);
		
		layout = new GlyphLayout();
		
		visible = true;
	}
	
	public void setText(String text) {
		this.text = text;
		
		layout.setText(this, this.text);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, (int)(layout.width * getData().scaleX), (int)(layout.height * getData().scaleY));
	}

	@Override
	public void dispose() {
		super.dispose();
		
		layout = null;
	}

	public void setScale(float f, float g) {
		getData().setScale(f, g);
		
	}
	
	

	
}
