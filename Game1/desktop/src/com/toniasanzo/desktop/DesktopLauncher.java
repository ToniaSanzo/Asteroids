/**
 * This Game was written following along too foreignguymike's LibGDX tutorial
 */
package com.toniasanzo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.toniasanzo.Game1;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Asteroids"; // Set title
		config.width = 500;         // Set width
		config.height = 400;        // Set height
		config.useGL30 = false;     // Not using GL30
		config.resizable = false;   // User's not going to be able to resize the window


		new LwjglApplication(new Game1(), config);
	}
}
