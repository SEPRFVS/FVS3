package com.fvs.taxe.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fvs.taxe.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.targetWindowsWidth;
		config.height = Game.targetWindowsHeight;
		config.title = "TaxE";
		config.resizable = false;
		config.addIcon("icon/fvs256.png", FileType.Internal);
		config.addIcon("icon/fvs128.png", FileType.Internal);
		config.addIcon("icon/fvs64.png", FileType.Internal);
		config.addIcon("icon/fvs32.png", FileType.Internal);
		config.addIcon("icon/fvs16.png", FileType.Internal);
		new LwjglApplication(new Game(), config);
	}
}
