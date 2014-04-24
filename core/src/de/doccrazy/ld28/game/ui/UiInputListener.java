package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.doccrazy.ld28.core.Debug;
import de.doccrazy.ld28.game.GameRenderer;
import de.doccrazy.ld28.game.GameWorld;

public class UiInputListener extends InputListener {
	private GameWorld world;
	private GameRenderer renderer;

	public UiInputListener(GameWorld world, GameRenderer renderer) {
		this.world = world;
		this.renderer = renderer;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		if (keycode == Keys.ENTER) {
			world.reset();
		}
		if (Debug.ON) {
			if (keycode == Keys.F) {
				world.getPlayer().toggleFlyMode();
			}
			if (keycode == Keys.Z) {
				renderer.setZoom(7.0f);
			}
		}
		return false;
	}
}
