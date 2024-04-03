

/*
 *	===============================================================================
 *	RectangleShape.java : A shape that is a rectangle.
 *  YOUR UPI: hxza301
 *	=============================================================================== */
import java.awt.*;
class RectangleShape extends Shape {
	public RectangleShape() {}
    public RectangleShape(Color c, Color bc, PathType pt) {super(c, bc, pt);}
	public RectangleShape(int x, int y, int w, int h, int mw, int mh, Color c, Color bc, PathType pt) {
		super(x ,y ,w, h ,mw ,mh, c, bc, pt);
	}
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(borderColor);
		g.drawRect(x, y, width, height);
	}
	public boolean contains(Point mousePt) {
		return (x <= mousePt.x && mousePt.x <= (x + width + 1)	&&	y <= mousePt.y && mousePt.y <= (y + height + 1));
	}
}