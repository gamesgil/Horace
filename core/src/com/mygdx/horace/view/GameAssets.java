package com.mygdx.horace.view;

import java.util.Vector;

import com.badlogic.gdx.audio.Sound;

public class GameAssets {
	static private Vector<Asset> assets = new Vector<Asset>(1);
	
	public static void load(String name, String filename)
	{
		assets.addElement(new Asset(name, filename));
	}
	
	static public MovieClip getSprite(String name)
	{
		MovieClip result = null;
		
		for (int i = 0; i < assets.size(); i++)
		{
			if (assets.get(i).name == name && assets.get(i).getType() == Asset.SPRITE)
			{
				result = assets.get(i).getSprite();
				break;
			}
		}
		
		return result;
	}
	
	static public Sound getSound(String name)
	{
		Sound result = null;
		
		for (int i = 0; i < assets.size(); i++)
		{
			if (assets.get(i).name == name && assets.get(i).getType() == Asset.AUDIO)
			{
				result = assets.get(i).getSound();
				break;
			}
		}
		
		return result;
	}
}
