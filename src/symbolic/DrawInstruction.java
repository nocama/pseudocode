package symbolic;

import java.awt.Color;
import java.awt.Graphics;

public class DrawInstruction extends Instruction {

	public static final int CIRCLE = 1;
	public static final int SQUARE = 2;
	public static final int OVAL = 3;
	public static final int RECTANGLE = 4;
	
	int type = 0;
	double x = 0;
	double y = 0;
	double width = 50;
	double height = 50;
	Color color;
	
	public DrawInstruction(String name) {
		if (name.equals("circle"))
			type = CIRCLE;
		if (name.equals("square"))
			type = SQUARE;
		if (name.equals("oval"))
			type = OVAL;
		if (name.equals("rectangle"))
			type = RECTANGLE;
		
		color = Color.WHITE;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setSize(double size) {
		this.width = size;
		this.height = size;
	}
	
	public void setRadius(double radius) {
		this.width = this.height = radius * 2;
	}
	
	public void setDiameter(double diameter) {
		this.width = this.height = diameter;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(color);
		switch (type) {
		case CIRCLE:
		case OVAL:
			g.fillOval((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
			break;
		case SQUARE:
		case RECTANGLE:
			g.fillRect((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
			break;
		}
	}

	public String toString() {
		switch (type) {
		case CIRCLE: return "draw a circle at (" + x + ", " + y + ") of radius " + width / 2;
		case SQUARE: return "draw a circle at (" + x + ", " + y + ") of radius " + width / 2;
		case OVAL: return "draw a circle at (" + x + ", " + y + ") of radius " + width / 2;
		case RECTANGLE: return "draw a circle at (" + x + ", " + y + ") of radius " + width / 2;
		}
		return "bad instruction";
	}
	
}
