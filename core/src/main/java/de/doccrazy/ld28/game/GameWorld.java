package de.doccrazy.ld28.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameWorld {
    // here we set up the actual viewport size of the game in meters.
    public static float UNIT_WIDTH = 12.8f;
    public static float UNIT_HEIGHT = 7.2f;

    public static final Vector2 GRAVITY = new Vector2(0, -9.8f);

    public final Stage stage; // stage containing game actors (not GUI, but actual game elements)
    public World box2dWorld; // box2d world

    public GameWorld() {
        box2dWorld = new World(GRAVITY, true);
        stage = new Stage(); // create the game stage
        stage.setViewport(UNIT_WIDTH, UNIT_HEIGHT, false); // set the game stage viewport to the meters size

        createWorld();
    }

    private void createWorld() {

    }

    public void update(float delta) {
        // perform game logic here
        box2dWorld.step(delta, 6, 3); // update box2d world
        stage.act(delta); // update game stage
    }
}
