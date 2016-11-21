/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DotsAndBox;

import java.awt.Color;




public class BoxSprite extends Sprite {

	/*
	 *
	 *	BoxSprite is a subclass of Sprite. BoxSprites represent the actual boxes made up by the Dot
	 *	Sprites and ConnectionSprites. BoxSprite contains references to the four ConnectionSprites
	 *	which make up its borders. The isBoxed method returns true when all four of the border
	 *	ConnectionSprites have true connectionMade fields. BoxSprites should be created using the
	 *	static createBox method.
	 *
	 */

	ConnectionSprite[] horizontalConnections;	//	The ConnectionSprites that are the top and bottom borders of the box
	ConnectionSprite[] verticalConnections;		//	The ConnectionSprites that are the left and right borders of the box

	int player;	//	Tracks the player that closed the box

	public BoxSprite() {
		super();

		color=Color.WHITE;	//	Initially the box should be the same color as the background

     
                horizontalConnections=new ConnectionSprite[2];
		verticalConnections=new ConnectionSprite[2];

		width=Dots.DOT_GAP;
		height=Dots.DOT_GAP;

		shape.addPoint(-width/2, -height/2);
        shape.addPoint(-width/2, height/2);
        shape.addPoint(width/2, height/2);
        shape.addPoint(width/2, -height/2);
	}

	public boolean isBoxed() {
		boolean boxed=true;

		for(int i=0; i<2; i++) {
			if(!horizontalConnections[i].connectionMade || !verticalConnections[i].connectionMade) {
				boxed=false;
			}
		}

		return boxed;
	}

	public static BoxSprite createBox(int x, int y, ConnectionSprite[] horizontalConnections, ConnectionSprite[] verticalConnections) {
		BoxSprite box=new BoxSprite();
		box.player=0;
		box.x=x;
		box.y=y;

		box.horizontalConnections=horizontalConnections;
		box.verticalConnections=verticalConnections;
		return box;
	}
}