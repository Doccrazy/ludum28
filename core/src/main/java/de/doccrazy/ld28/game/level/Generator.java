package de.doccrazy.ld28.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import de.doccrazy.ld28.game.GameWorld;

public class Generator {
	private static float TOP_LAYER_HEIGHT = 0.5f;

	private float x0;
	private float y0;
	private float width;
	private float height;
	private float distance;

	private List<JoinPoint> openJoints;
	private List<Rectangle> rects;
	private GameWorld world;
	private List<Body> levelElements = new ArrayList<>();

	public Generator(GameWorld world, float x0, float y0, float width, float height) {
		this.world = world;
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return a possible spawnpoint (bottom center)
	 */
	public Vector2 generate() {
		levelElements.clear();
		Vector2 result = null;
		boolean hadFirstTop = false;
		float curx = x0;
		openJoints = new ArrayList<JoinPoint>();
		rects = new ArrayList<Rectangle>();

		List<ElementType> topEnds = new ArrayList<ElementType>();
		List<ElementType> botEnds = new ArrayList<ElementType>();
		List<ElementType> mids = new ArrayList<ElementType>();
		for (ElementType type : ElementType.values()) {
			boolean hasUp = false, hasDown = false;
			for (JoinPoint joint : type.getJoints()) {
				if (joint.faceUp) {
					hasUp = true;
				} else {
					hasDown = true;
				}
			}
			if (hasUp && !hasDown) {
				botEnds.add(type);
			} else if (!hasUp && hasDown) {
				topEnds.add(type);
			} else if (hasUp && hasDown) {
				mids.add(type);
			}
		}

		while (curx < x0 + width || openJoints.size() > 0) {
			if (openJoints.size() > 0) {
				JoinPoint leftJoint = openJoints.get(0), newJoint = null;
				ElementType el = null;
				Rectangle tmp = null;
				boolean placeMid = false;
				for (int i = 0; i < 10; i++) {
					if (leftJoint.faceUp) {
						placeMid = leftJoint.y < height - TOP_LAYER_HEIGHT;
						el = placeMid ? select(mids) : select(topEnds);
						newJoint = findJoint(el, false);
						tmp = new Rectangle(leftJoint.x - newJoint.x, leftJoint.y - newJoint.y, el.getWidth(), el.getHeight());
					} else {
						el = select(mids, botEnds);
						newJoint = findJoint(el, true);
						tmp = new Rectangle(leftJoint.x - newJoint.x, leftJoint.y - newJoint.y, el.getWidth(), el.getHeight());
					}
					if (collides(tmp, rects)) {
						el = null;
						tmp = null;
						continue;
					}
					break;
				}
				openJoints.remove(0);
				if (el != null) {
					/*if (!placeMid && !hadFirstTop) {
						hadFirstTop = true;
						result = new Vector2(tmp.x + tmp.width/2, tmp.y + tmp.height);
					}*/
					result = new Vector2(tmp.x + tmp.width, tmp.y + tmp.height);
					place(el, tmp);
					//System.out.println("Place: " + tmp.x + " " + tmp.y);
					for (JoinPoint joint : el.getJoints()) {
						if (joint != newJoint) {
							openJoints.add(new JoinPoint(joint.x + tmp.x, joint.y + tmp.y, joint.faceUp));
						}
					}
				}
			} else {
				ElementType el = select(botEnds);
				Rectangle tmp = new Rectangle();
				tmp.x = curx;
				tmp.y = y0;
				tmp.height = el.getHeight();
				tmp.width = el.getWidth();
				while (collides(tmp, rects)) {
					tmp.x += Math.random() * 0.25f;
				}
				place(el, tmp);
				for (JoinPoint joint : el.getJoints()) {
					openJoints.add(new JoinPoint(joint.x + tmp.x, joint.y + tmp.y, joint.faceUp));
				}
				curx = (float) (tmp.x + tmp.width + Math.random() * distance);
			}
			Collections.sort(openJoints, new Comparator<JoinPoint>() {
				@Override
				public int compare(JoinPoint o1, JoinPoint o2) {
					return Float.valueOf(o1.x).compareTo(o2.x);
				}
			});
			//System.out.println(curx);
		}
		return result;
	}

	private JoinPoint findJoint(ElementType el, boolean up) {
		for (JoinPoint pt : el.getJoints()) {
			if (pt.faceUp == up) {
				return pt;
			}
		}
		return null;
	}

	private void place(ElementType type, Rectangle tmp) {
		// 1. Create a BodyDef, as usual.
        BodyDef bd = new BodyDef();
        bd.position.set(tmp.x, tmp.y);
        bd.type = BodyType.DynamicBody;
        bd.active = false;

        // 2. Create a FixtureDef, as usual.
        FixtureDef fd = new FixtureDef();
        fd.density = 50;
        fd.friction = 0.8f;
        fd.restitution = 0f;

        Body pillar = world.box2dWorld.createBody(bd);
    	type.attach(pillar, fd);
    	rects.add(tmp);
    	levelElements.add(pillar);
	}

	private boolean collides(Rectangle tmp, List<Rectangle> rects) {
		for (Rectangle rect : rects) {
			if (rect.overlaps(tmp)) {
				return true;
			}
		}
		return false;

	}

	private ElementType select(List<ElementType> list) {
		return list.get((int) (Math.random()*list.size()));
	}

	private ElementType select(List<ElementType> list, List<ElementType> list2) {
		List<ElementType> all = new ArrayList<ElementType>();
		all.addAll(list);
		all.addAll(list2);
		return select(all);
	}

	public List<Body> getLevelElements() {
		return levelElements;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
}
