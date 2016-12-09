package com.mygdx.horace.control;

import java.util.HashMap;
import java.util.Hashtable;

import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	static private SoundManager instance;
	
	private HashMap bgSound;
	private HashMap sndEffect;
	
	public SoundManager(Locker locker) {
		bgSound = new HashMap();
		sndEffect = new HashMap();
	}
	
	static public SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager(new Locker());
		}
		
		return instance;
	}
	
	public void playEffect(Sound sound) {
		if (sndEffect.get("sound") == null) {
			sndEffect.put("sound", sound);
		}
		
		sndEffect.put("id", sound.play());
	}
	
	public void playBg(Sound sound) {
		if (bgSound.get("sound") == null) {
			bgSound.put("sound", sound);
		}
		
		bgSound.put("id", sound.play());
	}
	
	public void playBg(Sound sound, float volume) {
		playBg(sound);
		
		Sound snd = (Sound)bgSound.get("sound");
		Long id = (Long)bgSound.get("id");
		
		snd.setVolume(id, volume);
		
	}
	
	static private class Locker{}
}
