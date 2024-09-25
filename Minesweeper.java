import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//represents a world class to animate Minesweeper 
class MSWorld extends World {
  int rows;
  int columns;
  int mines;
  Random rand;
  ArrayList<ArrayList<Cell>> grid;
  boolean initScreen;
  
  //official game constructor with 0 inputs, including the homescreen to select difficulty
  MSWorld() {
    this(16, 30, 0);
    this.initScreen = true;
  }
  
  //constructor with inputs for rows, columns and number of mines
  MSWorld(int rows, int columns, int mines) {
    this(rows, columns, mines, new Random());
  }
  
  //test constructor, with inputs for rows, cols, mines and a set random seed
  MSWorld(int rows, int columns, int mines, Random rand) {
    this.rows = rows;
    this.columns = columns;
    this.mines = mines;
    this.rand = rand;
    this.grid = new ArrayList<ArrayList<Cell>>();
    this.initScreen = false;
    //initializing the game by calling 3 methods
    this.makeGrid();
    this.placeMines();
    this.setNeighbors();
  }
  
  //test constructor, makes a pre-set 3x3 world with specific cells flagged and revealed
  MSWorld(boolean test) {
    this(3, 3, 2, new Random(0));
    if (test) {
      //reveal all cells
      for (int i = 0; i < 3; i = i + 1) {
        for (int j = 0; j < 3; j = j + 1) {
          this.grid.get(i).get(j).revealed = true;
        }
      }
      //flags mine at (1, 2)
      this.grid.get(0).get(1).flagged = true;
      //hides cell at (2, 1)
      this.grid.get(1).get(0).revealed = false;
    }
  }  
  
  //initializes the starting grid based off the number of rows and columns
  public void makeGrid() {
    for (int i = 0; i < rows; i = i + 1) {
      ArrayList<Cell> eachRow = new ArrayList<Cell>();
      for (int n = 0; n < columns; n = n + 1) {
        eachRow.add(new Cell());
      }
      this.grid.add(eachRow);
    }
  }
  
  //places random mines on the board according to the given number of mines
  public void placeMines() {
    for (int i = 0; i < mines; i = i + 1) {
      int randRow = this.rand.nextInt(rows);
      int randColumn = this.rand.nextInt(columns);
      Cell randCell = this.grid.get(randRow).get(randColumn);
      if (randCell.mine) {
        i = i - 1;
      }
      else {
        randCell.mine = true;
      }
    }
  }
  
  //sets the neighbors of each cell in the grid
  public void setNeighbors() {
    for (int i = 0; i < rows; i = i + 1) {
      for (int n = 0; n < columns; n = n + 1) {
        Cell newCell = grid.get(i).get(n);
        newCell.addNeighbors(this.grid, i, n);
      }
    }
  }
  
  //draws this Worldscene
  public WorldScene makeScene() { 
    int width = columns * 40 + 60;
    int height = rows * 40 + 170;
    WorldScene scene = new WorldScene(width, height);
    
    //placing borders around the world
    scene.placeImageXY(
        new OverlayImage(new RectangleImage(width - 10, height - 10, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                        new RectangleImage(width - 10, height - 10,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width, height, "solid", Color.white))))), 
        width / 2, height / 2);
    
    // making stat borders (title screen or face and timer)
    scene.placeImageXY(
        new OverlayImage(
            new RectangleImage(width - 60, 80, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(width - 60, 80,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width - 50, 90, "solid", Color.white))))), 
        width / 2, 70);
    
    // making cell borders
    scene.placeImageXY(
        new OverlayImage(
            new RectangleImage(columns * 40, rows * 40, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(columns * 40, rows * 40,"solid", Color.gray), 0, 0, 
                        new RectangleImage(columns * 40 + 10, rows * 40 + 10, 
                            "solid", Color.white))))), 
            width / 2, (height + 110) / 2);
    
    // if initScreen, draw the main manu
    if (initScreen) {
      scene.placeImageXY(new TextImage("Select difficulty:", 24, Color.black), 630, 70);
      
      //easy button
      scene.placeImageXY(
          new OverlayImage(new TextImage("Easy: 9 x 9 grid with 10 mines", 18, Color.black),
              new OverlayImage(new RectangleImage(490, 60, "solid", Color.lightGray), 
                  new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                      new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                          "solid", Color.gray), 0, 0, 
                      new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                          new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                              "solid", Color.gray), 0, 0, 
                          new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                              new RectangleImage(490, 60,"solid", Color.gray), 0, 0, 
                              new RectangleImage(500, 70, "solid", Color.white)))))), 
          630, 300);
      
      //medium button
      scene.placeImageXY(
          new OverlayImage(new TextImage("Medium: 16 x 16 grid with 35 mines", 18, Color.black),
              new OverlayImage(new RectangleImage(490, 60, "solid", Color.lightGray), 
                  new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                      new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                          "solid", Color.gray), 0, 0, 
                      new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                          new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                              "solid", Color.gray), 0, 0, 
                          new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                              new RectangleImage(490, 60,"solid", Color.gray), 0, 0, 
                              new RectangleImage(500, 70, "solid", Color.white)))))), 
          630, 460);
      
      //hard button
      scene.placeImageXY(
          new OverlayImage(new TextImage("Medium: 16 x 30 grid with 70 mines", 18, Color.black),
              new OverlayImage(new RectangleImage(490, 60, "solid", Color.lightGray), 
                  new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                      new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                          "solid", Color.gray), 0, 0, 
                      new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                          new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                              "solid", Color.gray), 0, 0, 
                          new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                              new RectangleImage(490, 60,"solid", Color.gray), 0, 0, 
                              new RectangleImage(500, 70, "solid", Color.white)))))), 
          630, 620);
      
      //quit game button
      scene.placeImageXY(
          new OverlayImage(new TextImage("Quit", 14, Color.red),
              new OverlayImage(new RectangleImage(100, 40, "solid", Color.lightGray), 
                  new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                      new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                          "solid", Color.gray), 0, 0, 
                      new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                          new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                              "solid", Color.gray), 0, 0, 
                          new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                              new RectangleImage(100, 40,"solid", Color.gray), 0, 0, 
                              new RectangleImage(110, 50, "solid", Color.white)))))), 
          100, 70);
    }
    
    //else draw game
    else {
      //tracking score
      int score = 0;
      for (int r = 0; r < rows; r = r + 1) {
        for (int c = 0; c < columns; c = c + 1) {
          Cell scoreCell = grid.get(r).get(c);
          if (scoreCell.revealed && !scoreCell.mine) {
            score++;
          }
        }
      }
      
      //if game board is too small, only draw the score
      if (columns < 9) {
        scene.placeImageXY(new TextImage(Integer.toString(rows * columns - mines - score),
            18, Color.black), 
            (columns * 30 + 40), 72);
      }
      
      //otherwise draw main menu buttn and score
      else {
        scene.placeImageXY(new TextImage(Integer.toString(rows * columns - mines - score)
            + " left", 18, Color.black), (columns * 30 + 40), 72);
        scene.placeImageXY(
            new OverlayImage(new TextImage("Main Menu", 14, Color.black),
                new OverlayImage(new RectangleImage(90, 30, "solid", Color.lightGray), 
                    new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                        new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                            "solid", Color.gray), 0, 0, 
                        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                            new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                                "solid", Color.gray), 0, 0, 
                            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                                new RectangleImage(90, 30,"solid", Color.gray), 0, 0, 
                                new RectangleImage(100, 40, "solid", Color.white)))))), 
            (columns * 10 + 10), 70);
      }
     
      //making grid
      for (int i = 0; i < rows; i = i + 1) {
        for (int n = 0; n < columns; n = n + 1) {
          int x = n * 40 + 50;
          int y = i * 40 + 160;
          Cell newCell = grid.get(i).get(n);
          //calls cell image to draw an individual cell
          scene.placeImageXY(newCell.cellImage(), x, y);
        }
      }
      
      //drawing face/restart button
      scene.placeImageXY(new OverlayImage(new RectangleImage(40, 40, "solid", Color.lightGray), 
          new OverlayImage(new TriangleImage(new Posn(0, 0), new Posn(0, 50), 
              new Posn(50, 0),"solid", Color.white),
              new TriangleImage(new Posn(50, 50), new Posn(0, 50), 
                  new Posn(50, 0),"solid", Color.gray))), (columns * 20 + 30), 70);
      
      //drawing the face
      //drawing win screen
      if (this.gameWon()) {
        WorldImage happyFace =  
            new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), 4, 3,
                new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), -6, 3,
                    new OverlayOffsetImage(
                        new RectangleImage(20, 13, "solid", Color.yellow), 0, 2,
                        new OverlayImage(new CircleImage(8, "outline", Color.black),
                          new OverlayImage(new CircleImage(15, "outline", Color.black), 
                            new CircleImage(15, "solid", Color.yellow))))));
        scene.placeImageXY(happyFace, columns * 20 + 30, 70);
      }
      
      //drawing lose screen
      else if (this.gameLost()) {
        WorldImage xEye = new OverlayImage(new LineImage(new Posn(4, 4), Color.black),
                new LineImage(new Posn(4, -4), Color.black));
        WorldImage deadFace = 
            new OverlayImage(new CircleImage(15, "outline", Color.black), 
            new OverlayOffsetImage(xEye, 5, 3, new OverlayOffsetImage(xEye, -5, 3,
                    new OverlayOffsetImage(
                        new RectangleImage(18, 5, "solid", Color.yellow), 0, -11,
                        new OverlayOffsetImage(
                            new EllipseImage(14, 8, "outline", Color.black), 0, -8,
                            new CircleImage(15, "solid", Color.yellow))))));
        
        scene.placeImageXY(deadFace, columns * 20 + 30, 70);
      }
      
      else {
        WorldImage face =  
            new OverlayOffsetImage(new LineImage(new Posn(12, 0), Color.black), 0, -4,
                new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), 4, 3,
                    new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), -6, 3,
                        new OverlayImage(new CircleImage(15, "outline", Color.black), 
                            new CircleImage(15, "solid", Color.yellow)))));
        scene.placeImageXY(face, columns * 20 + 30, 70);
      }
    }
    return scene;
  }
  
  //Effect: handles a click, if its a left click then the cell
  //clicked is revealed, if its a right click the cell is flagged
  public void onMouseClicked(Posn pos, String buttonName) {
    int screenWidth = columns * 40 + 60;
    int screenHeight = rows * 40 + 170; 
    
    //init screen click options
    //difficulty selected
    if (initScreen && buttonName.equals("LeftButton")) {
      if (380 < pos.x && pos.x < 880) {
        if (265 < pos.y && pos.y < 335) {
          initScreen = false;
          rows = 9;
          columns = 9;
          mines = 10;
          this.newGame();
        }
        else if (425 < pos.y && pos.y < 495) {
          initScreen = false;
          rows = 16;
          columns = 16;
          mines = 35;
          this.newGame();
        }
        else if (585 < pos.y && pos.y < 655) {
          initScreen = false;
          rows = 16;
          columns = 30;
          mines = 70;
          this.newGame();
        }
      }
      
      //quit game pressed
      if (45 < pos.x && pos.x < 155 && 45 < pos.y && pos.y < 95) {
        this.endOfWorld("Thanks for playing Minesweeper!!");
      }
    }
    
    //else draw game
    else {
      //if game isn't over, allow player to click cells
      if (!gameWon() && !gameLost() && 30 < pos.x && pos.x < screenWidth - 30 
          && 140 < pos.y && pos.y < screenHeight - 30) {
        int row = (pos.y - 140) / 40;
        int col = (pos.x - 30) / 40;
        Cell clickedCell = this.grid.get(row).get(col);
        
        if (buttonName.equals("LeftButton")) {
          clickedCell.revealed = true;
          if (!clickedCell.mine && clickedCell.countMines() == 0) {
            clickedCell.revealChain();
          } 
        }
        
        if (buttonName.equals("RightButton")) {
          //flags unflagged cells, and unflags flagged cells
          if (!clickedCell.revealed) {
            clickedCell.flagged = !clickedCell.flagged;
          }
          
          //if right clicked numbered cell with correct number of flags labeled, reveal neighbors
          else if (clickedCell.revealed && clickedCell.correctFlags() && !clickedCell.mine) {
            for (Cell c: clickedCell.neighbors) {
              if (!c.flagged) {
                c.revealed = true;
                if (!c.mine && c.countMines() == 0) {
                  c.revealChain();
                }
              }
            }
          }
        }
      }
      
      //if player clicks the face/restart button, start new game
      if (buttonName.equals("LeftButton") && (columns * 20 + 5) < pos.x 
          && pos.x < (columns * 20 + 55)
          && 45 < pos.y && pos.y < 95) {
        this.newGame();
      }
      
      //if player clicks the main menu button, return to main menu
      if (buttonName.equals("LeftButton") && (columns * 10 - 40) < pos.x 
          && pos.x < (columns * 10 + 60)
          && 50 < pos.y && pos.y < 90) {
        this.rows = 16;
        this.columns = 30;
        this.initScreen = true;
      }
    }
  }

  
  //displays the last scene showing the user has lost
  public WorldScene lastScene(String msg) {
    int width = 1260;
    int height = 810;
    WorldScene scene = new WorldScene(width, height);
    
    //placing borders 
    scene.placeImageXY(
        new OverlayImage(new RectangleImage(width - 10, height - 10, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                        new RectangleImage(width - 10, height - 10,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width, height, "solid", Color.white))))), 
        width / 2, height / 2);
    
    scene.placeImageXY(
        new OverlayImage(
            new RectangleImage(width - 60, height - 60, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(width - 60, height - 60,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width - 50, height - 50, "solid", Color.white))))), 
        width / 2, height / 2);
    
    //display words
    scene.placeImageXY(new TextImage(msg, 40, Color.black), width / 2, height / 2);
    return scene;
  }
  
  //starts a new game by clearing the grid and initializing grid again and making a new random
  public void newGame() {
    this.grid = new ArrayList<ArrayList<Cell>>();
    this.rand = new Random();
    this.makeGrid();
    this.placeMines();
    this.setNeighbors();
  }
  
  //returns if all the cells that aren't mines are revealed
  public boolean gameWon() {
    int count = 0;
    for (int i = 0; i < rows; i = i + 1) {
      for (int n = 0; n < columns; n = n + 1) {
        Cell selectedCell = grid.get(i).get(n);
        if (!selectedCell.mine && selectedCell.revealed) {
          count++;
        }
      }
    }
    //flag all mines if game is won
    if (count == rows * columns - mines) {
      for (int x = 0; x < rows; x = x + 1) {
        for (int y = 0; y < columns; y = y + 1) {
          if (grid.get(x).get(y).mine) {
            grid.get(x).get(y).flagged = true;
          }
        }
      }
      return true;
    }
    else {
      return false;
    }
  }
  
  //returns if any mines are revealed
  public boolean gameLost() {
    for (int i = 0; i < rows; i = i + 1) {
      for (int n = 0; n < columns; n = n + 1) {
        Cell selectedCell = grid.get(i).get(n);
        if (selectedCell.mine && selectedCell.revealed) {
          //if a mine is revealed on the board, reveal all mines
          for (int x = 0; x < rows; x = x + 1) {
            for (int y = 0; y < columns; y = y + 1) {
              if (grid.get(x).get(y).mine) {
                grid.get(x).get(y).revealed = true;
              }
            }
          }
          return true;
        }
      }
    }
    return false;
  }
}

//represents a cell
class Cell {
  boolean revealed;
  boolean mine;
  boolean flagged;
  ArrayList<Cell> neighbors;
  
  Cell() {
    this.revealed = false;
    this.mine = false;
    this.flagged = false;
    this.neighbors =  new ArrayList<Cell>();
  }
  
  //adds the neighbors of this cell on game initialization
  void addNeighbors(ArrayList<ArrayList<Cell>> grid, int row, int col) {
    //looping starting from either 0 or the row before given row/column, 
    //ends when it gets to edge of board or two rows past the given row/column
    for (int i = Math.max(0, row - 1); i < Math.min(grid.size(), row + 2); i++) {
      for (int j = Math.max(0, col - 1); j < Math.min(grid.get(i).size(), col + 2); j++) {
        neighbors.add(grid.get(i).get(j));
      }
    }
    //remove the cell itself from its neighbors
    neighbors.remove(grid.get(row).get(col));
  }
  
  //counts the neighboring mines of this cell
  public int countMines() {
    int count = 0;
    for (Cell c : neighbors) {
      if (c.mine) {
        count++;
      }
    }
    return count;
  }
  
  //starts a reveal chain for flood fill which reveals
  //all the neighbors and continues the chain if that neighbor
  //has no mines as neighbors
  void revealChain() {
    for (Cell c : neighbors) {
      if (!c.revealed) {
        c.revealed = true;
        if (c.countMines() == 0) {
          c.revealChain();
        }
      }
    }
  }
  
  //returns true if all the cells that aren't mines are revealed
  public boolean correctFlags() {
    int count = 0;
    for (Cell c : neighbors) {
      if (c.flagged) {
        count++;
      }
    }
    return count == this.countMines();
  }
  
  //creates an image of the cell 
  public WorldImage cellImage() {
    WorldImage cell = new OverlayImage(new RectangleImage(40, 40, "outline", Color.gray),
        new RectangleImage(40, 40, "solid", Color.lightGray));
    WorldImage hiddenCell = new OverlayImage(
        new RectangleImage(30, 30, "solid", Color.lightGray), 
        new OverlayImage(
            new TriangleImage(new Posn(0, 0), new Posn(0, 40), 
                new Posn(40, 0),"solid", Color.white),
            new TriangleImage(new Posn(40, 40), new Posn(0, 40), 
                new Posn(40, 0),"solid", Color.gray)));
    WorldImage mineCell = new OverlayImage(new CircleImage(12, "solid", Color.black), cell);
    WorldImage flaggedCell = new OverlayOffsetImage(
            new OverlayOffsetImage(
                new RotateImage(new EquilateralTriangleImage(13, "solid", Color.red), -90), 3, 6,
                new RectangleImage(3, 19, "solid", Color.black)), 4, 0,
        hiddenCell);
    WorldImage oneCell = new TextImage("1", 22, FontStyle.BOLD, Color.blue);
    WorldImage twoCell = new TextImage("2", 22, FontStyle.BOLD, Color.green.darker());
    WorldImage threeCell = new TextImage("3", 22, FontStyle.BOLD, Color.red);
    WorldImage fourCell = new TextImage("4", 22, FontStyle.BOLD, Color.blue.darker());
    WorldImage fiveUpCell = new TextImage(
        Integer.toString(this.countMines()), 22, FontStyle.BOLD, Color.red.darker());
    
    if (flagged) {
      return flaggedCell;
    }
    else if (!revealed) {
      return hiddenCell;
    }
    else if (mine) {
      return mineCell;
    }
    else {
      if (this.countMines() == 0) {
        return cell;
      }
      else if (this.countMines() == 1) {
        return new OverlayImage(oneCell, cell);
      }
      else if (this.countMines() == 2) {
        return new OverlayImage(twoCell, cell);
      }
      else if (this.countMines() == 3) {
        return new OverlayImage(threeCell, cell);
      }
      else if (this.countMines() == 4) {
        return new OverlayImage(fourCell, cell);
      }
      else {
        return new OverlayImage(fiveUpCell, cell);
      }
    }
  }
}

//examples class
class ExamplesMinesweeper {
  //3x3 test world with everything revealed, with one of each type of cell
  MSWorld test;
  //same as test, but with everything hidden
  MSWorld testHidden;
  //3x3 world with one mine
  MSWorld testOneMine;
  //30 x 16 world with 99 mines
  MSWorld standard;
  //world for user to play on 
  MSWorld play;
  
  WorldImage cell = new OverlayImage(new RectangleImage(40, 40, "outline", Color.gray),
      new RectangleImage(40, 40, "solid", Color.lightGray));
  WorldImage hiddenCell = new OverlayImage(
      new RectangleImage(30, 30, "solid", Color.lightGray), 
      new OverlayImage(
          new TriangleImage(new Posn(0, 0), new Posn(0, 40), 
              new Posn(40, 0),"solid", Color.white),
          new TriangleImage(new Posn(40, 40), new Posn(0, 40), 
              new Posn(40, 0),"solid", Color.gray)));
  WorldImage mineCell = new OverlayImage(new CircleImage(12, "solid", Color.black), cell);
  WorldImage flaggedCell = new OverlayOffsetImage(
          new OverlayOffsetImage(
              new RotateImage(new EquilateralTriangleImage(13, "solid", Color.red), -90), 3, 6,
              new RectangleImage(3, 19, "solid", Color.black)), 4, 0,
      hiddenCell);
  WorldImage oneCell = new TextImage("1", 22, FontStyle.BOLD, Color.blue);
  WorldImage twoCell = new TextImage("2", 22, FontStyle.BOLD, Color.green.darker());
  WorldImage threeCell = new TextImage("3", 22, FontStyle.BOLD, Color.red);
  WorldImage fourCell = new TextImage("4", 22, FontStyle.BOLD, Color.blue.darker());
  
  //initializing world
  void init() {
    this.test = new MSWorld(true);
    this.testHidden = new MSWorld(3, 3, 2, new Random(0));
    this.testOneMine = new MSWorld(3, 3, 1, new Random(0));
    this.standard = new MSWorld(16, 30, 40);
    this.play = new MSWorld();
  }
  
  //tests makeGrid by checking dimensions of given worlds
  void testMakeGrid(Tester t) {
    //initializing world, including makeGrid
    this.init();
    //tests the number of rows and columns in test
    t.checkExpect(test.grid.size(), 3);
    t.checkExpect(test.grid.get(0).size(), 3);
    t.checkExpect(testHidden.grid.size(), 3);
    t.checkExpect(testHidden.grid.get(0).size(), 3);
    //tests rows and columns in standard
    t.checkExpect(standard.grid.size(), 16);
    t.checkExpect(standard.grid.get(0).size(), 30);
  }
  
  //tests placeMines
  void testPlaceMines(Tester t) {
    //initializing world, including placeMines
    this.init();
    //test world should only have mines at (2, 1) and (3, 2) in the given seed
    t.checkExpect(test.grid.get(0).get(1).mine, true);
    t.checkExpect(test.grid.get(1).get(2).mine, true);
    t.checkExpect(test.grid.get(0).get(0).mine, false);
    t.checkExpect(test.grid.get(2).get(2).mine, false);
    t.checkExpect(testHidden.grid.get(0).get(1).mine, true);
    t.checkExpect(testHidden.grid.get(1).get(2).mine, true);
    t.checkExpect(testHidden.grid.get(0).get(0).mine, false);
    t.checkExpect(testHidden.grid.get(2).get(2).mine, false);
  }
  
  //tests countMines in Cell by going through each non-mine cell
  void testCountMines(Tester t) {
    //initializing world
    this.init();
    t.checkExpect(test.grid.get(0).get(0).countMines(), 1);
    t.checkExpect(test.grid.get(0).get(2).countMines(), 2);
    t.checkExpect(test.grid.get(1).get(0).countMines(), 1);
    t.checkExpect(test.grid.get(1).get(1).countMines(), 2);
    t.checkExpect(test.grid.get(2).get(0).countMines(), 0);
    t.checkExpect(test.grid.get(2).get(1).countMines(), 1);
    t.checkExpect(test.grid.get(2).get(2).countMines(), 1);
  }
  
  //tests setNeighbors by going through each cell
  void testSetNeighbors(Tester t) {
    //initializing world, including setNeighbors
    this.init();
    t.checkExpect(test.grid.get(0).get(0).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(0).get(1),
            test.grid.get(1).get(0),
            test.grid.get(1).get(1))));
    t.checkExpect(test.grid.get(1).get(1).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(0).get(0),
            test.grid.get(0).get(1),
            test.grid.get(0).get(2),
            test.grid.get(1).get(0),
            test.grid.get(1).get(2),
            test.grid.get(2).get(0),
            test.grid.get(2).get(1),
            test.grid.get(2).get(2))));
    t.checkExpect(test.grid.get(2).get(2).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(1).get(1),
            test.grid.get(1).get(2),
            test.grid.get(2).get(1))));
  }
  
  //tests addNeighbors in Cell
  void testAddNeighbors(Tester t) {
    //initializes world including adding neighbors to each cell
    this.init();
    t.checkExpect(test.grid.get(0).get(0).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(0).get(1),
            test.grid.get(1).get(0),
            test.grid.get(1).get(1))));
    t.checkExpect(test.grid.get(1).get(1).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(0).get(0),
            test.grid.get(0).get(1),
            test.grid.get(0).get(2),
            test.grid.get(1).get(0),
            test.grid.get(1).get(2),
            test.grid.get(2).get(0),
            test.grid.get(2).get(1),
            test.grid.get(2).get(2))));
    t.checkExpect(test.grid.get(2).get(2).neighbors, 
        new ArrayList<Cell>(Arrays.asList(
            test.grid.get(1).get(1),
            test.grid.get(1).get(2),
            test.grid.get(2).get(1))));
  }
  
  //tests makeScene
  void testMakeScene(Tester t) {
    //initializing world, including makeScene
    this.init();
    
    //constants for all the borders in the test world
    int width = 180;
    int height = 290;
    WorldImage worldBorders = 
        new OverlayImage(new RectangleImage(width - 10, height - 10, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                        new RectangleImage(width - 10, height - 10,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width, height, "solid", Color.white)))));
    
    WorldImage statBorders = 
        new OverlayImage(
            new RectangleImage(width - 60, 80, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(width - 60, 80,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width - 50, 90, "solid", Color.white))))); 
    
    WorldImage cellBorders = 
        new OverlayImage(
            new RectangleImage(120, 120, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(120, 120, "solid", Color.gray), 0, 0, 
                        new RectangleImage(130, 130, "solid", Color.white))))); 
    
    WorldImage restartButton = 
        new OverlayImage(new RectangleImage(40, 40, "solid", Color.lightGray), 
            new OverlayImage(new TriangleImage(new Posn(0, 0), new Posn(0, 50), 
                new Posn(50, 0),"solid", Color.white),
                new TriangleImage(new Posn(50, 50), new Posn(0, 50), 
                    new Posn(50, 0),"solid", Color.gray)));
    
    WorldImage face = 
        new OverlayImage(  
            new OverlayOffsetImage(new LineImage(new Posn(12, 0), Color.black), 0, -4,
                new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), 4, 3,
                    new OverlayOffsetImage(new RectangleImage(3, 3, "solid", Color.black), -6, 3,
                        new OverlayImage(new CircleImage(15, "outline", Color.black), 
                            new CircleImage(15, "solid", Color.yellow))))),
            new OverlayImage(new RectangleImage(40, 40, "solid", Color.lightGray), 
            new OverlayImage(new TriangleImage(new Posn(0, 0), new Posn(0, 50), 
                new Posn(50, 0),"solid", Color.white),
                new TriangleImage(new Posn(50, 50), new Posn(0, 50), 
                    new Posn(50, 0),"solid", Color.gray))));
    
    WorldImage xEye = new OverlayImage(new LineImage(new Posn(4, 4), Color.black),
        new LineImage(new Posn(4, -4), Color.black));
    WorldImage deadFace = 
        new OverlayImage(new CircleImage(15, "outline", Color.black), 
        new OverlayOffsetImage(xEye, 5, 3, new OverlayOffsetImage(xEye, -5, 3,
                new OverlayOffsetImage(new RectangleImage(18, 5, "solid", Color.yellow), 0, -11,
                    new OverlayOffsetImage(new EllipseImage(14, 8, "outline", Color.black), 0, -8,
                        new CircleImage(15, "solid", Color.yellow))))));
    
    //testing a hidden 3x3 test world
    WorldScene hidden3x3 = new WorldScene(180, 290);
    hidden3x3.placeImageXY(worldBorders, 90, 145);
    hidden3x3.placeImageXY(statBorders, 90, 70);
    hidden3x3.placeImageXY(cellBorders, 90, 200);
    hidden3x3.placeImageXY(restartButton, 90, 70);
    hidden3x3.placeImageXY(face, 90, 70);
    hidden3x3.placeImageXY(new TextImage("7", 18, Color.black), 130, 72);
    hidden3x3.placeImageXY(hiddenCell, 50, 160);
    hidden3x3.placeImageXY(hiddenCell, 50, 200);
    hidden3x3.placeImageXY(hiddenCell, 50, 240);
    hidden3x3.placeImageXY(hiddenCell, 90, 160);
    hidden3x3.placeImageXY(hiddenCell, 90, 200);
    hidden3x3.placeImageXY(hiddenCell, 90, 240);
    hidden3x3.placeImageXY(hiddenCell, 130, 160);
    hidden3x3.placeImageXY(hiddenCell, 130, 200);
    hidden3x3.placeImageXY(hiddenCell, 130, 240);
    t.checkExpect(testHidden.makeScene(), hidden3x3);
    
    //testing a 3x3 world with everythign shown
    WorldScene test3x3 = new WorldScene(180, 290);
    test3x3.placeImageXY(worldBorders, 90, 145);
    test3x3.placeImageXY(statBorders, 90, 70);
    test3x3.placeImageXY(cellBorders, 90, 200);
    test3x3.placeImageXY(restartButton, 90, 70);
    test3x3.placeImageXY(deadFace, 90, 70);
    test3x3.placeImageXY(new TextImage("1", 18, Color.black), 130, 72);
    test3x3.placeImageXY(new OverlayImage(
        new TextImage("1", 22, FontStyle.BOLD, Color.blue), 
        cell), 50, 160);
    test3x3.placeImageXY(flaggedCell, 90, 160);
    test3x3.placeImageXY(new OverlayImage(
        new TextImage("2", 22, FontStyle.BOLD, Color.green.darker()), 
        cell), 130, 160);
    test3x3.placeImageXY(hiddenCell, 50, 200);
    test3x3.placeImageXY(new OverlayImage(
        new TextImage("2", 22, FontStyle.BOLD, Color.green.darker()), 
        cell), 90, 200);
    test3x3.placeImageXY(mineCell, 130, 200);
    test3x3.placeImageXY(cell, 50, 240);
    test3x3.placeImageXY(new OverlayImage(
        new TextImage("1", 22, FontStyle.BOLD, Color.blue), 
        cell), 90, 240);
    test3x3.placeImageXY(new OverlayImage(
        new TextImage("1", 22, FontStyle.BOLD, Color.blue), 
        cell), 130, 240);
    t.checkExpect(test.makeScene(), test3x3);
  }
  
  //tests CellImage in cell
  void testCellImage(Tester t) {
    //initializing world
    this.init();
    t.checkExpect(test.grid.get(0).get(0).cellImage(), new OverlayImage(oneCell, cell));
    t.checkExpect(test.grid.get(0).get(1).cellImage(), flaggedCell);
    t.checkExpect(test.grid.get(0).get(2).cellImage(), new OverlayImage(twoCell, cell));
    t.checkExpect(test.grid.get(1).get(0).cellImage(), hiddenCell);
    t.checkExpect(test.grid.get(1).get(2).cellImage(), mineCell);
    t.checkExpect(test.grid.get(2).get(0).cellImage(), cell);
  }
  
  //tests correctFlags in cell
  void testCorrectFlags(Tester t) {
    this.init();
    //in revealed test world, top left cell correctly has one flag revealed around it
    t.checkExpect(test.grid.get(0).get(0).correctFlags(), true);
    t.checkExpect(test.grid.get(1).get(1).correctFlags(), false);
    t.checkExpect(testHidden.grid.get(0).get(0).correctFlags(), false);
    t.checkExpect(testHidden.grid.get(1).get(1).correctFlags(), false);
  }
  
  //tests the reveal chain method in cell
  void testRevealChain(Tester t) {
    this.init();
    //testing reveal chain on the bottom left cell of test hidden where
    //all neighboring cells have numbers
    testHidden.grid.get(2).get(0).revealChain();
    //setting the surrounding cells to revealed on test hidden 
    t.checkExpect(testHidden.grid.get(1).get(0).revealed, true);
    t.checkExpect(testHidden.grid.get(1).get(1).revealed, true);
    t.checkExpect(testHidden.grid.get(2).get(1).revealed, true);
    
    //testing reveal chain where a neighboring cell is also empty
    testOneMine.grid.get(2).get(0).revealChain();
    t.checkExpect(testOneMine.grid.get(2).get(0).revealed, true);
    t.checkExpect(testOneMine.grid.get(2).get(1).revealed, true);
    t.checkExpect(testOneMine.grid.get(2).get(2).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(0).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(1).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(2).revealed, true);
  }
  
  //tests the gamewon method in MSWorld
  void testGameWon(Tester t) {
    this.init();
    t.checkExpect(testHidden.gameWon(), false);
    
    t.checkExpect(test.gameWon(), false);
    test.grid.get(1).get(0).revealed = true;
    test.grid.get(0).get(1).revealed = true;
    t.checkExpect(test.gameWon(), true);
  }
  
  //tests the gamelost method in MSWorld
  void testGameLost(Tester t) {
    this.init();
    t.checkExpect(testHidden.gameLost(), false);
    testHidden.grid.get(0).get(1).revealed = true;
    t.checkExpect(testHidden.gameLost(), true);
  }
  
  
  //tests the newGame method in MSWorld
  void testNewGame(Tester t) {
    this.init();
    t.checkExpect(test.grid.get(0).get(0).revealed, true);
    t.checkExpect(test.grid.get(0).get(1).flagged, true);
    test.newGame();
    t.checkExpect(test.grid.get(0).get(0).revealed, false);
    t.checkExpect(test.grid.get(0).get(1).flagged, false);
  }
  
  //tests onMouseClick
  void testOnMouseClicked(Tester t) {
    this.init();
    //checking if reveal left button works on a normal cell
    this.testHidden.onMouseClicked(new Posn(40, 160), "LeftButton");
    t.checkExpect(testHidden.grid.get(0).get(0).revealed, true);
    
    //checking if flagging works
    this.testHidden.onMouseClicked(new Posn(100, 160), "RightButton");
    t.checkExpect(testHidden.grid.get(0).get(1).flagged, true);
    
    //checking if reveal chain is executed
    this.testOneMine.onMouseClicked(new Posn(40, 240), "LeftButton");
    t.checkExpect(testOneMine.grid.get(2).get(0).revealed, true);
    t.checkExpect(testOneMine.grid.get(2).get(1).revealed, true);
    t.checkExpect(testOneMine.grid.get(2).get(2).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(0).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(1).revealed, true);
    t.checkExpect(testOneMine.grid.get(1).get(2).revealed, true);
    
    //checking if clicking a mine ends the game
    this.testHidden.onMouseClicked(new Posn(100, 160), "RightButton");
    this.testHidden.onMouseClicked(new Posn(100, 160), "LeftButton");
    t.checkExpect(testHidden.grid.get(0).get(1).revealed, true);
    t.checkExpect(testHidden.gameLost(), true);
  
    //checking if winning the game properly executes
    this.test.grid.get(1).get(2).mine = false;
    this.test.onMouseClicked(new Posn(50, 200), "LeftButton");
    t.checkExpect(test.gameWon(), true);
    
    //checking to choose difficulty on initial difficulty screen for easy
    this.play.onMouseClicked(new Posn(500, 270), "LeftButton");
    t.checkExpect(this.play.columns, 9);
    
    //checking to choose difficulty on initial difficulty screen for medium
    this.play = new MSWorld();
    this.play.onMouseClicked(new Posn(500, 430), "LeftButton");
    t.checkExpect(this.play.columns, 16);
    
    //checking to choose difficulty on initial difficulty screen for hard
    this.play = new MSWorld();
    this.play.onMouseClicked(new Posn(500, 590), "LeftButton");
    t.checkExpect(this.play.columns, 30);
  
  }
  
  //tests the last scene method
  void testLastScene(Tester t) {
    this.init();
    
    int width = 1260;
    int height = 810;
    WorldScene endScreen = new WorldScene(width, height);
    
    endScreen.placeImageXY(
        new OverlayImage(new RectangleImage(1250, 800, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(10, 10), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.BOTTOM,
                        new RectangleImage(1250, 800,"solid", Color.gray), 0, 0, 
                        new RectangleImage(1260, 810, "solid", Color.white))))), 
        width / 2, height / 2);
    
    endScreen.placeImageXY(
        new OverlayImage(
            new RectangleImage(width - 60, height - 60, "solid", Color.lightGray), 
            new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP, 
                new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                    "solid", Color.gray), 0, 0, 
                new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.BOTTOM,
                    new TriangleImage(new Posn(10, 0), new Posn(0, 0), new Posn(0, 10),
                        "solid", Color.gray), 0, 0, 
                    new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
                        new RectangleImage(width - 60, height - 60,"solid", Color.gray), 0, 0, 
                        new RectangleImage(width - 50, height - 50, "solid", Color.white))))), 
        width / 2, height / 2);
    
    endScreen.placeImageXY(new TextImage("Thanks for playing Minesweeper!", 40, Color.black), 
        width / 2, height / 2);
    t.checkExpect(test.lastScene("Thanks for playing Minesweeper!"), endScreen);
    t.checkExpect(play.lastScene("Thanks for playing Minesweeper!"), endScreen);
  }
  
  
  //tests the given test world with everything shown
  void testBigBang(Tester t) {
    this.init();
    play.bigBang(1260, 810, 0.01);
  }
}
