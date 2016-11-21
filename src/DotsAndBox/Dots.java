

package DotsAndBox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.Color;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Dots extends JFrame implements MouseMotionListener, MouseListener {

    public  int DOT_NUMBER=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter the number of dots in Rows and Colums: "));	//	The number of dots on each side of the square game board
    public static final int DOT_GAP=24;		//	The space between each dot
    public static final int DOT_SIZE=4;		//	The length of the sides of the square dot

    public static final int PLAYER_ONE=1;
    public static final int PLAYER_TWO=2;

    public static final Color PLAYER_ONE_COLOR=Color.YELLOW;	//	The color of player1's boxes
    public static final Color PLAYER_TWO_COLOR=Color.GREEN;		// 	The color of player2's boxes



  private ConnectionSprite[] horizontalConnections;	//	Array for all the ConnectionSprites that horizontally connect dots
    private ConnectionSprite[] verticalConnections;		//	Array for all the ConnectionSprites that vertically connect dots
    private BoxSprite[] boxes;	//	Array for all the BoxSprites
    private Sprite[] dots;		//	Array for all the dots

    private Dimension dim;		//	Window dimensions

    private int clickx;		//	Holds the x coordinate of mouse click
    private int clicky;		// 	Holds the y coordinate of mouse click

    private int mousex;		// 	Holds the x coordinate of the mouse location
    private int mousey; 	// 	Holds the y coordinate of the mouse location

    private int centerx;	//	x coordinate of the center of the gameboard
    private int centery; 	// 	y coordinate of the center of the gameborad

    private int side;	//	Length of the sides of the square gameboard
    private int space;	// Length of 1 dot + 1 connection

    private int activePlayer;	// 	Holds the current player

    public Dots() {
        super("Connect the Dots");

        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);

        loadProperties();
        loadDots();

        startNewGame();

        setVisible(true);
    }

    private void loadProperties() {
    	//	Initialize fields

        clickx=0;
        clicky=0;
        mousex=0;
        mousey=0;

        dim=getSize();
        centerx=dim.width/2;
        centery=(dim.height - 100) /2;

        side=DOT_NUMBER * DOT_SIZE + (DOT_NUMBER - 1) * DOT_GAP;	//	There is one less connection than dot per side
    	space=DOT_SIZE + DOT_GAP;
    }

    private void loadConnections() {

        horizontalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
        verticalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];

        /*
         *
         *	There are two ways to cycle through the Connections, Boxes, and Dots grids. This way uses only 1 for
         *	loop and keeps track of the current row and column number in colsx, rowsx, colsy, rowsy. colsx and rowsx
         *	track the columns and rows for the horizontalConnections while colsy and rowsy track the columns and
         *	rows for the vertical connections. The reason to have different fields for vertical and horizontal
         *	connections is so that both grids will be filled in left to right and then top to bottom (rows first
         *	then columns). This makes it easier to match the connection up to box or boxes it borders. Simple setting
         *	colsy=rowsx and rowsy=colsx will put the vertical connections on the correct place on the screen
         *	but they won't match up to the boxes correctly.
         *
         */

        for(int i=0; i<horizontalConnections.length; i++) {
        	int colsx=i % (DOT_NUMBER-1);
        	int rowsx=i / (DOT_NUMBER-1);
        	int horx=centerx - side / 2 + DOT_SIZE + colsx * space;
        	int hory=centery - side / 2 + rowsx * space;
        	horizontalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.HORZ_CONN, horx, hory);

        	int colsy=i % DOT_NUMBER;
        	int rowsy=i / DOT_NUMBER;
        	int vertx=centerx - side / 2 + colsy * space;
        	int verty=centery - side / 2 + DOT_SIZE + rowsy * space;
        	verticalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.VERT_CONN, vertx, verty);
        }
    }

    private void loadBoxes() {

    	/*
    	 *
    	 *	loadBoxes cycles through the box grid the way loadConnection does. There is oneless box per side
    	 *	than dot per side.
    	 *
    	 */

    	boxes=new BoxSprite[(DOT_NUMBER-1) * (DOT_NUMBER-1)];

    	for(int i=0; i<boxes.length; i++) {
    		int cols=i % (DOT_NUMBER-1);
    		int rows=i / (DOT_NUMBER-1);

    		int boxx=centerx - side / 2 + DOT_SIZE + cols * space;
    		int boxy=centery - side / 2 + DOT_SIZE + rows * space;

    		ConnectionSprite[] horConn=new ConnectionSprite[2];
    		horConn[0]=horizontalConnections[i];
    		horConn[1]=horizontalConnections[i + (DOT_NUMBER - 1)];

    		ConnectionSprite[] verConn=new ConnectionSprite[2];		//	This only works if the verticalConnections were put into the array rows then columns
    		verConn[0]=verticalConnections[i + rows];
    		verConn[1]=verticalConnections[i + rows + 1];

    		boxes[i]=BoxSprite.createBox(boxx, boxy, horConn, verConn);
    	}
    }

    private void loadDots() {

		/*
		 *
		 *	loadDots cycles through the dot grid differently than the loadConnections and loadBoxes methods
		 *	cycle through the connections and boxes grids. The loadDots cycles through the dot grid with two
		 *	for loops. It doesn't matter what order the dots are loaded into the dots array since they are for
		 *	visual purposes only. The body of the loop also contains the code to actually build the dots shape.
		 *
		 */

        dots=new Sprite[DOT_NUMBER * DOT_NUMBER];
        for(int rows=0; rows<DOT_NUMBER; rows++) {
            for(int cols=0; cols<DOT_NUMBER; cols++) {
                Sprite dot=new Sprite();
                dot.width=DOT_SIZE;
                dot.height=DOT_SIZE;
                dot.x=centerx - side/2 + cols * space;
                dot.y=centery - side/2 + rows * space;
                dot.shape.addPoint(-DOT_SIZE/2, -DOT_SIZE/2);
                dot.shape.addPoint(-DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, DOT_SIZE/2);
                dot.shape.addPoint(DOT_SIZE/2, -DOT_SIZE/2);
                int index=rows * DOT_NUMBER + cols;
                dots[index]=dot;
            }
        }
    }

    private void startNewGame() {
    	activePlayer=PLAYER_ONE;
    	loadConnections();
        loadBoxes();
    }

    private ConnectionSprite getConnection(int x, int y) {

    	// Get the connection that encloses point (x, y) or return null if there isn't one

    	for(int i=0; i<horizontalConnections.length; i++) {
    		if(horizontalConnections[i].containsPoint(x, y)) {
    			return horizontalConnections[i];
    		}
    	}

    	for(int i=0; i<verticalConnections.length; i++) {
    		if(verticalConnections[i].containsPoint(x, y)) {
    			return verticalConnections[i];
    		}
    	}

    	return null;
    }

    private boolean[] getBoxStatus() {
    	boolean[] status=new boolean[boxes.length];

    	for(int i=0; i<status.length; i++) {
    		status[i]=boxes[i].isBoxed();
    	}

    	return status;
    }

    private int[] calculateScores() {
    	int[] scores={0, 0};

    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed() && boxes[i].player!=0) {
    			scores[boxes[i].player - 1]++;
    		}
    	}

    	return scores;
    }

    private boolean makeConnection(ConnectionSprite connection) {
    	boolean newBox=false;

    	boolean[] boxStatusBeforeConnection=getBoxStatus();	//	The two boolean arrays are used to see if a new box was created after the connection was made

    	connection.connectionMade=true;

    	boolean[] boxStatusAfterConnection=getBoxStatus();

    	for(int i=0; i<boxes.length; i++) {
    		if(boxStatusAfterConnection[i]!=boxStatusBeforeConnection[i]) {
    			newBox=true;
    			boxes[i].player=activePlayer;
    		}
    	}

    	if(!newBox) {	//	Allow the current player to go again if he made a box
    		if(activePlayer==PLAYER_ONE)
    			activePlayer=PLAYER_TWO;
    		else
    			activePlayer=PLAYER_ONE;
    	}

    	checkForGameOver();

    	return newBox;
    }

    private void checkForGameOver() {
    	int[] scores=calculateScores();
    	if((scores[0] + scores[1])==((DOT_NUMBER - 1) * (DOT_NUMBER - 1))) {
    		JOptionPane.showMessageDialog(this, "Player1: " + scores[0] + "\nPlayer2: " + scores[1], "Game Over", JOptionPane.PLAIN_MESSAGE);
    		startNewGame();
    		repaint();
    	}
    }

    private void handleClick() {
    	ConnectionSprite connection=getConnection(clickx, clicky);
    	if(connection==null)
    		return;

    	if(!connection.connectionMade) {
    		makeConnection(connection);

    	}

    	repaint();
    }

    public void mouseMoved(MouseEvent event) {
    	mousex=event.getX();
    	mousey=event.getY();
    	repaint();
    }

    public void mouseDragged(MouseEvent event) {
    	mouseMoved(event);
    }

    public void mouseClicked(MouseEvent event) {
    	clickx=event.getX();
    	clicky=event.getY();

    	handleClick();
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    private void paintBackground(Graphics g) {
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, dim.width, dim.height);
    }

    private void paintDots(Graphics g) {
    	for(int i=0; i<dots.length; i++) {
    		dots[i].render(g);
    	}
    }

    private void paintConnections(Graphics g) {
    	for(int i=0; i<horizontalConnections.length; i++) {

    		if(!horizontalConnections[i].connectionMade && activePlayer==PLAYER_ONE) {
    			if(horizontalConnections[i].containsPoint(mousex, mousey)) {
    				horizontalConnections[i].color=Color.YELLOW;
                                
                                
    			} else {
    				horizontalConnections[i].color=Color.WHITE;
    			}
                        
    		} else if(!horizontalConnections[i].connectionMade && activePlayer==PLAYER_TWO) {
    			if(horizontalConnections[i].containsPoint(mousex, mousey)) {
    				horizontalConnections[i].color=Color.GREEN;
    			} else {
    				horizontalConnections[i].color=Color.WHITE;
    			}
                }
//                else { horizontalConnections[i].color=Color.YELLOW;
//                }

    		horizontalConnections[i].render(g);
    	}

    	for(int i=0; i<verticalConnections.length; i++) {

    		if(!verticalConnections[i].connectionMade && activePlayer==PLAYER_ONE) {
    			if(verticalConnections[i].containsPoint(mousex, mousey)) {
                            if(activePlayer==PLAYER_ONE){
    				verticalConnections[i].color=Color.YELLOW;
                            }
                            
    			} else {
    				verticalConnections[i].color=Color.WHITE;
    			}
    		} else if(!verticalConnections[i].connectionMade && activePlayer==PLAYER_TWO) {
    			if(verticalConnections[i].containsPoint(mousex, mousey)) {
    				verticalConnections[i].color=Color.GREEN;
    			} else {
    				verticalConnections[i].color=Color.WHITE;
    			}
                }
//                } else {
//                    verticalConnections[i].color=Color.YELLOW;
//                    
//                         }

    		verticalConnections[i].render(g);
        
        }}

    public void paintBoxes(Graphics g) {
    	for(int i=0; i<boxes.length; i++) {
    		if(boxes[i].isBoxed()) {
    			if(boxes[i].player==PLAYER_ONE) {
    				boxes[i].color=PLAYER_ONE_COLOR;
    			} else if(boxes[i].player==PLAYER_TWO) {
    				boxes[i].color=PLAYER_TWO_COLOR;
    			}
    		} else {
    			boxes[i].color=Color.WHITE;
    		}

    		boxes[i].render(g);
    	}
    }

    public void paintStatus(Graphics g) {
    	int[] scores=calculateScores();
    	String status="It is player" + activePlayer + "'s turn";
    	String status2="Player 1: " + scores[0];
    	String status3="Player 2: " + scores[1];

    	//Color currentColor=(activePlayer==PLAYER_ONE) ? PLAYER_ONE_COLOR : PLAYER_TWO_COLOR ;
    	//g.setColor(currentColor);
    	g.setColor(Color.BLACK);
    	g.drawString(status, 10, dim.height-50);

    	g.setColor(PLAYER_ONE_COLOR);
    	g.drawString(status2, 10, dim.height-35);

    	g.setColor(PLAYER_TWO_COLOR);
    	g.drawString(status3, 10, dim.height-20);
    }

    public void update(Graphics g) {
    	paint(g);
    }

    public void paint(Graphics g) {
    	//	The double buffer technique is not really necessarry because there is no animation

    	Image bufferImage=createImage(dim.width, dim.height);
    	Graphics bufferGraphics=bufferImage.getGraphics();

    	paintBackground(bufferGraphics);
    	paintDots(bufferGraphics);
    	paintConnections(bufferGraphics);
    	paintBoxes(bufferGraphics);
    	paintStatus(bufferGraphics);

    	g.drawImage(bufferImage, 0, 0, null);
    }

    public static void main(String[] args) {


    	new Dots();
    }
}