package com.fvs.taxe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fvs.taxe.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.targetWindowsWidth;
		config.height = Game.targetWindowsHeight;
		config.title = "TaxE";
		new LwjglApplication(new Game(), config);
	}
}
