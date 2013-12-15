package de.doccrazy.ld28.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;

public class BodypartActor extends Box2dActor {
	private static final float RADIUS = 0.2f;

	private Sprite sprite;
	private float scale;

	public BodypartActor(GameWorld world, Vector2 pos) {
		super(world);
		sprite = Resource.bodyparts[(int) (Math.random()*Resource.bodyparts.length)];

		// generate bob's box2d body
        CircleShape circle = new CircleShape();
        circle.setRadius(RADIUS);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.x = pos.x;
        bodyDef.position.y = pos.y;
        bodyDef.linearDamping = 0.1f;
        bodyDef.angularDamping = 0.5f;

        this.body = world.box2dWorld.createBody(bodyDef);
        this.body.setUserData(this);
        float angle = (float) (Math.random() * Math.PI);
        Vector2 v = new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
        v.mul(10);
        body.setLinearVelocity(v);
        //body.setAngularVelocity((float) (Math.random()*10f));

        Fixture fix = body.createFixture(circle, 100);
        fix.setFriction(3.5f);
        fix.setRestitution(0.5f);
        //fix.setFilterData(filter);

        circle.dispose();

        setScale((float) (Math.random() + 0.5f));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
    	setOrigin(RADIUS, RADIUS);
        setRotation(MathUtils.radiansToDegrees * body.getAngle());
        setPosition(body.getPosition().x-RADIUS, body.getPosition().y-RADIUS);

        batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(),
    			RADIUS*2, RADIUS*2, getScaleX(), getScaleY(), body.getAngle());
	}

}
