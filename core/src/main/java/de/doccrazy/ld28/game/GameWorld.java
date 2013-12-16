package de.doccrazy.ld28.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.doccrazy.ld28.core.Resource;
import de.doccrazy.ld28.game.actor.BodypartActor;
import de.doccrazy.ld28.game.actor.CheckpointActor;
import de.doccrazy.ld28.game.actor.DeathPortalActor;
import de.doccrazy.ld28.game.actor.Floor;
import de.doccrazy.ld28.game.actor.LevelActor;
import de.doccrazy.ld28.game.actor.Player;
import de.doccrazy.ld28.game.actor.SubwayActor;
import de.doccrazy.ld28.game.base.ActorContactListener;
import de.doccrazy.ld28.game.base.ActorListener;
import de.doccrazy.ld28.game.base.Box2dActor;
import de.doccrazy.ld28.game.level.ElementType;
import de.doccrazy.ld28.game.level.Generator;

public class GameWorld {
    private static float PHYSICS_STEP = 1f/300f;

    public static final Vector2 GRAVITY = new Vector2(0, -9.8f);
    private static final Vector2 SPAWN = new Vector2(-4f, 4.5f);

    public final Stage stage; // stage containing game actors (not GUI, but actual game elements)
    public final World box2dWorld; // box2d world
    public final RayHandler rayHandler;

	private Player player;
	private float deltaCache;
	private ActorListener actorListener;

	private boolean doGenerate = false;
	private LevelActor currentLevel;
	private CheckpointActor rightmostCp;
	private float rightmost;
	private List<RemoveEntry> levelRemoveList = new ArrayList<>();

	private int checkpointIdx;
	private float maxDistance;

    public GameWorld() {
        box2dWorld = new World(GRAVITY, true);
        box2dWorld.setContactListener(new ActorContactListener());
        stage = new Stage(); // create the game stage
        rayHandler = new RayHandler(box2dWorld);

        createWorld();
    }

    public void reset() {
    	checkpointIdx = 0;
    	maxDistance = 0;

		player = null;
		currentLevel = null;
		rightmostCp = null;
		doGenerate = false;
		levelRemoveList.clear();
		stage.setKeyboardFocus(null);

		List<Actor> actors = Arrays.asList(stage.getActors().toArray());
		for (Actor actor : actors) {
    		actor.remove();
		}

		/*Array<Body> bodies = new Array<>();
		box2dWorld.getBodies(bodies);
		for (Body body : bodies) {
			if (box2dWorld.getBodyCount() > 0) {
				box2dWorld.destroyBody(body);
			}
		}*/
		createWorld();
    }

    private void createWorld() {
    	new Floor(this, 0f);

    	rightmost = -8.5f;
    	genBlock();
    	currentLevel.setActive(true);
    	rightmostCp.activate(false);

    	player = new Player(this, SPAWN);
    	stage.setKeyboardFocus(getPlayer());

    	/*pillar(-2, 0);
        pillar(0, 0);
        pillar(2, 0);
        pillar(4, 0);
        pillar(6, 0);
        pillar(8, 0);
        pillar(10, 0);
        create(ElementType.plank, 1, 5f);

        create(ElementType.lightPost, 12, 0f);
        create(ElementType.middle, 13, 0f);
        create(ElementType.middle2, 14, 0f);
        create(ElementType.pillarMedium, 16, 0f);
        create(ElementType.split, 18, 0f);
        create(ElementType.split2, 20, 0f);
        create(ElementType.top2, 24, 0f);*/

    	Resource.start.play();
    }

	private void pillar(float x, float y) {
		create(ElementType.pillarBottom, x, y);
        create(ElementType.pillarMiddle, x + 0.1f, y + 1.2f);
        create(ElementType.pillarMiddle, x + 0.1f, y + 1.8f);
        create(ElementType.pillarMiddle, x + 0.1f, y + 2.4f);
        create(ElementType.pillarTop, x - 0.3f, y + 3.0f);
	}

	private void create(ElementType type, float x, float y) {
		// 1. Create a BodyDef, as usual.
        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = BodyType.DynamicBody;

        // 2. Create a FixtureDef, as usual.
        FixtureDef fd = new FixtureDef();
        fd.density = 50;
        fd.friction = 0.8f;
        fd.restitution = 0.0f;

        Body pillar = box2dWorld.createBody(bd);
    	type.attach(pillar, fd);
	}

    public void update(float delta) {
    	deltaCache += delta;

    	if (doGenerate) {
    		doGenerate = false;

    		genBlock();
    	}
    	for (Iterator<RemoveEntry> it = levelRemoveList.iterator(); it.hasNext(); ) {
    		RemoveEntry entry = it.next();
    		if (player != null && player.getBody().getPosition().x > entry.at) {
    			entry.level.remove();
    			entry.cp.remove();
    			it.remove();
    		}
    	}

    	while (deltaCache >= PHYSICS_STEP) {
    		// perform game logic here
    		stage.act(PHYSICS_STEP); // update game stage
    		box2dWorld.step(PHYSICS_STEP, 6, 3); // update box2d world
    		deltaCache -= PHYSICS_STEP;
    	}

    	if (player != null) {
    		maxDistance = Math.max(maxDistance, player.getBody().getPosition().x - SPAWN.x);
    	}
    }

	private void genBlock() {
		if (currentLevel != null) {
			currentLevel.setActive(true);
			scheduleRemove(currentLevel, rightmostCp, rightmost + GameRenderer.UNIT_WIDTH);
		}

		rightmostCp = new CheckpointActor(this, rightmost + 2.5f, checkpointIdx * 0.2f);

		Generator gen = new Generator(this, rightmostCp.getRight(), 0f, 30f, 3f + checkpointIdx * 0.2f);
		gen.setDistance(1.0f + checkpointIdx*0.25f);
		rightmost = gen.generate().x;
		currentLevel = new LevelActor(this, gen.getLevelElements());
	}

	private void scheduleRemove(LevelActor level, CheckpointActor cp, float at) {
		for (RemoveEntry entry : levelRemoveList) {
			entry.level.setActive(false);
		}
		levelRemoveList.add(new RemoveEntry(level, cp, at));
	}

	public Player getPlayer() {
		return player;
	}

	public void spawnDeathPortal() {
		//player.getBody().setType(BodyType.StaticBody);
		final DeathPortalActor portal2 = new DeathPortalActor(this, 2f, 1f, false);
		new DeathPortalActor(this, -2f, 0f, true) {
			@Override
			public void onPortalOpened() {
				new SubwayActor(GameWorld.this, this, portal2);
			}
		};
	}

	public void setActorListener(ActorListener actorListener) {
		this.actorListener = actorListener;
	}

	public void onActorAdded(Box2dActor actor) {
		if (actorListener != null) {
			actorListener.actorAdded(actor);
		}
	}

	public void onActorRemoved(Box2dActor actor) {
		if (actorListener != null) {
			actorListener.actorRemoved(actor);
		}
	}

	public void killPlayer() {
		Vector2 pos = getPlayer().getBody().getPosition();
		if (player.remove()) {
			player = null;
			stage.setKeyboardFocus(null);
			for (int i = 0; i < 20; i++) {
				new BodypartActor(this, pos);
			}
			Resource.die.play();
		}
	}

	public void checkpointReached() {
		checkpointIdx++;
		doGenerate = true;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public int getCheckpointIdx() {
		return checkpointIdx;
	}

	public int getScore() {
		return (int) (getMaxDistance() * getCheckpointIdx());
	}

	private static class RemoveEntry {
		LevelActor level;
		CheckpointActor cp;
		float at;
		public RemoveEntry(LevelActor level, CheckpointActor cp, float at) {
			this.level = level;
			this.cp = cp;
			this.at = at;
		}
	}
}
