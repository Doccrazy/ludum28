package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameScreen;
import de.doccrazy.ld28.game.GameWorld;

public class TryAgainLabel extends Label {
	private GameWorld world;
	private int highscore = 0;

	public TryAgainLabel(GameWorld world) {
		super("Press ENTER to try again", new LabelStyle(Resource.fontSmall, new Color(1f, 0.4f, 0.3f, 0.7f)));
		this.world = world;

		setPosition(0, GameScreen.SCREEN_HEIGHT - 50);
		setWidth(GameScreen.SCREEN_WIDTH);
		setAlignment(Align.center);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		boolean below = world.getPlayer() == null || world.getPlayer().getBody().getPosition().y < 2f;
		setVisible(below && System.currentTimeMillis() % 500 < 250);
	}

}
