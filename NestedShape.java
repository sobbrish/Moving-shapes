/*
 * ==========================================================================================
 * NestedShape.java : A NestedShape(a shape that contains other shapes).
 * 
 * ==========================================================================================
 */
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;

class NestedShape extends RectangleShape{
    private ArrayList<Shape> innerShapes = new ArrayList<>();
    public Shape createInnerShape(PathType pt, ShapeType st){
        Shape innerShape = null;
        switch (st){
           case RECTANGLE:
            innerShape = new RectangleShape(0,0,width/5,height/5,width,height,getColor(),getBorderColor(),pt);
            break;
           case OVAL:
            innerShape = new OvalShape(0,0,width/5,height/5,width,height,getColor(),getBorderColor(),pt);
            break;
           case NESTED:
            innerShape = new NestedShape(0,0,width/5,height/5,width,height,getColor(),getBorderColor(),pt);
            break;
        }
        innerShape.setParent(this);
        innerShapes.add(innerShape);
        return innerShape;
    }
    public NestedShape(){
        super();
        createInnerShape(PathType.BOUNCING, ShapeType.RECTANGLE);
    }
    public NestedShape(int x, int y, int width, int height, int pw, int ph, Color c, Color bc, PathType pt){
        super(x, y, width, height, pw, ph, c, bc, pt);
        createInnerShape(PathType.BOUNCING, ShapeType.RECTANGLE);
    }
    public NestedShape(int width, int height){
        super(0, 0, width, height, Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT, Shape.DEFAULT_COLOR, Shape.DEFAULT_BORDER_COLOR, PathType.BOUNCING);
    }
    public Shape getInnerShapeAt(int index){
        return innerShapes.get(index);
    }
    public int getSize(){
        return innerShapes.size();
    }
    public void draw(Graphics g){
        g.setColor(color.BLACK);
        g.drawRect(x,y,width,height);
        g.translate(x, y);
        for(Shape i:innerShapes){
            i.draw(g);
            if(i.isSelected()){
                i.drawHandles(g);
            }
            i.drawString(g);

        }
        g.translate(-x, -y);
    }
    public void move(){
            super.move();
            for(Shape i:innerShapes){
                i.move();
            }
    }
    public int indexOf(Shape s){
        return innerShapes.indexOf(s);
    }
    public void addInnerShape(Shape s){
        s.setParent(this);
        innerShapes.add(s);
    }
    public void removeInnerShape(Shape s){
        innerShapes.remove(s);
        s.setParent(null);
    }
    public void removeInnerShapeAt(int index){
        Shape shape = innerShapes.get(index);
        innerShapes.remove(shape);
        shape.setParent(null);
    }
    public ArrayList<Shape> getAllInnerShapes(){
        return innerShapes;
    }


}
