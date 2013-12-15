package de.doccrazy.ld28.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.doccrazy.ld28.core.Resource;

public class DeathLabel extends Label {

	public DeathLabel() {
		super("You died!", new LabelStyle(Resource.fontBig, new Color(1f, 0.4f, 0.3f, 0.7f)));

		setPosition(230, 300);
	}

}
