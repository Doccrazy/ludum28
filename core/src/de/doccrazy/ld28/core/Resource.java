package de.doccrazy.ld28.core;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Resource {
	public static Texture background;
	public static Texture tex;

	public static Sprite playerStand;
	public static Animation playerWalk;
	public static Texture scifi1;
	public static Texture scifi2;
	public static BodyEditorLoader pillar;
	public static Animation portalAnim;
	public static Animation portalAnimRev;
	public static Sprite portal;
	public static Sprite subway;

	public static Sprite[] bodyparts = new Sprite[3];
	public static BitmapFont fontBig, fontSmall;
	public static Sound die;
	public static Sound checkpoint;
	public static Sound jump;
	public static Sound subwayFx;
	public static Sound start;

	private Resource() {
	}

	public static void init() {
		background = new Texture(Gdx.files.internal("background2.png"));
		tex = new Texture(Gdx.files.internal("tex.png"));
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		TextureAtlas atlasGame = new TextureAtlas(Gdx.files.internal("game.atlas"));
		playerStand = atlasGame.createSprite("stand");
		playerWalk = new Animation(0.2f, atlasGame.findRegions("walk"));
		scifi1 = new Texture(Gdx.files.internal("scifi1.png"));
		scifi1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		scifi2 = new Texture(Gdx.files.internal("scifi2.png"));
		scifi2.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Array<AtlasRegion> portalFrames = atlasGame.findRegions("portal");
		portalAnim = new Animation(0.025f, portalFrames);
		portalAnimRev = new Animation(0.025f, portalFrames);
		portalAnimRev.setPlayMode(Animation.PlayMode.REVERSED);
		portal = atlasGame.createSprite("portal", 0);
		subway = atlasGame.createSprite("subway");
		bodyparts[0] = atlasGame.createSprite("bodypart1");
		bodyparts[1] = atlasGame.createSprite("bodypart2");
		bodyparts[2] = atlasGame.createSprite("bodypart3");

		World.setVelocityThreshold(World.getVelocityThreshold());  //load Box2d
		pillar = new BodyEditorLoader(Gdx.files.internal("pillar.json"));

		fontBig = new BitmapFont(Gdx.files.internal("big.fnt"), Gdx.files.internal("big.png"), false);
		fontSmall = new BitmapFont(Gdx.files.internal("small.fnt"), Gdx.files.internal("small.png"), false);

		die = Gdx.audio.newSound(Gdx.files.internal("die.wav"));
		checkpoint = Gdx.audio.newSound(Gdx.files.internal("checkpoint.wav"));
		jump = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		subwayFx = Gdx.audio.newSound(Gdx.files.internal("subway.wav"));
		start = Gdx.audio.newSound(Gdx.files.internal("start.wav"));
	}
}
