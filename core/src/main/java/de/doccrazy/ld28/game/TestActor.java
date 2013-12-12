package de.doccrazy.ld28.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.doccrazy.ld28.core.Resource;

public class TestActor extends Actor {
	float elapsed;

	public TestActor() {
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		elapsed += delta;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Resource.libgdxLogo, 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed));
	}
}
