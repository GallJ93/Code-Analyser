

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.StringTokenizer;
import physics.Geometry;
import physics.LineSegment;
import shapes.cAbsorber;
import shapes.cLeftFlipper;
import shapes.cRightFlipper;
import shapes.iGizmo;
import utils.fileIO;

/* This is the main model, that in a sense "powers" both views
 * it allows all the informaiton to be stored about the 20x20grid
 * and whats on it
 */
public class basicModel extends Observable implements
		models.basicModelInterface {
	// Set list of our shapes
	private final String[] SET_VALUES = new String[] { "Square", "Triangle",
			"Circle", "LeftFlipper", "RightFlipper", };
	private final Set<String> shapes = new HashSet<String>(
			Arrays.asList(SET_VALUES));

	// If enabled buildMode screen will be displayed
	// If false run mode screen is displayed
	boolean buildingMode = false;

	boolean timerStat = false;
	boolean killThread = false;

	public boolean getKillThread() {
		return killThread;
	}

	public void setKillThread(boolean k) {
		killThread = k;
	}

	// This is our 20x20 grid which can contain any type of gizmos
	// the ball is not included here as it can go anywhere (even between grid
	// spaces)
	private shapes.iGizmo[][] gizmoGrid = new shapes.iGizmo[20][20];

	/*
	 * This is the width of each gizmo to make it easier to understand heres the
	 * calculate if you want to place a square gizmo and you know its in grid
	 * positon x and grid position y then you would do:
	 * 
	 * real x: grid position x * gizmo width real y: grid position y * gizmo
	 * width
	 * 
	 * The value is set in the constructor default set to 0 here when
	 * initilizaed
	 */
	private int gizmoWidth;

	/*
	 * These integers contain the size of our drawing screen but we dont need to
	 * worry about these until later
	 */
	private int drawAreaWidth, drawAreaHeight;

	// Sets the values for gravity and friction on the board in gizmoWidths/s^2
	private double gravity = 2;
	private double frictionM1 = 0.025;
	private double frictionM2 = 0.025;
	private double timeRemaining = 0;

	/* As our ball is not modeled within our grid we have it seperately declared */
	private shapes.cBall ball;
	private shapes.iGizmo walls;

	private String firstBallN;
	public final utils.fileIO fileHandler;
	private double firstBallx, firstBally, firstBallsize, firstBallvx,
			firstBallvy;

	// Consturctor for our model
	public basicModel() {
		// Create a new ball
		setGizmoWidth(20);
		drawAreaHeight = 400;
		drawAreaWidth = 400;
		walls = new shapes.cOuterWalls("Walls", 0, 0, drawAreaWidth);

		firstBallx = 100;
		firstBally = 100;
		firstBallsize = getGizmoWidth() / 4;
		firstBallvx = 0;
		firstBallvy = -3;

		ball = new shapes.cBall("B", firstBallx, firstBally, firstBallsize,
				firstBallvx, firstBallvy);
		// absorber = new shapes.cAbsorber("Absorber 1", 0, 19 * gizmoWidth,
		// drawAreaWidth, 1 * gizmoWidth);

		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				gizmoGrid[x][y] = null;
			}
		}

		fileHandler = new utils.fileIO(this);

	}

	/* Simple getting returns our ball */
	public shapes.cBall getBall() {
		return ball;
	}

	/* Simple setting takes in our ball and sets this.ball */
	public void setBall(shapes.cBall b) {
		this.ball = b;
	}

	/*
	 * Sets the position of the ball along with it's velocity
	 */
	public void setBall(double x, double y, double vx, double vy) {
		firstBallx = x;
		firstBally = y;
		firstBallvx = vx;
		firstBallvy = vy;
		setBall(new shapes.cBall(firstBallN, firstBallx, firstBally,
				firstBallsize, firstBallvx, firstBallvy));
	}

	/*
	 * Returns the balls velocity as an array of doubles
	 */
	public double[] getBallVelocity() {
		double[] toReturn = new double[4];
		toReturn[0] = firstBallx;
		toReturn[1] = firstBally;
		toReturn[2] = firstBallvx;
		toReturn[3] = firstBallvy;
		return toReturn;
	}
	
	
	/*
	 * Sets the ball to it's initial position and resets the velocity
	 */
	public void reloadBall() {
		setBall(new shapes.cBall(firstBallN, firstBallx, firstBally,
				firstBallsize, firstBallvx, firstBallvy));
	}

	/* 
	 * Returns a ball object which has the values set to the initial values
	 */
	public shapes.cBall returnStartingBall() {
		return new shapes.cBall(firstBallN, firstBallx, firstBally,
				firstBallsize, firstBallvx, firstBallvy);
	}

	/* Simple seter for the area width */
	public void setAreaWidth(int w) {
		this.drawAreaWidth = w;
	}

	/* Simple seter for the area height */
	public void setAreaHeight(int h) {
		this.drawAreaHeight = h;
	}

	/*
	 * The width of a gizmo is expalined above, this is just a simple setting
	 * for it
	 */
	public void setGizmoWidth(int w) {
		this.gizmoWidth = w;
	}

	/* Same as above but is the getting */
	private int getGizmoWidth() {
		return gizmoWidth;
	}

	/*
	 * This function is what adds the gizmos to our grid, its very simple it
	 * takes in what kind of operation it is, its name, and position in the grid
	 * 
	 * TO BE UPDATED bit of a hack
	 */
	public boolean addGizmo(String operation, String name, int x, int y) {
		// If theres something on that space
		
		int posx = ((int)(ball.GetX()/20));
		int posy = ((int)(ball.GetY()/20));
		
		int posx1 = ((int)((ball.GetX() + 5)/20));
		int posx2 = ((int)((ball.GetX() - 5)/20));
		int posx3 = ((int)((ball.GetX() + 2.5)/20));
		int posx4 = ((int)((ball.GetX() - 2.5)/20));
		
		int posy1 = ((int)((ball.GetY() + 5)/20));
		int posy2 = ((int)((ball.GetY() - 5)/20));
		int posy3 = ((int)((ball.GetY() + 2.5)/20));
		int posy4 = ((int)((ball.GetY() - 2.5)/20));		
		
		if((!operation.equals("Rotate")) && (checkBall(posx, posy2, x,y) == false || checkBall(posx1,posy,x,y) == false || checkBall(posx,posy1,x,y) == false|| checkBall(posx2,posy,x,y) == false)){
			System.out.println("Cant place on top of ball!");
			return false;
		}
		
		if((!operation.equals("Rotate")) && (checkBall(posx3, posy4, x,y) == false || checkBall(posx3,posy3,x,y) == false || checkBall(posx4,posy3,x,y) == false|| checkBall(posx4,posy4,x,y) == false)){
			System.out.println("Cant place on top of ball!");
			return false;
		}
		
		
		if (x < 0 || y < 0 || operation.length() == 0 || operation == null) {
			return false;
		}
		if (gizmoGrid[x][y] != null && !operation.equals("Rotate")) {
			System.out
					.println("Cant add a gizmo here, there already one here!");
			return false;
		}
		if (name == null) {
			name = (operation + x + y);
		}
		
		if(!unqiueName(name)){
			System.out.println("Cant add something with the same name");
			return false;
		}

		// Switch for each kind of gizmo
		switch (operation) {
		case "Triangle":
			shapes.iGizmo newGizmo = new shapes.cTriangle(name, x, y,
					this.getGizmoWidth());
			gizmoGrid[x][y] = newGizmo;
			break;

		case "Square":
			shapes.iGizmo newGizmo2 = new shapes.cSquare(name, x, y,
					this.getGizmoWidth());
			gizmoGrid[x][y] = newGizmo2;
			break;

		case "Circle":
			shapes.iGizmo newGizmo3 = new shapes.cCircle(name, x, y,
					this.getGizmoWidth());
			gizmoGrid[x][y] = newGizmo3;
			break;

		case "LeftFlipper":

			if (spaceToAddFlipper(x, y)) {
				gizmoGrid[x][y] = new shapes.cLeftFlipper(name, x, y,
						this.getGizmoWidth());
				gizmoGrid[x + 1][y] = new shapes.cLeftFlipper(name, -1, -1,
						this.getGizmoWidth());
				gizmoGrid[x][y + 1] = new shapes.cLeftFlipper(name, -1, -1,
						this.getGizmoWidth());
				gizmoGrid[x + 1][y + 1] = new shapes.cLeftFlipper(name, -1, -1,
						this.getGizmoWidth());

			} else {

				return false;
			}

			break;

		case "RightFlipper":
			if (spaceToAddFlipper(x, y)) {

				gizmoGrid[x][y] = new shapes.cRightFlipper(name, x, y,
						this.getGizmoWidth());
				gizmoGrid[x + 1][y] = new shapes.cRightFlipper(name, -1, -1,
						this.getGizmoWidth());
				gizmoGrid[x][y + 1] = new shapes.cRightFlipper(name, -1, -1,
						this.getGizmoWidth());
				gizmoGrid[x + 1][y + 1] = new shapes.cRightFlipper(name, -1,
						-1, this.getGizmoWidth());

			} else {

				return false;
			}
			break;

		case "Rotate":
			rotateGizmo(x, y);
			break;
		}
		setChanged();
		notifyObservers();
		return true;
	}

	/*
	 * Used to add an absorber to the game, adds it at the specified position.
	 */
	public boolean addAbsorber(String operation, String name, int x1, int y1,
			int x2, int y2) {

		if(!unqiueName(name)){
			System.out.println("Cant add something with the same name");
			return false;
		}
		if (name == null) {
			name = (operation + x1 + y1);
		}

		if ((x1 >= x2) || (y1 > y2)) {
			return false;
		}

		// Check all potential absorber cells for gizmos
		int xDif = x2 - x1;
		int yDif = y2 - y1;
		boolean absorberBlocked = false;
		for (int ys = y1; ys <= (y1 + yDif); ys++) {
			for (int xs = x1; xs <= (x1 + xDif); xs++) {

				if (gizmoGrid[xs][ys] != null) {

					absorberBlocked = true;
				}
			}
		}
		if (!absorberBlocked) {
			gizmoGrid[x1][y1] = new shapes.cAbsorber(name, x1, y1, x2, y2,
					getGizmoWidth());
			// absorber = new shapes.cAbsorber("Absorber 1", 0,
			// 19 * gizmoWidth,
			// drawAreaWidth, 1 * gizmoWidth);
			for (int ys = y1; ys <= (y1 + yDif); ys++) {
				for (int xs = x1; xs <= (x1 + xDif); xs++) {
					if (gizmoGrid[xs][ys] == null) {
						gizmoGrid[xs][ys] = new shapes.cAbsorber(name, -1, -1,
								-1, -1, -1);
					}
				}
			}
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}

	/*
	 * This is used to check if a flipper is ok to be added to the game by making sure that when
	 * it flips it doesn't collide with another gizmo. Returns a boolean to indicate whether it's ok or not
	 */
	private boolean spaceToAddFlipper(int x, int y) {

		if (!((x + 1 < 20) && (y + 1 < 20))) {
			return false;
		}
		if ((gizmoGrid[x][y] == null) && (gizmoGrid[x + 1][y] == null)
				&& (gizmoGrid[x][y + 1] == null)// Make sure there not full
				&& (gizmoGrid[x + 1][y + 1]) == null) {
			return true;
		} else {
			return false;
		}

	}
	
	public int[] nameToCordinates(String n) {
		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				if (gizmoGrid[x][y] == null) {
					continue;
				}
				if (!gizmoGrid[x][y].isPadding()) {
					if (gizmoGrid[x][y].getName().equals(n)) {
						int[] toReturn = new int[] { x, y };
						return toReturn;
					}
				}
			}
		}
		return null;
	}

	
	/* 
	 * This removes a gizmo at the specified position from the grid. As well as it removing it
	 * from the game, it removes all references to it as well.
	 */
	public boolean removeGizmo(int x, int y) {
		if (x < 0 || y < 0 || gizmoGrid[x][y] == null) {
			return false;
		}
		removeReferences(x, y);
		if (this.gizmoGrid[x][y] instanceof shapes.cLeftFlipper
				|| this.gizmoGrid[x][y] instanceof shapes.cRightFlipper) {
			int[] cordinates = nameToCordinates(this.gizmoGrid[x][y].getName());
			int newX = cordinates[0];
			int newY = cordinates[1];
			gizmoGrid[newX][newY] = null;
			gizmoGrid[newX + 1][newY] = null;
			gizmoGrid[newX][newY + 1] = null;
			gizmoGrid[newX + 1][newY + 1] = null;
		} else if (this.gizmoGrid[x][y] instanceof shapes.cAbsorber) {
			String delAbsName = this.gizmoGrid[x][y].getName();
			for (int delY = 0; delY < this.gizmoGrid.length; delY++) {
				for (int delX = 0; delX < this.gizmoGrid.length; delX++) {
					if (this.gizmoGrid[delX][delY] != null
							&& this.gizmoGrid[delX][delY].getName().equals(
									delAbsName)) {
						gizmoGrid[delX][delY] = null;
					}
				}
			}
		} else {
			this.gizmoGrid[x][y] = null;
		}

		setChanged();
		notifyObservers();
		return true;
	}

	// Returns the grid
	public shapes.iGizmo[][] getGrid() {
		return gizmoGrid;
	}

	/*
	 * Returns the Gzmo at a specified position in the Gizmo
	 */
	public shapes.iGizmo getGizmo(int x, int y) {
		return gizmoGrid[x][y];
	}

	/*
	 * Used to move the ball to a new position 
	 */
	public boolean moveBall(double x, double y) {
		if(gizmoGrid[(int) (x / 20)][(int) (y / 20)] != null){
			return false;
		}
		
		if(gizmoGrid[(int) ((x + 20) / 20)][(int) (y / 20)] != null && x < gizmoGrid[(int) ((x + 20) / 20)][(int) (y / 20)].GetX() && x > (gizmoGrid[(int) ((x + 20) / 20)][(int) (y / 20)].GetX() - 5)){
			return false;
		}
		
		if(gizmoGrid[(int) ((x - 20) / 20)][(int) (y / 20)] != null && x > (gizmoGrid[(int) ((x - 20) / 20)][(int) (y / 20)].GetX() + 20) && x < (gizmoGrid[(int) ((x - 20) / 20)][(int) (y / 20)].GetX() + 25)){
			return false;
		}
		
		if(gizmoGrid[(int) (x / 20)][(int) ((y + 20) / 20)] != null && y < (gizmoGrid[(int) (x / 20)][(int) ((y + 20) / 20)].GetY()) && y > (gizmoGrid[(int) (x / 20)][(int) ((y + 20) / 20)].GetY() - 5)){
			return false;
		}
		
		if(gizmoGrid[(int) (x / 20)][(int) ((y - 20) / 20)] != null && y > (gizmoGrid[(int) (x / 20)][(int) ((y - 20) / 20)].GetY() + 20) && y < (gizmoGrid[(int) (x / 20)][(int) ((y - 20) / 20)].GetY() + 25)){
			return false;
		}

		if ((x < this.drawAreaWidth - firstBallsize && x > firstBallsize)
				&& (y > firstBallsize && y < this.drawAreaHeight
						- firstBallsize)) {
			ball.setPOS(x, y);
			firstBallx = x;
			firstBally = y;
			setChanged();
			notifyObservers();
			return true;


		}
		return false;

	}


	private boolean emptyBox(int x, int y) {
		return (this.getGizmo(x, y) == null);
	}

	/*
	 * Returns a Gizmo object by searching for its name
	 */
	public shapes.iGizmo getGizmo(String n) {
		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				if (gizmoGrid[x][y] == null) {
					continue;
				}
				if (!gizmoGrid[x][y].isPadding()) {
					if (gizmoGrid[x][y].getName().equals(n)) {
						return gizmoGrid[x][y];
					}
				}
			}
		}
		return null;
	}

	// Getter for the building mode boolean
	public boolean isBuildingMode() {
		return buildingMode;
	}

	// Simple setter, also alerts the GUI to update
	public void setBuildingMode(boolean bool) {
		buildingMode = bool;
		setChanged();
		notifyObservers();
	}

	/* Reads/Parse this given file in accordance to the spec */
	public boolean readFile(String fileName) {
		StringTokenizer st = null;
		BufferedReader br = null;
		try {

			String sCurrentLine;
			System.out.println("Reading: " + fileName);
			br = new BufferedReader(new FileReader(fileName));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					continue;
				}
				st = new StringTokenizer(sCurrentLine);

				String type = st.nextToken();
				try {

					if (shapes.contains(type)) {
						String name = st.nextToken();
						int x = Integer.parseInt(st.nextToken());
						int y = Integer.parseInt(st.nextToken());
						if(!addGizmo(type, name, x, y)){
							throw new Exception();
						}
					} else if (type.equals("KeyConnect")) {
						st.nextToken();
						int keyNum = Integer.parseInt(st.nextToken());
						String dir = st.nextToken();
						String n = st.nextToken();
						addKeyMapping(keyNum, dir, n);
					} else if (type.equals("Connect")) {
						String source = st.nextToken();
						String toTrigger = st.nextToken();
						this.addTrigger(source, toTrigger);

					} else if (type.equals("Delete")) {
						String n = st.nextToken();
						int[] cor = nameToCordinates(n);
						if (getGizmo(cor[0], cor[1]) == null) {
							continue;
						}
						removeGizmo(cor[0], cor[1]);

					} else if (type.equals("Move")) {
						String n = st.nextToken();
						int x = Integer.parseInt(st.nextToken());
						int y = Integer.parseInt(st.nextToken());
						System.out.println("Moving " + n + " from " + x + ","
								+ y);
						moveGizmo(n, x, y);
					} else if (type.equals("Ball")) {
						double bx, by, vx, vy;
						String n = st.nextToken();
						bx = Double.parseDouble(st.nextToken());
						by = Double.parseDouble(st.nextToken());
						vx = Double.parseDouble(st.nextToken());
						vy = Double.parseDouble(st.nextToken());
						System.out.println("Ball  " + bx + "," + by);
						ball = null;
						ball = new shapes.cBall(n, bx, by, firstBallsize, vx,
								vy);
						firstBallx = bx;
						firstBally = by;
						firstBallsize = getGizmoWidth() / 4;
						firstBallvx = vx;
						firstBallvy = vy;

					} else if (type.equals("Gravity")) {
						double g = Double.parseDouble(st.nextToken());
						this.setGravity(g);
						System.out.println("Gravity Set: " + g);
					} else if (type.equals("Friction")) {
						double f = Double.parseDouble(st.nextToken());
						double f2 = Double.parseDouble(st.nextToken());
						this.setFrictionM1(f);
						this.setFrictionM2(f2);
						System.out.println("Friction set M1: " + f + " M2:"
								+ f2);

					} else if (type.equals("Rotate")) {
						String n = st.nextToken();

						if (getGizmo(n) != null) {
							System.out.println("Triangle rotated");
							getGizmo(n).rotate();

						}
					} else if (type.equals("Absorber")) {
						int x1, y1, x2, y2;
						String n = st.nextToken();
						System.out.println(n);
						x1 = Integer.parseInt(st.nextToken());
						System.out.println(x1);
						y1 = Integer.parseInt(st.nextToken());
						System.out.println(y1);
						x2 = Integer.parseInt(st.nextToken());
						System.out.println(x2);
						y2 = Integer.parseInt(st.nextToken());
						System.out.println(y2);
						addAbsorber(type, n, x1, y1, x2, y2);
					}
				} catch (Exception e) {
					clearModel();
					System.out.println("File is formatted wrong: "
							+ sCurrentLine);
					return false;
				}
			}

		} catch (IOException e) {

		} finally {
			try {
				if (br != null)
					br.close();

				setChanged();
				notifyObservers();
				return true;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	/*
	 * Removes all references to a particular Gizmo, used in the delete Gizmo method
	 */
	private void removeReferences(int rx, int ry) {
		if (gizmoGrid[rx][ry] == null) {
			return;
		}

		String n = gizmoGrid[rx][ry].getName();

		for (int i = 0; i < keyMapping.size(); i++) {
			utils.KeyTriggerDetail temp = keyMapping.get(i);
			if (temp.getTrigger().equals(n)) {
				keyMapping.remove(temp);
			}
		}

		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				if (gizmoGrid[x][y] == null) {
					continue;
				}
				if (!gizmoGrid[x][y].isPadding()) {
					gizmoGrid[x][y].removeTrigger(n);
				}
			}
		}
		triggersStart.clear();
		triggersStop.clear();
	}

	/*
	 * Adds a Gizmo trigger to a Gizmo
	 */
	public void addTrigger(String s, String tt) {
		if (getGizmo(s) == null || this.getGizmo(tt) == null) {
			return;
		}
		this.getGizmo(s).addTrigger(tt);
	}

	/*
	 * Resets values in the model back to their initial state
	 */
	public void clearModel() {
		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				gizmoGrid[x][y] = null;
			}
		}

		// Reset key mapping
		emptyKeyMapping();

		// Clearing triggers
		triggersStop.clear();
		triggersStart.clear();
		
		ball.setPOS(-10, -10);
		
		setChanged();
		notifyObservers();
	}

	/*
	 * Resets the absorber so it returns to its intiial state
	 */
	public void resetAbsorbers() {
		for (int x = 0; x < gizmoGrid.length; x++) {
			for (int y = 0; y < gizmoGrid[x].length; y++) {
				if (gizmoGrid[x][y] instanceof cAbsorber) {
					cAbsorber temp = (cAbsorber) gizmoGrid[x][y];
					temp.resetRefrences();

				}
			}
		}
	}

	/*
	 * Used to move a Gizmo to the speicifed position. Has checks to
	 * prevent overlapping and colliding 
	 */
	public boolean moveGizmo(String n, int x, int y) {
		
		int posx = ((int)(ball.GetX()/20));
		int posy = ((int)(ball.GetY()/20));
		
		int posx1 = ((int)((ball.GetX() + 5)/20));
		int posx2 = ((int)((ball.GetX() - 5)/20));
		int posx3 = ((int)((ball.GetX() + 2.5)/20));
		int posx4 = ((int)((ball.GetX() - 2.5)/20));
		
		int posy1 = ((int)((ball.GetY() + 5)/20));
		int posy2 = ((int)((ball.GetY() - 5)/20));
		int posy3 = ((int)((ball.GetY() + 2.5)/20));
		int posy4 = ((int)((ball.GetY() - 2.5)/20));		
		
		if(checkBall(posx, posy2, x,y) == false || checkBall(posx1,posy,x,y) == false || checkBall(posx,posy1,x,y) == false|| checkBall(posx2,posy,x,y) == false){
			System.out.println("Cant place on top of ball!");
			return false;
		}
		
		if(checkBall(posx3, posy4, x,y) == false || checkBall(posx3,posy3,x,y) == false || checkBall(posx4,posy3,x,y) == false|| checkBall(posx4,posy4,x,y) == false){
			System.out.println("Cant place on top of ball!");
			return false;
		}

		if (gizmoGrid[x][y] != null) {
			System.out
					.println("Cant move a gizmo here, there already one here!");
			if (!getGizmo(x, y).getName().equals(n)) {
				return false;	
			}
		}
		
		
		
		int[] cordin = nameToCordinates(n);

		
		shapes.iGizmo temp = getGizmo(cordin[0], cordin[1]);

		if (isItAFlipper(gizmoGrid[cordin[0]][cordin[1]])) {
			if( gizmoGrid[cordin[0]][cordin[1]] instanceof cLeftFlipper){
				cLeftFlipper tempFlip = (cLeftFlipper)temp;
				if (x >= (cordin[0] - 1) && x <= (cordin[0] + 1) && y >= (cordin[1] - 1) && y <= (cordin[1] + 1)) {
					removeGizmo(cordin[0], cordin[1]);
					if (spaceToAddFlipper(x, y)) {
					gizmoGrid[x][y] = new shapes.cLeftFlipper(tempFlip.getName(), x, y,
							this.getGizmoWidth());
					gizmoGrid[x + 1][y] = new shapes.cLeftFlipper(tempFlip.getName(), -1, -1,
							this.getGizmoWidth());
					gizmoGrid[x][y + 1] = new shapes.cLeftFlipper(tempFlip.getName(), -1, -1,
							this.getGizmoWidth());
					gizmoGrid[x + 1][y + 1] = new shapes.cLeftFlipper(tempFlip.getName(), -1, -1,
							this.getGizmoWidth());
					return true;
				}
			}
			} 
		}
		
		if (isItAFlipper(gizmoGrid[cordin[0]][cordin[1]])) {
		if (gizmoGrid[cordin[0]][cordin[1]] instanceof cRightFlipper) {
				cRightFlipper tempFlip = (cRightFlipper)temp;
				if (x >= (cordin[0] - 1) && x <= (cordin[0] + 1) && y >= (cordin[1] - 1) && y <= (cordin[1] + 1)) {
					removeGizmo(cordin[0], cordin[1]);
					if (spaceToAddFlipper(x, y)) {
					gizmoGrid[x][y] = new shapes.cRightFlipper(tempFlip.getName(), x, y,
							this.getGizmoWidth());
					gizmoGrid[x + 1][y] = new shapes.cRightFlipper(tempFlip.getName(), -1, -1,
							this.getGizmoWidth());
					gizmoGrid[x][y + 1] = new shapes.cRightFlipper(tempFlip.getName(), -1, -1,
							this.getGizmoWidth());
					gizmoGrid[x + 1][y + 1] = new shapes.cRightFlipper(tempFlip.getName(), -1,
							-1, this.getGizmoWidth());
					return true;
			}
			}
		}
		}
		
		if (!spaceToAddFlipper(x, y)
				&& isItAFlipper(gizmoGrid[cordin[0]][cordin[1]])) {
			return false;
		}

		boolean sucessfulAbsMove = false;
		
		if( gizmoGrid[cordin[0]][cordin[1]] instanceof cAbsorber){
			cAbsorber tempAbs = (cAbsorber)temp;
			int width = tempAbs.getCellLength();
			int height = tempAbs.getCellHeight();
			
			sucessfulAbsMove = addAbsorber(tempAbs.getType(), tempAbs.getName() + x + y, x, y, x + tempAbs.getCellLength(), y + tempAbs.getCellHeight());
			if(sucessfulAbsMove){
				removeGizmo(cordin[0], cordin[1]);	
				return true;
			} else if  (x >= (cordin[0] - width) && x <= (cordin[0] + width) && y >= (cordin[1] - height) && y <= (cordin[1] + height)) {
				removeGizmo(cordin[0], cordin[1]);
				addAbsorber(tempAbs.getType(), tempAbs.getName() + x + y, x, y, x + tempAbs.getCellLength(), y + tempAbs.getCellHeight());
						return true;
			}
			else if (gizmoGrid[x][y] == null) { return false; }
		}

		// Remove it
		removeGizmo(cordin[0], cordin[1]);
		// Add it to its new position
		addGizmo(temp.getType(), temp.getName(), x, y);
		return true;

	}

	public boolean checkBall(int posx, int posy, int x, int y){
		if(x == posx && y == posy){

			return false;
		}
//		else if(y == posy){
//
//			return false;
//		}
		return true;
	}
	
	/*
	 * Sets the gravity of the game to the specified value
	 */
	public void setGravity(double g) {
		this.gravity = g;
	}

	/*
	 * Returns a boolean value to determine the status of the timer
	 */
	public boolean timerStatus() {
		return timerStat;
	}

	/*
	 * Sets the timer to either true or false 
	 */
	public void setTimerStatus(boolean s) {
		System.out.println("Setting status to " + s);
		timerStat = s;
		setChanged();
		notifyObservers();
	}

	/*
	 * Checks if a particular gizmo is either a left or
	 * right flipper
	 */
	private boolean isItAFlipper(shapes.iGizmo i) {
		if (i instanceof shapes.cLeftFlipper
				|| i instanceof shapes.cRightFlipper) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * This is used to handle the games physics. Deals with gravity, velocity,
	 * friction, collision and so on.
	 */
	public void physicsLoop() {

		int closesti = 0;
		int closestj = 0;
		int lineOrCircle = 0;

		physics.LineSegment closestLine = null;
		physics.Circle closestCircle = null;

		physics.Vect velocity = new physics.Vect(ball.getXVelocity(),
				ball.getYVelocity());
		physics.Vect centerOfRotation;

		double minTime = Double.POSITIVE_INFINITY;
		double angularMomentum, distanceFromBelowGizmo = Double.POSITIVE_INFINITY;

		if (walls.getLines() != null) {

			for (physics.LineSegment line : walls.getLines()) {
				if (Geometry.timeUntilWallCollision(line, ball.getBall(),
						velocity) < minTime) {
					minTime = Geometry.timeUntilWallCollision(line,
							ball.getBall(), velocity);
					closesti = -1;
					closestj = -1;
					lineOrCircle = 1;
					closestLine = line;
				}
			}
		}
		if (walls.getCircles() != null) {
			for (physics.Circle circle : walls.getCircles()) {
				if (Geometry.timeUntilCircleCollision(circle, ball.getBall(),
						velocity) < minTime) {
					minTime = Geometry.timeUntilCircleCollision(circle,
							ball.getBall(), velocity);
					closesti = -1;
					closestj = -1;
					lineOrCircle = 2;
					closestCircle = circle;
				}
			}
		}

		for (int i = 0; i < gizmoGrid.length; i++) {
			for (int j = 0; j < gizmoGrid[i].length; j++) {
				if (gizmoGrid[i][j] != null) {
					if (gizmoGrid[i][j].isRotating() != 0) {
						centerOfRotation = new physics.Vect(
								gizmoGrid[i][j].GetX() + getGizmoWidth() / 2,
								gizmoGrid[i][j].GetY() + getGizmoWidth() / 2);
						angularMomentum = Math.toRadians(180)
								* gizmoGrid[i][j].isRotating();

						for (int k = 0; k < gizmoGrid[i][j].getLines().size(); k++) {
							LineSegment line = gizmoGrid[i][j].getLines()
									.get(k);
							if (Geometry.timeUntilRotatingWallCollision(line,
									centerOfRotation, angularMomentum,
									ball.getBall(), velocity) < minTime) {
								minTime = Geometry.timeUntilWallCollision(line,
										ball.getBall(), velocity);
								closesti = i;
								closestj = j;
								lineOrCircle = 1;
								closestLine = line;
							}
						}

						for (int k = 0; k < gizmoGrid[i][j].getCircles().size(); k++) {
							physics.Circle circle = gizmoGrid[i][j]
									.getCircles().get(k);

							if (Geometry.timeUntilRotatingCircleCollision(
									circle, centerOfRotation, angularMomentum,
									ball.getBall(), velocity) < minTime) {
								minTime = Geometry.timeUntilCircleCollision(
										circle, ball.getBall(), velocity);
								closesti = i;
								closestj = j;
								lineOrCircle = 2;
								closestCircle = circle;
							}
						}
					} else {
						if (gizmoGrid[i][j].getLines() != null) {
							for (physics.LineSegment line : gizmoGrid[i][j]
									.getLines()) {
								if (Geometry.timeUntilWallCollision(line,
										ball.getBall(), velocity) < minTime) {
									minTime = Geometry.timeUntilWallCollision(
											line, ball.getBall(), velocity);
									closesti = i;
									closestj = j;
									lineOrCircle = 1;
									closestLine = line;
								}
							}
						}
						if (gizmoGrid[i][j].getCircles() != null) {
							for (physics.Circle circle : gizmoGrid[i][j]
									.getCircles()) {
								if (Geometry.timeUntilCircleCollision(circle,
										ball.getBall(), velocity) < minTime) {
									minTime = Geometry
											.timeUntilCircleCollision(circle,
													ball.getBall(), velocity);
									closesti = i;
									closestj = j;
									lineOrCircle = 2;
									closestCircle = circle;
								}
							}
						}
					}
				}
			}
		}

		if (lineOrCircle == 1) {
			distanceFromBelowGizmo = calculateDistanceToLine(closestLine,
					ball.getBall());
		} else if (lineOrCircle == 2) {
			distanceFromBelowGizmo = calculateDistanceToCircle(closestCircle,
					ball.getBall());
		}

		if (ball.getYVelocity() > 0) {
			ball.adjustVelocity(gravity, frictionM1, frictionM2,
					distanceFromBelowGizmo);
		} else {
			ball.adjustVelocity(gravity, frictionM1, frictionM2,
					Double.POSITIVE_INFINITY);
		}

		if (minTime < timeRemaining) {

			if (closesti == -1 && closestj == -1) {

				// TODO Trigger walls

				if (lineOrCircle == 1) {
					physics.Vect newVelocity = Geometry.reflectWall(
							closestLine, velocity, 1);
					ball.setXVelocity(newVelocity.x());
					ball.setYVelocity(newVelocity.y());
				} else if (lineOrCircle == 2) {
					physics.Vect newVelocity = Geometry.reflectCircle(
							closestCircle.getCenter(), ball.getBall()
									.getCenter(), velocity, 1);
					if (velocity.x() < 0) {
						ball.setXVelocity(newVelocity.x());
					} else if (velocity.x() > 0) {
						ball.setXVelocity(-(newVelocity.x()));
					}
					if (velocity.y() < 0) {
						ball.setYVelocity(newVelocity.y());
					} else if (velocity.y() > 0) {
						ball.setYVelocity(-(newVelocity.y()));
					}
				}
			} else {

				System.out.println("It's not a Flipper!");

				// TODO Trigger gizmoGrid[closesti][closestj]

				if (lineOrCircle == 1) {
					System.out.println("It's a line!!!");

					if (gizmoGrid[closesti][closestj].getType().equals(
							"RightFlipper")
							|| gizmoGrid[closesti][closestj].getType().equals(
									"LeftFlipper")) {
						if (gizmoGrid[closesti][closestj].isRotating() != 0) {
							centerOfRotation = new physics.Vect(
									gizmoGrid[closesti][closestj].GetX()
											+ getGizmoWidth() / 2,
									gizmoGrid[closesti][closestj].GetY()
											+ getGizmoWidth() / 2);
							angularMomentum = Math.toRadians(90)
									* gizmoGrid[closesti][closestj]
											.isRotating();
							physics.Vect newVelocity = Geometry
									.reflectRotatingWall(closestLine,
											centerOfRotation, angularMomentum,
											ball.getBall(), velocity, 0.95);
							ball.setXVelocity(newVelocity.x());
							ball.setYVelocity(newVelocity.y());
							triggerGizmo(closesti, closestj);
						} else {
							physics.Vect newVelocity = Geometry.reflectWall(
									closestLine, velocity, 0.95);
							ball.setXVelocity(newVelocity.x());
							ball.setYVelocity(newVelocity.y());
							triggerGizmo(closesti, closestj);
						}
					} else if (gizmoGrid[closesti][closestj] instanceof shapes.cAbsorber) {
						// physics.Vect newVelocity = Geometry.reflectWall(
						// closestLine, velocity, 0);
						// ball.setXVelocity(newVelocity.x());
						// ball.setYVelocity(newVelocity.y());
						shapes.cAbsorber temp = (shapes.cAbsorber) gizmoGrid[closesti][closestj];
						temp.captureBall(ball, this);
						triggerGizmo(closesti, closestj);
					} else {
						physics.Vect newVelocity = Geometry.reflectWall(
								closestLine, velocity, 1);
						ball.setXVelocity(newVelocity.x());
						ball.setYVelocity(newVelocity.y());
						triggerGizmo(closesti, closestj);
					}

				} else if (lineOrCircle == 2) {
					if (gizmoGrid[closesti][closestj].getType().equals(
							"RightFlipper")
							|| gizmoGrid[closesti][closestj].getType().equals(
									"LeftFlipper")) {
						if (gizmoGrid[closesti][closestj].isRotating() != 0) {
							centerOfRotation = new physics.Vect(
									gizmoGrid[closesti][closestj].GetX()
											+ getGizmoWidth() / 2,
									gizmoGrid[closesti][closestj].GetY()
											+ getGizmoWidth() / 2);
							angularMomentum = Math.toRadians(90)
									* gizmoGrid[closesti][closestj]
											.isRotating();
							physics.Vect newVelocity = Geometry
									.reflectRotatingCircle(closestCircle,
											centerOfRotation, angularMomentum,
											ball.getBall(), velocity, 0.95);
							ball.setXVelocity(newVelocity.x());
							ball.setYVelocity(newVelocity.y());
							triggerGizmo(closesti, closestj);
						} else {
							physics.Vect newVelocity = Geometry.reflectCircle(
									closestCircle.getCenter(), ball.getBall()
											.getCenter(), velocity, 0.95);
							ball.setXVelocity(newVelocity.x());
							ball.setYVelocity(newVelocity.y());
							triggerGizmo(closesti, closestj);
						}
					} else {
						physics.Vect newVelocity = Geometry.reflectCircle(
								closestCircle.getCenter(), ball.getBall()
										.getCenter(), velocity, 1);
						ball.setXVelocity(newVelocity.x());
						ball.setYVelocity(newVelocity.y());
						triggerGizmo(closesti, closestj);
					}
				}
			}
		}

		ball = ball.move();

	}

	/*
	 * This is used to call a certain Gizmos trigger
	 */
	private void triggerGizmo(int x, int y) {
		// System.out.println("Triggered called");
		if (gizmoGrid[x][y] != null) {
		if (gizmoGrid[x][y] instanceof shapes.cAbsorber
		&& ((shapes.cAbsorber) gizmoGrid[x][y]).hasCaughtBall()) {
		((shapes.cAbsorber) gizmoGrid[x][y]).startTrigger();
		}
		List<String> trigs = gizmoGrid[x][y].getTriggers();
		for (String s : trigs) {
		shapes.iGizmo temp = getGizmo(s);
		if (temp instanceof shapes.cLeftFlipper
		|| temp instanceof shapes.cRightFlipper) {
		if (temp.getTriggeredStatus()) {
		triggersStop.add(s);
		continue;
		} else {
		triggersStart.add(s);
		continue;
		}
		}
		System.out.println("Added to start " + s);
		triggersStart.add(new String(s));
		System.out.println("Added to stop " + s);
		triggersStop.add(new String(s));
		}
		}
	}

	utils.NoDuplicatesList<utils.KeyTriggerDetail> keyMapping = new utils.NoDuplicatesList<utils.KeyTriggerDetail>();

	utils.NoDuplicatesList<String> triggersStop = new utils.NoDuplicatesList<String>();
	utils.NoDuplicatesList<String> triggersStart = new utils.NoDuplicatesList<String>();

	/*
	 * This adds key mapping to a particualr Gizmo
	 */
	public void addKeyMapping(int key, String direction, String n) {
		iGizmo test = getGizmo(n);
		if (test == null) {
			System.out.println("Cant connect a gizmo to a key thats not real");
			return;
		}
		System.out.println("Mapping key " + key + " " + direction
				+ " to gizmo " + n);
		boolean dir = false;
		if (direction.equalsIgnoreCase("up")) {
			dir = true;
		}
		keyMapping.add(new utils.KeyTriggerDetail(key, n, dir));

	}

	/*
	 * This is used to determine whether a key was pressed and what key it was
	 */
	public void registerKeyPressed(int keyCode) {
		for (int i = 0; i < keyMapping.size(); i++) {
			utils.KeyTriggerDetail temp = keyMapping.get(i);
			if (temp.getKey() == keyCode && !temp.getUp()) {
				System.out.println("Registered New Key DOWN " + keyCode);
				triggersStart.add(temp.getTrigger());
			}
		}
	}

	/*
	 * This is used to determine whether a key was released and what key it was
	 */
	public void registerKeyReleased(int keyCode) {
		for (int i = 0; i < keyMapping.size(); i++) {
			utils.KeyTriggerDetail temp = keyMapping.get(i);
			if (temp.getKey() == keyCode && temp.getUp()) {
				System.out.println("Registered New Key UP " + keyCode);

				triggersStart.remove(temp.getTrigger());
				triggersStop.add(temp.getTrigger());
			}
		}
	}

	HashSet<String> keyhistory = new HashSet<String>();

	/*
	 * Used to start and stop the Gizmos
	 */
	public void setGizmoTriggers() {
		while (!triggersStart.isEmpty()) {
			String s = triggersStart.remove();
			System.out.println("Started: " + s);
			getGizmo(s).startTrigger();
			calculateGizmoActions();

		}

		

		while (!triggersStop.isEmpty()) {
			String s = triggersStop.remove();
			System.out.println("Stoped: " + s);
			getGizmo(s).stopTrigger();

		}

	}

	/*
	 * Determines the action that needs to be performed by a Gizmo (Based on a trigger)
	 */
	public void calculateGizmoActions() {
		for (int x = 0; x < this.gizmoGrid.length; x++) {
			for (int y = 0; y < this.gizmoGrid[x].length; y++) {
				if (this.gizmoGrid[x][y] == null) {
					continue;
				}
				this.gizmoGrid[x][y].preformAction();
			}
		}

	}

	/*
	 * Roatates a Gizmo at a specified location
	 */
	public boolean rotateGizmo(int x, int y) {
		System.out.println("Rotating");
		if (gizmoGrid[x][y] != null) {
			if (gizmoGrid[x][y].isPadding()) {
				getGizmo( gizmoGrid[x][y].getName() ).rotate();
			} else{
			gizmoGrid[x][y].rotate();
			}
			System.out.println("Rotating now");
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}

	/*
	 * Returns the frictionM1 value
	 */
	public double getFrictionM1() {
		return frictionM1;
	}

	/*
	 *  Sets the frictionM1 value
	 */
	public void setFrictionM1(double frictionM1) {
		this.frictionM1 = frictionM1;
	}

	/*
	 * Returns the frictionM2 value
	 */
	public double getFrictionM2() {
		return frictionM2;
	}

	/*
	 * Sets the frictionM2 value
	 */
	public void setFrictionM2(double frictionM2) {
		this.frictionM2 = frictionM2;
	}

	/*
	 *  Returns the gravity value
	 */
	public double getGravity() {
		return gravity;
	}

	/*
	 * Returns the list of keys for key mapping
	 */
	public utils.NoDuplicatesList<utils.KeyTriggerDetail> getKeyMapping() {

		return keyMapping;
	}

	/*
	 * Deletes the contents of the key mapping list
	 */
	private void emptyKeyMapping() {
		keyMapping.clear();
	}

	/*
	 * Sets the time remaining to the specified value
	 */
	public void setTimeRemaining(double timeLeft) {
		this.timeRemaining = timeLeft;
	}

	/*
	 * This is part of the physics, calculates the distance from the balls current location
	 * to the next collision if it isn't a circle
	 */
	private double calculateDistanceToLine(physics.LineSegment line,
			physics.Circle tempBall) {
		double startX = line.p1().x();
		double startY = line.p1().y();
		double endX = line.p2().x();
		double endY = line.p2().y();
		double ballX = tempBall.getCenter().x();
		double ballY = tempBall.getCenter().y();

		double topHalf = ((ballY * (startX - endX)) - (ballX * (startY - endY)) + ((endX * startY) - (startX * endY)));
		double topHalfSquared = topHalf * topHalf;
		double bottomHalf = (((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY)));

		double resultSquared = topHalfSquared / bottomHalf;
		double result = Math.sqrt(resultSquared);

		return result / getGizmoWidth();
	}

	/*
	 * This is part of the physics, calculates the distance from the balls current location
	 * to the next collision if it is a circle
	 */
	private double calculateDistanceToCircle(physics.Circle circle,
			physics.Circle tempBall) {
		return Geometry.distanceSquared(circle.getCenter(),
				tempBall.getCenter())
				/ getGizmoWidth();
	}

	@Override
	/*
	 * Returns the file handler
	 */
	public fileIO getFileHandler() {
		return fileHandler;
	}

	/*
	 * This is sed to determine if the name of a Gizmo is unique and not
	 * being used anywhere else
	 */
	public boolean unqiueName(String n) {
		for (int x = 0; x < getGrid().length; x++) {
			for (int y = 0; y < getGrid()[x].length; y++) {
				if (getGrid()[x][y] != null) {
					if (getGrid()[x][y].getName().equalsIgnoreCase(n)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}