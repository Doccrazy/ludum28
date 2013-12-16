package de.doccrazy.ld28.game.actor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;
import de.doccrazy.ld28.game.base.CollisionListener;
import de.doccrazy.ld28.game.base.MovementInputListener;

public class Player extends Box2dActor implements CollisionListener {
	public static final float RADIUS = 0.4f;
	private static final int CONTACT_TTL = 50;
	private static final float VELOCITY = 10.f;
	private static final float JUMP_IMPULSE = 250f;
	private static final float AIR_CONTROL = 100f;

	private MovementInputListener movement;
	private Map<Body, ContactInfo> floorContacts = new HashMap<Body, ContactInfo>();
	private ConeLight light;
	private float orientation = 1;
	private boolean dead;
	private boolean flyMode;

	public Player(GameWorld world, Vector2 spawn) {
		super(world);
		// generate bob's box2d body
        CircleShape circle = new CircleShape();
        circle.setRadius(RADIUS);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.x = spawn.x;
        bodyDef.position.y = spawn.y + RADIUS + 0.1f;
        bodyDef.linearDamping = 0.1f;
        bodyDef.angularDamping = 0.5f;

        this.body = world.box2dWorld.createBody(bodyDef);
        this.body.setUserData(this);

        Fixture fix = body.createFixture(circle, 100);
        fix.setFriction(3.5f);
        fix.setRestitution(0f);
        //fix.setFilterData(filter);

        circle.dispose();

        // generate bob's actor
        this.setPosition(body.getPosition().x-RADIUS, body.getPosition().y-RADIUS); // set the actor position at the box2d body position
        this.setSize(RADIUS*2, RADIUS*2); // scale actor to body's size
        //this.setScaling(Scaling.stretch); // stretch the texture
        //this.setAlign(Align.center);

        movement = new MovementInputListener();
        addListener(movement);

        addLights();
    }

	private void addLights() {
		light = new ConeLight(world.rayHandler, 100, new Color(0.4f,0.5f,0.8f,1f), 15f, 0, 0, 90, 30);
		//light.setXray(true);
		//light.attachToBody(body, 0, RADIUS*2.0f);
	}

    @Override
    public void act(float delta) {
        super.act(delta);

        processContacts();
        move(delta);
    }

	private void processContacts() {
		for (Iterator<Entry<Body, ContactInfo>> it = floorContacts.entrySet().iterator(); it.hasNext(); ) {
			Entry<Body, ContactInfo> entry = it.next();
			entry.getValue().ttl -= 1;
			if (entry.getValue().ttl <= 0) {
				it.remove();
			}
		}
	}

    private void move(float delta) {
     	Vector2 mv = movement.getMovement();
     	if (mv.x < 0) {
     		orientation = -1;
     	} else if (mv.x > 0) {
     		orientation = 1;
     	}
     	if (flyMode) {
     		body.applyForceToCenter(new Vector2(0, body.getMass() * 9.8f), true);
     		mv.scl(body.getMass() * 10f);
     		body.applyForceToCenter(mv, true);
     	} else {
	     	if (touchingFloor()) {
	     		body.setAngularVelocity(-mv.x*VELOCITY);
	     	} else {
	     		body.applyForceToCenter(mv.x * AIR_CONTROL, 0f, true);
	     	}
     	}
     	if (movement.pollJump() && touchingFloor()) {
     		body.applyLinearImpulse(0f, JUMP_IMPULSE, body.getPosition().x, body.getPosition().y, true);
     		floorContacts.clear();
     	}
	}

    private boolean touchingFloor() {
    	return floorContacts.size() > 0;
    }

	@Override
    public void draw(Batch batch, float parentAlpha) {
		float angle = (float)(2f * Math.cos(4f * (body.getPosition().x % (Math.PI*2f))));
		light.setDirection(90 - 90 * orientation + angle);
		light.setPosition(body.getPosition().x, body.getPosition().y + RADIUS*2f);
        // here we override Actor's act() method to make the actor follow the box2d body
    	setOrigin(RADIUS, RADIUS);
        setRotation(MathUtils.radiansToDegrees * body.getAngle());
        setPosition(body.getPosition().x-RADIUS, body.getPosition().y-RADIUS);

        Matrix4 mat = batch.getTransformMatrix();
        Matrix4 old = mat.cpy();
        mat.trn(getX() + getOriginX(), getY() + getOriginY(), 0);
        mat.scale(orientation, 1, 1);
        batch.setTransformMatrix(mat);
        batch.draw(Resource.playerStand, -getOriginX(), -getOriginY(), 0, 0,
    			RADIUS*2, RADIUS*3, getScaleX(), getScaleY(), 0);
        batch.setTransformMatrix(old);
    }

	public void addFloorContact(Body body, Vector2 point) {
		floorContacts.put(body, new ContactInfo(Integer.MAX_VALUE, point));
	}

	public void removeFloorContact(Body body) {
		ContactInfo info = floorContacts.get(body);
		if (info != null) {
			info.ttl = CONTACT_TTL;
		}
	}

	private static class ContactInfo {
		private int ttl;
		private Vector2 p;
		ContactInfo(int ttl, Vector2 p) {
			this.ttl = ttl;
			this.p = p;
		}
	}

	@Override
	public void beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
		if (normal.y > 0.707f && !other.getFixtureList().get(0).isSensor()) {   //45 deg
			addFloorContact(other, contactPoint);
		}
	}

	@Override
	public void endContact(Body other) {
		removeFloorContact(other);
	}

	@Override
	public boolean remove() {
		boolean ret = super.remove();
		if (ret) {
			light.remove();
		}
		return ret;
	}

	@Override
	public Body getBody() {
		return body;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void toggleFlyMode() {
		flyMode = !flyMode;
	}
}
