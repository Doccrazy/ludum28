package de.doccrazy.ld28.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.ui.UiInputListener;
import de.doccrazy.ld28.game.ui.UiRoot;

public class GameScreen implements Screen {
    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = (int)(SCREEN_WIDTH*9f/16f);

    private GameWorld world; // contains the game world's bodies and actors.
    private GameRenderer renderer; // our custom game renderer.
    private Stage uiStage; // stage that holds the GUI. Pixel-exact size.
    private SpriteBatch batch;
    private Scaling bgScaling = Scaling.fill;

	@Override
	public void show() {
		batch = new SpriteBatch();
		uiStage = new Stage(new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT));

        world = new GameWorld();
        renderer = new GameRenderer(world);

		Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, world.stage));
		uiStage.getRoot().addListener(new UiInputListener(world, renderer));

		uiStage.addActor(new UiRoot(world));
	}

	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

        drawBackground();

        renderer.render(); // draw the box2d world
        uiStage.draw(); // draw the GUI
	}

	private void drawBackground() {
		batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();
        Vector2 bgSize = bgScaling.apply(SCREEN_WIDTH, SCREEN_HEIGHT, uiStage.getWidth(), uiStage.getHeight());
        batch.draw(Resource.background, uiStage.getWidth()/2 - bgSize.x/2, uiStage.getHeight()/2 - bgSize.y/2, bgSize.x, bgSize.y);
        batch.end();
	}

	private void update(float delta) {
        world.update(delta); // update the box2d world
        uiStage.act(delta); // update GUI
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		uiStage.dispose();
		batch.dispose();
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
