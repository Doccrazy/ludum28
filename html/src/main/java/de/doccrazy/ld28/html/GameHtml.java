package de.doccrazy.ld28.html;

import de.doccrazy.ld28.core.Game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Game();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
