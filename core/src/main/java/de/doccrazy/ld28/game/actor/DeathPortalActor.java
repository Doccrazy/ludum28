package de.doccrazy.ld28.game.actor;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;

public class DeathPortalActor extends Box2dActor {
	public static final float SIZE_Y = 2f;
	private static final float OPEN_TIME = 1.5f;
	private static final float CONE_DEG = 60f;

	private float stateTime = 0f;
	private int state = -1;
	private Animation anim = Resource.portalAnimRev;
	private float widthPortal;
	private boolean facingRight;
	private ConeLight light;

	public DeathPortalActor(GameWorld world, float xOffset, float delay, boolean facingRight) {
		super(world);
		this.facingRight = facingRight;
		this.stateTime = -delay;

		widthPortal = Resource.portal.getWidth() * SIZE_Y / Resource.portal.getHeight();
		setBounds(world.getPlayer().getBody().getPosition().x - getWidthPortal()/2f + xOffset,
				world.getPlayer().getBody().getPosition().y - world.getPlayer().getOriginY(),
				widthPortal, SIZE_Y);

		addLight();
	}

	private void addLight() {
		light = new ConeLight(world.rayHandler, 100, new Color(1f, 0f, 1f, 0.75f), 5f,
				getX() + (facingRight ? 0f : getWidth()), getY() + getHeight()/2f, facingRight ? 0f : 180f, CONE_DEG);
		light.setXray(true);
		light.setActive(false);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		light.getColor().a = (float) (Math.random() * 0.5f + 0.3f);
		light.setColor(light.getColor());
		if (state == -1 && stateTime >= 0) {
			state = 0;
			light.setActive(true);
		}
		if (anim.isAnimationFinished(stateTime)) {
			if (state == 0) {
				onPortalOpened();
				stateTime = 0;
				state = 1;
				anim = Resource.portalAnim;
				light.setConeDegree(CONE_DEG);
			}
			if (state == 2) {
				stateTime = 0;
				state = 3;
				remove();
			}
		}
		if (state == 1 && stateTime > OPEN_TIME) {
			state = 2;
			stateTime = 0;
		}
		TextureRegion frame = null;
		if (state == 0 || state == 2) {
			frame = anim.getKeyFrame(stateTime);
			float prog = Math.min(stateTime / anim.animationDuration, 1.0f);
			light.setConeDegree(CONE_DEG * (state == 0 ? prog : 1.0f - prog));
		}
		if (state == 1) {
			frame = Resource.portal;
		}
		if (frame != null) {
			batch.draw(frame, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(), 0);
		}
	}

	public void onPortalOpened() {
	}

	public float getWidthPortal() {
		return widthPortal;
	}

	@Override
	public boolean remove() {
		boolean ret = super.remove();
		if (ret) {
			light.remove();
		}
		return ret;
	}

}
