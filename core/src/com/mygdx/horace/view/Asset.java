package com.mygdx.horace.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Asset {
	public static final String SPRITE = "sprite";
	public static final String AUDIO = "audio";
	
	public String name;
	
	private MovieClip sprite;
	private String type;
	private Sound sound;
	
	public Asset(String name, String filename)
	{
		this.name = name;
		
		switch (filename.substring(filename.length() - 3))
		{
			case "mp3":
			case "wav":
			case "ogg":
				type = AUDIO;
				sound = Gdx.audio.newSound(Gdx.files.internal(filename));
				break;
		
			case "png":
			case "jpg":
			case "jpeg":
			default:
				type = SPRITE;
				Texture img = new Texture(filename);
				img.setFilter(TextureFilter.Linear,  TextureFilter.Linear);
				sprite = new MovieClip(img, name);
				break;
		}
	}
	
	public MovieClip getSprite()
	{
		return sprite;
	}
	
	public Sound getSound()
	{
		return sound;
	}
	
	public String getType()
	{
		return type;
	}
}
