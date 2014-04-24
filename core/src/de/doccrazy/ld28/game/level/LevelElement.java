package de.doccrazy.ld28.game.level;

import com.badlogic.gdx.math.Vector2;

public abstract class LevelElement {
	protected Vector2 pos = new Vector2();
	protected Vector2 size = new Vector2();
	protected ElementType type;

	public LevelElement() {
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public ElementType getType() {
		return type;
	}

	public void setType(ElementType type) {
		this.type = type;
	}

}
