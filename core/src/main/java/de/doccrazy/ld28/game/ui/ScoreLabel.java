package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameScreen;
import de.doccrazy.ld28.game.GameWorld;

public class ScoreLabel extends Label {
	private GameWorld world;

	public ScoreLabel(GameWorld world) {
		super("", new LabelStyle(Resource.fontSmall, new Color(1f, 0.4f, 0.3f, 0.7f)));
		this.world = world;

		setPosition(GameScreen.SCREEN_WIDTH - 20, 20);
		setAlignment(Align.right);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setText("Score: " + world.getScore());
	}

}
