package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameScreen;
import de.doccrazy.ld28.game.GameWorld;

public class HighscoreLabel extends Label {
	private GameWorld world;
	private int highscore = 0;

	public HighscoreLabel(GameWorld world) {
		super("", new LabelStyle(Resource.fontSmall, new Color(1f, 0.4f, 0.3f, 0.7f)));
		this.world = world;

		setPosition(0, 450);
		setWidth(GameScreen.SCREEN_WIDTH);
		setAlignment(Align.center);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		highscore = Math.max(highscore, world.getScore());
		setVisible(world.getPlayer() == null);
		setText("Your Highscore: " + highscore);
	}

}
