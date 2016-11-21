
package DotsAndBox;


import java.awt.Color;

public class ConnectionSprite extends Sprite {

	/*
	 *
	 *	ConnectionSprite is a sublcass of Sprite. There are two types of connections: vertical
	 *	connections between dots and horizontal connections between sprites. The static method
	 *	createConnection is a convenience method to create the ConnectionSprite at the proper
	 *	coordinates and build its shape.
	 *
	 */

    public static final int HORZ_CONN=1;
    public static final int VERT_CONN=2;

    boolean connectionMade;	// Tracks wether the ConnectionSprite has been clicked on

    public ConnectionSprite() {
    	// Initialize all the fields
        super();

        connectionMade=false;
        color=Color.WHITE;
    }

    public static ConnectionSprite createConnection(int type, int x, int y) {
    	ConnectionSprite conn=new ConnectionSprite();

        if(type==ConnectionSprite.HORZ_CONN) {
        	conn.width=Dots.DOT_GAP;
        	conn.height=Dots.DOT_SIZE;
        } else if(type==ConnectionSprite.VERT_CONN) {
        	conn.width=Dots.DOT_SIZE;
        	conn.height=Dots.DOT_GAP;
        } else {
        	return null;
        }

        conn.x=x;
        conn.y=y;

        conn.shape.addPoint(-conn.width/2, -conn.height/2);
        conn.shape.addPoint(-conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, -conn.height/2);

        return conn;
    }
}
