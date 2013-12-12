package de.doccrazy.ld28.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.doccrazy.ld28.core.Main;
import de.doccrazy.ld28.game.GameScreen;

public class GameDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = GameScreen.SCREEN_WIDTH;
		config.height = GameScreen.SCREEN_HEIGHT;
		config.vSyncEnabled = true;
		config.title = "Ludum Dare 28";
		new LwjglApplication(new Main(), config);
	}
}
