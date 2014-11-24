package com.turkishdelight.taxe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent extends Sprite{
	//All sprites are by default places at z order of 0
	private int z = 0;
	private Scene parentScene;
	public SpriteComponent(Scene parentScene, Texture text, int z)
	{
		super(text);
		this.z = z;
		this.parentScene = parentScene;
	}
	
	public int getZ()
	{
		return z;
	}
	
	public void setZ(int newZ)
	{
		z = newZ;
		//We indicate that the spriteComponents need to be reordered in the Component batch
		parentScene.postSpriteReorder();
	}

}
