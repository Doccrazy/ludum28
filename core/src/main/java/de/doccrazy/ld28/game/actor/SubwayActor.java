package de.doccrazy.ld28.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;

public class SubwayActor extends Box2dActor {
	public static final float SIZE_Y = 2f;
	public static final float SPEED = 4f;

	private DeathPortalActor portal1;
	private DeathPortalActor portal2;
	private float widthSprite;
	private float stateTime = 0f;

	public SubwayActor(GameWorld world, DeathPortalActor portal1, DeathPortalActor portal2) {
		super(world);
		this.portal1 = portal1;
		this.portal2 = portal2;

		widthSprite = Resource.subway.getWidth() * SIZE_Y / Resource.subway.getHeight();
		float x = portal1.getX() + portal1.getWidthPortal()/2f;
		setBounds(x, portal1.getY(), (portal2.getX() + portal2.getWidthPortal()/2f) - x, SIZE_Y);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		if (SPEED*stateTime >= getWidth() + widthSprite) {
			remove();
		}
		if (world.getPlayer() != null && getX() + SPEED*stateTime > world.getPlayer().getBody().getPosition().x) {
			world.killPlayer();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		clipBegin();
		batch.begin();
		batch.draw(Resource.subway, getX() - widthSprite + SPEED*stateTime, getY(), getOriginX(), getOriginY(),
				widthSprite, getHeight(), getScaleX(), getScaleY(), 0);
		batch.end();
		clipEnd();
		batch.begin();
	}
}
