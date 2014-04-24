package de.doccrazy.ld28.game.actor;

import java.util.List;

import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;
import de.doccrazy.ld28.game.base.PolyRenderer;

public class LevelActor extends Box2dActor {
	private List<Body> levelElements;

	public LevelActor(GameWorld world, List<Body> levelElements) {
		super(world);
		this.levelElements = levelElements;

		addLights();
	}

	private void addLights() {
		Vector2 vertex = new Vector2();
		for (Body body : levelElements) {
			if (body.getUserData() != null) continue;
			for (Fixture fixture : body.getFixtureList()) {
				if (fixture.getType() == Type.Polygon) {
					PolygonShape chain = (PolygonShape)fixture.getShape();
					int vertexCount = chain.getVertexCount();
					for (int i = 0; i < vertexCount; i++) {
						chain.getVertex(i, vertex);
						if (Math.random() > 0.9) {
							createLight(body, vertex);
						}
					}
				}
			}
		}
	}

	private void createLight(Body body, Vector2 vertex) {
		PointLight light = new PointLight(world.rayHandler, 10,
				new Color((float)Math.random()*0.5f, (float)Math.random()*0.5f, (float)Math.random()*0.5f, (float)Math.random()),
				(float)(1f + Math.random()), vertex.x, vertex.y);
		//light.setStaticLight(true);
		light.attachToBody(body, vertex.x, vertex.y);
		light.setXray(true);
		light.setActive(false);
		lights.add(light);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		/*for (Light light : lights) {
			light.setPosition(light.getBody().getPosition().x, light.getBody().getPosition().y);
		}*/

		batch.end();
		PolyRenderer.drawBodies(levelElements, batch.getProjectionMatrix());
        batch.begin();
	}

	@Override
	public boolean remove() {
		boolean ret = super.remove();
		if (ret) {
			for (Body el : levelElements) {
				world.box2dWorld.destroyBody(el);
			}
		}
		return ret;
	}

	public void setActive(boolean active) {
		for (Body body : levelElements) {
			body.setActive(active);
		}
		for (Light light : lights) {
			light.setActive(active);
		}
	}

}
