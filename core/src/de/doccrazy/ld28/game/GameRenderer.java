package de.doccrazy.ld28.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.doccrazy.ld28.core.Debug;
import de.doccrazy.ld28.game.base.ActorListener;
import de.doccrazy.ld28.game.base.Box2dActor;

public class GameRenderer implements ActorListener {
    // here we set up the actual viewport size of the game in meters.
    public static float UNIT_WIDTH = 24f;
    public static float UNIT_HEIGHT = UNIT_WIDTH*9f/16f;
	private static final Vector2 CAMERA_WINDOW = new Vector2(3, 2);

    private GameWorld world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;
    private Vector2 playerPos, cameraPos = new Vector2(-100, 100);
	private float zoom = 1;

    public GameRenderer(GameWorld world) {
        this.world = world;
        world.stage.setViewport(new ExtendViewport(UNIT_WIDTH, UNIT_HEIGHT));
        renderer = new Box2DDebugRenderer();

        // we obtain a reference to the game stage camera. The camera is scaled to box2d meter units
        camera = (OrthographicCamera) world.stage.getCamera();

        world.rayHandler.setAmbientLight(new Color(0.05f, 0.1f, 0.05f, 0.2f));
    }

    public void render() {
        positionCamera();

        camera.update();
        // box2d debug renderering (optional)
        if (Debug.ON) {
        	renderer.render(world.box2dWorld, camera.combined);
        }

        // game stage rendering
        world.stage.draw();

        world.rayHandler.setCombinedMatrix(camera.combined);
        world.rayHandler.updateAndRender();
    }

	private void positionCamera() {
		ExtendViewport vp = (ExtendViewport)world.stage.getViewport();
		vp.setMinWorldWidth(UNIT_WIDTH*zoom);
		vp.setMinWorldHeight(UNIT_HEIGHT*zoom);
        world.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true); // set the game stage viewport to the meters size

        // have the camera follow bob
        if (world.getPlayer() != null) {
        	playerPos = new Vector2(world.getPlayer().getX() + 5f, world.getPlayer().getY());
        }
    	cameraPos.x = clip(cameraPos.x, playerPos.x - CAMERA_WINDOW.x, playerPos.x + CAMERA_WINDOW.x);
    	cameraPos.y = clip(cameraPos.y, playerPos.y - CAMERA_WINDOW.y, playerPos.y + CAMERA_WINDOW.y);
    	camera.position.x = cameraPos.x;
    	camera.position.y = cameraPos.y;
	}

	private float clip(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}

	@Override
	public void actorAdded(Box2dActor actor) {
	}

	@Override
	public void actorRemoved(Box2dActor actor) {
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
}