package de.doccrazy.ld28.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.doccrazy.ld28.core.Main;
import de.doccrazy.ld28.game.GameScreen;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameScreen.SCREEN_WIDTH;
		config.height = GameScreen.SCREEN_HEIGHT;
		config.vSyncEnabled = true;
		config.title = "Anomaly One (a Ludum Dare 28 game by d0ccrazy)";
		new LwjglApplication(new Main(), config);
	}
}
