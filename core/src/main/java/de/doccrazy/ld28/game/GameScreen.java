package de.doccrazy.ld28.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.ui.UiRoot;

public class GameScreen implements Screen {
    public static int SCREEN_WIDTH = 800;
    public static int SCREEN_HEIGHT = 480;

    private GameWorld world; // contains the game world's bodies and actors.
    private GameRenderer renderer; // our custom game renderer.
    private Stage stage; // stage that holds the GUI. Pixel-exact size.
    private OrthographicCamera guiCam; // camera for the GUI. It's the stage default camera.
    private SpriteBatch batch = new SpriteBatch();

	@Override
	public void show() {
		stage = new Stage();
		//stage.addActor(new TestActor());

        world = new GameWorld();
        renderer = new GameRenderer(world);

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, world.stage));
		stage.getRoot().addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					world.reset();
				}
				if (keycode == Keys.F) {
					world.getPlayer().toggleFlyMode();
				}
				return false;
			}
		});

		stage.addActor(new UiRoot(world));
	}

	@Override
	public void render(float delta) {
		update(delta);

        guiCam = (OrthographicCamera) stage.getCamera();
        guiCam.position.set(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 0);

		Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
        guiCam.update();

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(Resource.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        renderer.render(); // draw the box2d world
        stage.draw(); // draw the GUI
	}

	private void update(float delta) {
        world.update(delta); // update the box2d world
        stage.act(delta); // update GUI
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(SCREEN_WIDTH, SCREEN_HEIGHT, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
