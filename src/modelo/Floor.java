package modelo;

import java.awt.Image;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class Floor implements Drawable{
	
	
	private int x;
	int y;
	private boolean checked;
	private Image dark;
	private Image seen;
	Floor(int x, int y, Image pic, Image pic2){
		this.x=x;
		this.y=y;
		this.setChecked(false);
		this.dark = pic;
		this.seen = pic2;
		//this.color = Color.black;
	};
	
	public void draw(SimGraphics g) {
		if(isChecked())
			g.drawImageToFit(seen);
		else
			g.drawImageToFit(dark);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		//this.color = Color.DARK_GRAY;
	}
	
}
