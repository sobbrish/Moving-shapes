

/* ==============================================
 *  Shape.java : The superclass of all shapes.
 *  A shape defines various properties, including selected, colour, width and height.
 *  YOUR UPI: hxza301
 *  ===============================================================================
 */
import java.awt.*;

abstract class Shape {
    public static final PathType DEFAULT_PATHTYPE = PathType.BOUNCING;
    public static final ShapeType DEFAULT_SHAPETYPE = ShapeType.RECTANGLE;
    public static final int DEFAULT_X = 0, DEFAULT_Y = 0, DEFAULT_WIDTH=50, DEFAULT_HEIGHT=50, DEFAULT_PANEL_WIDTH=600, DEFAULT_PANEL_HEIGHT=600;
    public static final Color DEFAULT_COLOR=Color.red, DEFAULT_BORDER_COLOR=Color.blue;
    public int x, y, width=DEFAULT_WIDTH, height=DEFAULT_HEIGHT, panelWidth=DEFAULT_PANEL_WIDTH, panelHeight=DEFAULT_PANEL_HEIGHT; // the bouncing area
    protected Color color = DEFAULT_COLOR, borderColor =DEFAULT_BORDER_COLOR ;
    protected boolean selected = false;    // draw handles if selected
    protected MovingPath path = new BouncingPath(1, 2);
	public static final String DEFAULT_LABEL = "0";
    private static int numberOfShapes = 0;
    protected String label = DEFAULT_LABEL;
    protected NestedShape parent;


    public Shape() {this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT, DEFAULT_COLOR, DEFAULT_BORDER_COLOR, DEFAULT_PATHTYPE); }
	public Shape(Color c, Color bc, PathType pt) {
		this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_PANEL_WIDTH, DEFAULT_PANEL_HEIGHT, c, bc, pt);
	}
    public Shape(int x, int y, int w, int h, int pw, int ph, Color c, Color bc, PathType pt) {
        this.x = x;
        this.y = y;
        panelWidth = pw;
        panelHeight = ph;
        width = w;
        height = h;
        color = c;
        borderColor = bc;
        label = "" + ++numberOfShapes;
		switch (pt) {
			case BOUNCING : {
				path = new BouncingPath(1, 2);
				break;
			} case DOWN: {
				path = new DownPath(2);
				break;
			}
		}
    }
    public NestedShape getParent(){
        return parent;
    }
    public void setParent(NestedShape s){
        parent = s;
    }
    public Shape[] getPath(){
        return getPathToRoot(this,0);
    }
    public Shape[] getPathToRoot(Shape aShape, int depth) {
        Shape[] returnShapes;
        if (aShape == null) {
          if(depth == 0) return null;
          else returnShapes = new Shape[depth];
        }
        else {
          depth++;
          returnShapes = getPathToRoot(aShape.getParent(), depth);
          returnShapes[returnShapes.length - depth] = aShape;
        }
        return returnShapes;
    }
	public String getLabel() { return this.label; }
	public void setLabel(String t) { this.label = t; }
	public void drawString(Graphics g) {
		g.setColor(Color.black);
	  	g.drawString("" + label, x, y);
	}
    public String toString() {
		return String.format("%s,%s,%dx%d,%s,%dx%d", this.getClass().getName(),path.getClass().getSimpleName(), width, height, label, panelWidth, panelHeight);
	}
    public void move() {
        path.move();
    }
    public abstract boolean contains(Point p);
    public abstract void draw(Graphics g);
    public int getX() { return this.x; }
	public void setX(int x) { this.x = x; }
    public int getY() { return this.y;}
	public void setY(int y) { this.y = y; }
	public int getWidth() { return width; }
	public void setWidth(int w) { if (w < DEFAULT_PANEL_WIDTH && w > 0) width = w; }
	public int getHeight() {return height; }
	public void setHeight(int h) { if (h < DEFAULT_PANEL_HEIGHT && h > 0) height = h; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean s) { selected = s; }
	public Color getColor() { return color; }
    public void setColor(Color fc) { color = fc; }
	public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color bc) { borderColor = bc; }
    public void resetPanelSize(int w, int h) {
		panelWidth = w;
		panelHeight = h;
	}
    public void drawHandles(Graphics g) {
	 if (isSelected()) {
		 g.setColor(Color.black);
		 g.fillRect(x -2, y-2, 4, 4);
		 g.fillRect(x + width -2, y + height -2, 4, 4);
		 g.fillRect(x -2, y + height -2, 4, 4);
		 g.fillRect(x + width -2, y-2, 4, 4);
	  }
    }

    /* Inner class ===================================================================== Inner class
     *    MovingPath : The superclass of all paths. It is an inner class.
     *    A path can change the current position of the shape.
     *    =============================================================================== */
    abstract class MovingPath {
        protected int deltaX, deltaY; // moving distance
        public MovingPath(int dx, int dy) { deltaX=dx; deltaY=dy; }
        public MovingPath() { }
        public abstract void move();
        public String toString() { return getClass().getSimpleName(); };
    }
    class BouncingPath extends MovingPath {
        public BouncingPath(int dx, int dy) {
            super(dx, dy);
         }
        public void move() {
             x = x + deltaX;
             y = y + deltaY;
             if ((x < 0) && (deltaX < 0)) {
                 deltaX = -deltaX;
                 x = 0;
             }
             else if ((x + width > panelWidth) && (deltaX > 0)) {
                 deltaX = -deltaX;
                 x = panelWidth - width;
             }
             if ((y< 0) && (deltaY < 0)) {
                 deltaY = -deltaY;
                 y = 0;
             }
             else if((y + height > panelHeight) && (deltaY > 0)) {
                 deltaY = -deltaY;
                 y = panelHeight - height;
             }
        }
    }
    class DownPath extends MovingPath {
		public DownPath(int dy) {
			deltaY = dy;
		}
		public void move() {
			y += deltaY;
			if ((y + height > panelHeight) && (deltaY > 0))
				y = 0;
		}
	}
}