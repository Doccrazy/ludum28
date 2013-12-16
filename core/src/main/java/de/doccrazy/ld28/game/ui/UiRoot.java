package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.scenes.scene2d.Group;

import de.doccrazy.ld28.game.GameWorld;

public class UiRoot extends Group {
	private GameWorld world;

	public UiRoot(GameWorld world) {
		this.world = world;

		addActor(new DeathLabel(world));
	}
}
