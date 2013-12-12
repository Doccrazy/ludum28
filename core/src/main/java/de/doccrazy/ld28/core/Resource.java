package de.doccrazy.ld28.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Resource {
	public static Sprite libgdxLogo;

	private Resource() {
	}
	
	public static void init() {
		TextureAtlas atlasGame = new TextureAtlas(Gdx.files.internal("game.atlas"));
		libgdxLogo = atlasGame.createSprite("libgdx-logo");
	}
}
