package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;

public class TryAgainLabel extends Label {
	private GameWorld world;

	public TryAgainLabel(GameWorld world) {
		super("Press ENTER to try again", new LabelStyle(Resource.fontSmall, new Color(1f, 0.4f, 0.3f, 0.7f)));
		this.world = world;

		setAlignment(Align.center);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setPosition(0, getStage().getHeight() - 50);
		setWidth(getStage().getWidth());

		boolean below = world.getPlayer() == null || world.getPlayer().getBody().getPosition().y < 2f;
		setVisible(below && System.currentTimeMillis() % 500 < 250);
	}

}
