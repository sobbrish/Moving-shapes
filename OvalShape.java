

/*
 *	===============================================================================
 *	OvalShape.java : A shape that is an oval.
 *  YOUR UPI: hxza301
 *	=============================================================================== */
import java.awt.*;
class OvalShape extends Shape {
	public OvalShape() {}
	public OvalShape(Color c, Color bc, PathType pt) {super(c, bc, pt);}
	public OvalShape(int x, int y, int w, int h, int mw, int mh, Color c, Color bc, PathType pt) {
		super(x ,y ,w, h ,mw ,mh, c, bc, pt);
	}
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, width, height);
		g.setColor(borderColor);
		g.drawOval(x, y, width, height);
	}
	public boolean contains(Point mousePt) {
		double dx, dy;
		Point EndPt = new Point(x + width, y + height);
		dx = (2 * mousePt.x - x - EndPt.x) / (double) width;
		dy = (2 * mousePt.y - y - EndPt.y) / (double) height;
		return dx * dx + dy * dy < 1.0;
	}
}