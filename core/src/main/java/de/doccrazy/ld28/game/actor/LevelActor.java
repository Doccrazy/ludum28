package de.doccrazy.ld28.game.actor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.GameWorld;
import de.doccrazy.ld28.game.base.Box2dActor;

public class LevelActor extends Box2dActor {
	private final static Array<Body> bodies = new Array<Body>();
	private final static Vector2[] vertices = new Vector2[1000];
	private final static Vector2[] verticesRaw = new Vector2[1000];
	private final static short[] indices = new short[1000];
	private final static float[] vertTmp = new float[5000];
	private final static float[] vertParts = new float[5000];

	private PolygonSpriteBatch shapeRenderer = new PolygonSpriteBatch();
	private EarClippingTriangulator triangulator = new EarClippingTriangulator();
	private List<Light> lights = new ArrayList<>();

	public LevelActor(GameWorld world) {
		super(world);

		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new Vector2();
		for (int i = 0; i < verticesRaw.length; i++)
			verticesRaw[i] = new Vector2();

		addLights();
	}

	private void addLights() {
		Vector2 vertex = new Vector2();
		world.box2dWorld.getBodies(bodies);
		for (Iterator<Body> iter = bodies.iterator(); iter.hasNext();) {
			Body body = iter.next();
			if (body.getUserData() != null) continue;
			Transform transform = body.getTransform();
			for (Fixture fixture : body.getFixtureList()) {
				if (fixture.getType() == Type.Polygon) {
					PolygonShape chain = (PolygonShape)fixture.getShape();
					int vertexCount = chain.getVertexCount();
					for (int i = 0; i < vertexCount; i++) {
						chain.getVertex(i, vertex);
						//transform.mul(vertex);
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
				2f, vertex.x, vertex.y);
		//light.setStaticLight(true);
		light.attachToBody(body, vertex.x, vertex.y);
		light.setXray(true);
		lights.add(light);
	}

	private void drawLevel() {
		world.box2dWorld.getBodies(bodies);
		for (Iterator<Body> iter = bodies.iterator(); iter.hasNext();) {
			Body body = iter.next();
			if (body.getUserData() == null) renderBody(body);
		}
	}

	private void renderBody(Body body) {
		Transform transform = body.getTransform();
		for (Fixture fixture : body.getFixtureList()) {
			drawShape(fixture, transform, Resource.tex);
		}
	}

	private void drawShape(Fixture fixture, Transform transform, Texture tex) {
		Vector2 rnd = new Vector2((float)Math.random(), (float)Math.random());
		if (fixture.getType() == Type.Polygon) {
			PolygonShape chain = (PolygonShape)fixture.getShape();
			int vertexCount = chain.getVertexCount();
			for (int i = 0; i < vertexCount; i++) {
				chain.getVertex(i, vertices[i]);
				chain.getVertex(i, verticesRaw[i]);
				transform.mul(vertices[i]);
			}
			drawSolidPolygon(vertices, verticesRaw, vertexCount, tex, rnd);
			shapeRenderer.flush();
			return;
		}
	}

	private void drawSolidPolygon (Vector2[] vertices, Vector2[] verticesRaw, int vertexCount, Texture tex, Vector2 texRnd) {
		for (int i = 0; i < vertexCount; i++) {
			Vector2 v = vertices[i];
			Vector2 raw = verticesRaw[i];
			vertParts[i*5] = v.x;
			vertParts[i*5+1] = v.y;
			vertParts[i*5+2] = new Color(1,1,1,1).toFloatBits();
			vertParts[i*5+3] = texRnd.x + raw.x/4f;
			vertParts[i*5+4] = texRnd.y + raw.y/4f;

			vertTmp[i*2] = v.x;
			vertTmp[i*2+1] = v.y;
			indices[i] = (short) i;
		}

		ShortArray idx = triangulator.computeTriangles(vertTmp, 0, vertexCount*2);
		shapeRenderer.draw(tex, vertParts, 0, vertexCount*5,
				idx.items, 0, Math.max(0, vertexCount - 2) * 3);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		/*for (Light light : lights) {
			light.setPosition(light.getBody().getPosition().x, light.getBody().getPosition().y);
		}*/

		batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin();
        drawLevel();
        shapeRenderer.end();
        batch.begin();
	}

	@Override
	public boolean remove() {
		boolean ret = super.remove();
		if (ret) {
			for (Light light : lights) {
				light.remove();
			}
		}
		return ret;
	}

}
