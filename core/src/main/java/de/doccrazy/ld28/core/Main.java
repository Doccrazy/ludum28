package de.doccrazy.ld28.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

import de.doccrazy.ld28.game.GameScreen;

public class Main extends Game {
	private GameScreen gameScreen;
	private FPSLogger fps;

	@Override
	public void create () {
		if (!Main.class.getResource(Main.class.getSimpleName() + ".class").toString().startsWith("jar:")) {
			packTextures();
		}
		Resource.init();

		gameScreen = new GameScreen();
		setScreen(gameScreen);

		fps = new FPSLogger();
	}

	private void packTextures() {
		TexturePacker2.Settings settings = new TexturePacker2.Settings();
		settings.filterMag = Texture.TextureFilter.Linear;
		settings.filterMin = Texture.TextureFilter.MipMapLinearLinear;
		TexturePacker2.processIfModified(settings, "../assets_src", "../assets_generated", "game");
	}

	@Override
	public void render () {
		super.render();
		//fps.log();
	}
}
