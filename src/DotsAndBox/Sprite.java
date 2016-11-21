/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DotsAndBox;


import java.awt.Graphics;

import java.awt.Polygon;
import java.awt.Color;


public class Sprite {

	/*
	 *
	 *	Sprite is the basic object that is drawn onto the screen. The dots
	 *	are all Sprite objects. ConnectionSprite and BoxSprite are subclasses
	 *	of Sprite. Sprite has a method check to see if a point is within
	 *	the drawn object. Sprite also has method to draw the Sprite to the screen.
	 *
	 */

    Polygon shape;	//	The shape that is to be drawn
    Color color;	//	The color of the shape
    int width;		//	Width of the Sprite
    int height;		//	Height of the Sprite
    int x;			//	Horizontal coordinate of the center of the sprite
    int y;			//	Vertical coordinate of the center of the sprite

    public Sprite() {
    	//	Initialize all the fields
        shape=new Polygon();
        width=0;
        height=0;
        x=0;
        y=0;
        color=Color.BLACK;
    }

    public void render(Graphics g) {
    	//	The render method is responsible for positioning the sprite at the proper location

        g.setColor(color);

        Polygon renderedShape=new Polygon();
        for(int i=0; i<shape.npoints; i++) {
            int renderedx=shape.xpoints[i] + x + width / 2;
            int renderedy=shape.ypoints[i] + y + height / 2;
            renderedShape.addPoint(renderedx, renderedy);
        }
        g.fillPolygon(renderedShape);
    }

    public boolean containsPoint(int x, int y) {
    	//	This returns true only if the point (x, y) is contained within the visible shape of the sprite

    	return shape.contains(x - this.x - width /2, y - this.y - height /2);
    }
}
