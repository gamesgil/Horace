package com.mygdx.horace.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class MovieClip extends Sprite implements Comparable<MovieClip> {
	static public int tileSize = 1;
	
	protected HashMap<String, Texture> textures;
	private HashMap<String, Boolean> basicProps;
	private String textureName = "";
	
	public String name = "";
	
	public float xMax;
	public float yMax;
	public float xMin;
	public float yMin;
	
	public boolean visible;

	private Rectangle collisionArea;
	
	private ShapeRenderer shape;
	
	public MovieClip(Texture texture, String name)
	{
		super(texture);
		
		this.name = name;
		
		textures = new HashMap<String, Texture>();
		textures.put(name, texture);
		
		setTexture(name);
		
		basicProps = new HashMap<String, Boolean>();
		
		setCollisionArea(new Rectangle(0, 0, getWidth(), getHeight()));
		shape = new ShapeRenderer();
	}
	
	public ShapeRenderer getShape() {
		return shape;
	}
	
	public void update() {
		
	}
	
	@Override
	public String toString() {
		return name + " (" + getX() + ", " + getY() + "): " + visible;
	}

	public void addTexture(String name, Texture texture) {
		textures.put(name, texture);
	}
	
	protected void setTexture(String name) {
		Texture texture = textures.get(name);
		
		if (texture != null) {
			setTexture(texture);
			
			textureName = name;
		}
	}
	
	public String getTextureName() {
		return textureName;
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		
		if (canMoveFreely() || isWithinBounds())
		{
			visible = true;
		}
		else if (!canMoveFreely())
		{
			visible = false;
		}
	}

	@Override
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		
		if (canMoveFreely() || isWithinBounds())
		{
			visible = true;
		}
		else if (!canMoveFreely())
		{
			visible = false;
		}
		
		
	}
	
	protected boolean isWithinBounds() {
		boolean result = (getX() >= xMin && getX() <= xMax && getY() >= yMin && getY() <= yMax);
		
		return result;
	}
	
	protected int getTiledPosX() {
		int result = (int)getX() / tileSize;
		
		return result;
	}

	protected int getTiledPosY() {
		int result = (int)getY() / tileSize;
		
		return result;
	}
	
	public boolean checkCollision(MovieClip other) {
		boolean result = false;
		
		if (getTiledPosX() == other.getTiledPosX() && getTiledPosY() == other.getTiledPosY()) {
			result = true;
		}
		
		return result;
	}
	
	public MovieClip checkCollision(ArrayList<MovieClip> others) {
		MovieClip result = null;
		float xDist = 0;
		float yDist = 0;
		
		for (int i = 0; i < others.size(); i++) {
			/*xDist = Math.abs(getX() - others.get(i).getX());
			yDist = Math.abs(getY() - others.get(i).getY());
			
			if (xDist <= collisionBuffer.x && 
				yDist <= collisionBuffer.y) {
				result = others.get(i);
				break;
			}*/
			
			if (getCollisionArea().overlaps(others.get(i).getCollisionArea())) {
				result = others.get(i);
				break;
			}
		}
		
		return result;
	}

	public void setCollisionArea(Rectangle area) {
		collisionArea = area;
		
		//System.out.println("collisionArea: "  +getCollisionArea());
	}
	
	public Rectangle getCollisionArea() {
		Rectangle result = new Rectangle(getX() + collisionArea.x, getY() + collisionArea.y, collisionArea.width, collisionArea.height);
		
		return result;
	}

	@Override
	public void draw(Batch batch) {
		if (visible) {
			super.draw(batch);	
		}
	}

	public boolean canMoveFreely()
	{
		boolean result = (xMin == xMax && xMin == 0 && yMin == yMax && yMin == 0);
		
		return result;
	}
	
	public void setProp(String key, Boolean value) {
		basicProps.put(key, value);
	}
	
	public Boolean getProp(String key) {
		return basicProps.get(key);
	}
	
	public void destroy() {
		textures = null;
		basicProps = null;
	}

	@Override
	public int compareTo(MovieClip otherClip) {
		int result = 0;
		float otherY = otherClip.getY();

		if (getY() > otherY) {
			result = -1;
		}
		else if (getY() < otherY) {
			result = 1;
		}
		
		return result;
	}
}
