package com.mygdx.horace.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Renderer {
	private SpriteBatch batch;
	private ArrayList<MovieClip> clips;
	private HashMap<String, GameFont> fonts;
	
	
	public Renderer(SpriteBatch batch) {
		this.batch = batch;
		
		clips = new ArrayList<MovieClip>();
		fonts = new HashMap<String, GameFont>();
	}
	
	public void destroy() {
		for (int i = 0; i < clips.size(); i++) {
			clips.get(i).destroy();
		}
		
		for (String fontName : fonts.keySet()) {
			fonts.get(fontName).dispose();
		}
		
		clips = null;
		batch = null;
		fonts = null;
	}
	
	public void add(MovieClip clip) {
		clips.add(clip);
	}
	
	public void add(String name, GameFont font) {
		fonts.put(name, font);
	}
	
	public void render(OrthographicCamera camera) {
		if (camera != null) {
			camera.update();
			
			batch.begin();
			batch.setProjectionMatrix(camera.combined);
			
			for (int i = 0; i < clips.size(); i++) {
				if (clips.get(i).visible)
				{
					clips.get(i).draw(batch);
					clips.get(i).update();
					
					
				}
			}
			
			for (String fontName : fonts.keySet()) {
				if (fonts.get(fontName).visible) {
					fonts.get(fontName).draw(batch, fonts.get(fontName).text, fonts.get(fontName).x, fonts.get(fontName).y);
				}
			}
			
			batch.end();
			
			/*for (int i = 0; i < clips.size(); i++) {
				if (clips.get(i).visible)
				{
					clips.get(i).getShape().setProjectionMatrix(camera.combined);
					clips.get(i).getShape().begin(ShapeType.Line);
					clips.get(i).getShape().setColor(1, 1, 1, 0.5f);
					clips.get(i).getShape().rect(clips.get(i).getCollisionArea().x, clips.get(i).getCollisionArea().y, clips.get(i).getCollisionArea().width, clips.get(i).getCollisionArea().height);
					clips.get(i).getShape().end();
				}
			}*/
		}
		
	}

	public SpriteBatch getBatch() {
		return batch;
	}
	
	public ArrayList<MovieClip> getAllClips(String... exclude) {
		ArrayList<MovieClip> result = new ArrayList<MovieClip>();
		
		if (exclude.length > 0) {
			for (int i = 0; i < clips.size(); i++) {
				for (int j = 0; j < exclude.length; j++) {
					if (clips.get(i).name != exclude[j]) {
						result.add(clips.get(i));
					}
				}
			}
		}
		else {
			result = clips;
		}
		
		return result;
	}
	
	public GameFont getTextField(String name) {
		GameFont result = fonts.get(name);
		
		return result;
	}
}
