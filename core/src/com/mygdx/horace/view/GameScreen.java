package com.mygdx.horace.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.horace.control.IController;
import com.mygdx.horace.control.SoundManager;
import com.mygdx.horace.model.Status;

public class GameScreen implements Screen, IView, InputProcessor {
	private IController controller;

	private Renderer renderer;
	private OrthographicCamera camera;

	private ArrayList<String> layout;
	private boolean isAccelSupported;

	private Horace horace;
	private float posY = 0;

	private boolean isPaused = false;
	private String status = Status.READY;

	private int startLine = 0;

	private int currentKey = 0;

	private final float SIDEWAYS_SWAY = 1f;
	private final int TILE_SIZE = 64;
	private final float GAME_WORLD_WIDTH = TILE_SIZE * 9;
	private final float GAME_WORLD_HEIGHT = TILE_SIZE * 6;
	private final float MIN_SPEED = 1f;
	private final float MAX_SPEED = 2f;
	private final int TOPMOST_POS = 6;
	private final int BOTTOM_POS = 5;
	private final float DOWNWARD_SLIDE = 0.01f;

	private float elapsedTime = 0;

	private ShapeRenderer shape;
	private ShapeRenderer hColl;
	private ShapeRenderer fColl;
	
	private boolean isAccel;
	
	public GameScreen(SpriteBatch batch, ArrayList<String> layout) {
		renderer = new Renderer(batch);

		this.layout = layout;

		MovieClip.tileSize = TILE_SIZE;

		horace = new Horace(GameAssets.getSprite("horace_c").getTexture());
		horace.addTexture("slide_left", GameAssets.getSprite("horace_l").getTexture());
		horace.addTexture("sway_left", GameAssets.getSprite("horace_ll").getTexture());
		horace.addTexture("slide_right", GameAssets.getSprite("horace_r").getTexture());
		horace.addTexture("sway_right", GameAssets.getSprite("horace_rr").getTexture());
		horace.addTexture("boom", GameAssets.getSprite("boom").getTexture());

		horace.xMin = 0;
		horace.xMax = GAME_WORLD_WIDTH;
		horace.yMax = GAME_WORLD_HEIGHT - TILE_SIZE * 2;
		horace.yMin = horace.yMax - TILE_SIZE * 1;
		horace.setX((horace.xMax - horace.xMin - horace.getWidth()) / 2);
		horace.setY(horace.yMin);
		horace.setCollisionArea(new Rectangle(20, 0, horace.getWidth() - 40, 20));
		renderer.add(horace);

		GameFont font = new GameFont("fonts/c64.fnt");
		renderer.add("ttlScore", font);
		font.setText("Score: ");
		font.getData().setScale(0.5f);
		font.x = TILE_SIZE * 2;
		font.y = (int) (GAME_WORLD_HEIGHT) - TILE_SIZE / 4;

		font = new GameFont("fonts/c64.fnt");
		renderer.add("txtScore", font);
		font.setText("0");
		font.getData().setScale(0.5f);
		font.x = renderer.getTextField("ttlScore").x + (int)renderer.getTextField("ttlScore").getBounds().width;
		font.y = (int) (GAME_WORLD_HEIGHT) - TILE_SIZE / 4;

		font = new GameFont("fonts/c64.fnt");
		renderer.add("ttlLives", font);
		font.setText("Lives: ");
		font.getData().setScale(0.5f);
		font.x = renderer.getTextField("txtScore").x + (int)renderer.getTextField("txtScore").getBounds().width + TILE_SIZE;
		font.y = (int) (GAME_WORLD_HEIGHT) - TILE_SIZE / 4;

		font = new GameFont("fonts/c64.fnt");
		renderer.add("txtLives", font);
		font.setText("0");
		font.getData().setScale(0.5f);
		font.x = renderer.getTextField("ttlLives").x + (int)renderer.getTextField("ttlLives").getBounds().width;
		font.y = (int) (GAME_WORLD_HEIGHT) - TILE_SIZE / 4;

		font = new GameFont("fonts/c64.fnt");
		renderer.add("txtGameOver", font);
		font.setText("GAME OVER!");
		font.x = (int) ((GAME_WORLD_WIDTH - font.getBounds().width) / 2);
		font.y = (int) ((GAME_WORLD_HEIGHT + font.getBounds().height) / 2);
		font.setColor(1, 0, 0, 1);
		font.visible = false;
		
		shape = new ShapeRenderer();
		hColl = new ShapeRenderer();
		fColl = new ShapeRenderer();
		
		boolean available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		
		setStatus(Status.READY);
	}

	@Override
	public void show() {
		isAccelSupported = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		camera = new OrthographicCamera(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
		camera.position.set(GAME_WORLD_WIDTH / 2, GAME_WORLD_HEIGHT / 2, 0);

		// add all clips to renderer
		MovieClip clip = null;
		String textureName = "";
		String val;
		Boolean isObstacle;
		Boolean isBonus;
		Boolean isSuperBonus;
		Boolean isHurdle;

		for (int i = 0; i < layout.size(); i++) {
			for (int j = 0; j < layout.get(i).length(); j++) {
				clip = null;
				val = layout.get(i).substring(j, j + 1);
				isObstacle = false;
				isBonus = false;
				isSuperBonus = false;
				isHurdle = false;

				switch (val) {
				case "T":
					textureName = "tree";
					isObstacle = true;
					break;

				case "^":
					textureName = "hurdle";
					isHurdle = true;
					break;

				case "[":
					textureName = "flag_l";
					break;

				case "]":
					textureName = "flag_r";
					break;

				case "F":
					textureName = "flag_trigger";
					isBonus = true;
					break;
					
				case "!":
					textureName = "finish";
					break;
					
				case "E":
					textureName = "end_trigger";
					isSuperBonus = true;
					break;
				}

				if (textureName != "") {
					clip = new MovieClip(GameAssets.getSprite(textureName).getTexture(), textureName);
					clip.xMin = 0;
					clip.xMax = GAME_WORLD_WIDTH;
					clip.yMin = 0;
					clip.yMax = GAME_WORLD_HEIGHT - TILE_SIZE * 1;
					clip.setPosition(TILE_SIZE * j, GAME_WORLD_HEIGHT - (i + 2) * TILE_SIZE + posY);
					clip.setProp("obstacle", isObstacle);
					clip.setProp("bonus", isBonus);
					clip.setProp("superBonus", isSuperBonus);
					clip.setProp("hurdle", isHurdle);
					renderer.add(clip);

					if (textureName == "flag_trigger") {
						clip.setAlpha(0);
						clip.setCollisionArea(new Rectangle(0, 0, clip.getWidth(), 10));
					}
					else if (textureName == "end_trigger") {
						clip.setCollisionArea(new Rectangle(0, 0, clip.getWidth(), clip.getHeight()));
					}

					textureName = "";
				}
			}
		}

		isAccel = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		Gdx.input.setInputProcessor(this);
		
		SoundManager.getInstance().playBg(GameAssets.getSound("sndBgMusic"), 0.05f);
	}

	@Override
	public void render(float delta) {
		checkKeys();
		checkAccel();

		Collections.sort(renderer.getAllClips());
		// System.out.println("horace: " +
		// renderer.getAllClips().indexOf(horace));

		float lastPosY = posY;

		if (!isPaused) {
			posY++;
		}

		if (!isPaused) {
			for (MovieClip clip : renderer.getAllClips("horace")) {
				clip.setY(clip.getY() + 1);
			}

			MovieClip collision = horace.checkCollision(renderer.getAllClips("horace"));

			if (collision != null) {
				if (collision.getProp("obstacle") == true) {
					isPaused = true;

					horace.setTexture("boom");

					elapsedTime = 0;

					SoundManager.getInstance().playEffect(GameAssets.getSound("sndHit"));
					
					controller.updateModel(Status.HIT);
				} else if (collision.getProp("bonus") == true) {
					controller.updateModel(Status.FLAG);

					collision.setProp("bonus", false);
					
					SoundManager.getInstance().playEffect(GameAssets.getSound("sndBonus"));
				}  else if (collision.getProp("superBonus") == true) {
					collision.setProp("superBonus", false);
					
					SoundManager.getInstance().playEffect(GameAssets.getSound("sndWin"));
					
					controller.updateModel(Status.CROSSED_THE_LINE);
					
				} else if (collision.getProp("hurdle") == true) {
					collision.setProp("hurdle", false);

					horace.setStatus(Status.HURDLE);
				}
			} else {
				if (Math.floor(posY / TILE_SIZE) >= layout.size()) {
					isPaused = true;

					setStatus(Status.GAME_OVER);
				}
			}

		} else {
			
			elapsedTime += delta;
			Gdx.app.error("elapsed time", elapsedTime + "");
			if (elapsedTime > 1) {
				
				if (status != Status.GAME_OVER) {
					isPaused = false;

					resetPositions();
				} else {
					renderer.getTextField("txtGameOver").visible = true;

					if (elapsedTime > 2) {
						controller.updateModel(Status.GAME_OVER);
					}
				}
			}
		}

		if (camera != null) {
			Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			renderer.render(camera);
			
		}
	}

	private void resetPositions() {
		for (MovieClip clip : renderer.getAllClips("horace")) {
			clip.setY(clip.getY() - posY);

			switch (clip.getTextureName()) {
			case "hurdle":
				clip.setProp("hurdle", true);
				break;

			case "tree":
				clip.setProp("obstacle", true);
				break;

			case "flag_trigger":
				clip.setProp("bonus", true);
				break;
				
			case "end_trigger":
				clip.setProp("superBonus", true);
				break;
			}
		}

		posY = 0;
		horace.setX((horace.xMax - horace.xMin - horace.getWidth()) / 2);

		horace.setTexture("horace");
	}

	private void checkAccel() {
		if (isAccel) {
			/*if (Gdx.input.getAccelerometerX() > 2) {
				if (Gdx.input.getAccelerometerX() < 4 && horace.getTextureName() != "slide_left") {
					horace.moveLeft();
				}
				else if (Gdx.input.getAccelerometerX() > 4) {
					
				}
				
			}
			else if (Gdx.input.getAccelerometerX() < -2) {
				horace.moveRight();
			}*/
				
			//Gdx.app.log("tag", Gdx.input.getAccelerometerX() + ", " + Gdx.input.getAccelerometerY() + ", " + Gdx.input.getAccelerometerZ());
		}
			
			
	}

	private void checkKeys() {
		
	}

	private void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(IController controller) {
		// TODO Auto-generated method stub
		this.controller = controller;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		renderer.destroy();
		shape.dispose();
		
		renderer = null;
		shape = null;
		camera = null;
		horace = null;
		layout = null;
		controller = null;
		Gdx.input.setInputProcessor(null);
		
		GameAssets.getSound("sndBgMusic").stop();
		GameAssets.getSound("sndBonus").stop();
		GameAssets.getSound("sndHit").stop();
		GameAssets.getSound("sndWin").stop();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
			horace.moveLeft();
			break;

		case Keys.RIGHT:
			horace.moveRight();
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX > GAME_WORLD_WIDTH / 2) {
			horace.moveRight();
		}
		else {
			horace.moveLeft();
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateStatus(String status, int value) {
		switch (status) {
		case Status.SCORE:
			renderer.getTextField("txtScore").setText(String.valueOf(value));
			break;

		case Status.LIVES:
			renderer.getTextField("txtLives").setText(String.valueOf(value));
			break;

		case Status.GAME_OVER:
			this.status = status;
			break;

		case Status.START_PLAYING:

			break;
		}
	}
}
