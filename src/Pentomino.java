/**
 * Created by Rico Montulet on 3-11-2014.
 */

/*
    004
    changed name of dropDown() to dropFallingPENTOMINO

*/


import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class Pentomino {
    private Color[][] board;
    private static final int rows = 15;
    private static final int cols = 8;

    private static final boolean[][] P_PIECE = {
            {true, true},
            {true, true},
            {true, false}};
    private static final boolean[][] X_PIECE = {
            {false, true, false},
            {true, true, true},
            {false, true, false}};
    private static final boolean[][] U_PIECE = {
            {true, false, true},
            {true, true, true}};
    private static final boolean[][] F_PIECE = {
            {false, true, true},
            {true, true, false},
            {false, true, false}};
    private static final boolean[][] I_PIECE = {
            {true},
            {true},
            {true},
            {true},
            {true}};
    private static final boolean[][] W_PIECE = {
            {true, false, false},
            {true, true, false},
            {false, true, true}};
    private static final boolean[][] T_PIECE = {
            {true, true, true},
            {false, true, false},
            {false, true, false}};
    private static final boolean[][] Z_PIECE = {
            {true, true, false},
            {false, true, false},
            {false, true, true}};
    private static final boolean[][] N_PIECE = {
            {true, false},
            {true, false},
            {true, true},
            {false, true}};
    private static final boolean[][] V_PIECE = {
            {true, false, false},
            {true, false, false},
            {true, true, true}};
    private static final boolean[][] Y_PIECE = {
            {false, true},
            {true, true},
            {false, true},
            {false, true}};
    private static final boolean[][] L_PIECE = {
            {true, false},
            {true, false},
            {true, false},
            {true, true}};

    private static boolean[][][] PENTOMINO_PIECES = {P_PIECE, X_PIECE, U_PIECE, F_PIECE, I_PIECE, W_PIECE, T_PIECE, Z_PIECE, N_PIECE, V_PIECE, Y_PIECE, L_PIECE};
    private boolean alive = true;
    private int score;
    private boolean[][] fallingPENTOMINO;
    private Color fallingPENTOMINOColor;
    private int fallingPENTOMINORow=0;
    private int fallingPENTOMINOCol=0;
    private int fallingPENTOMINORows=0;
    private int fallingPENTOMINOCols=0;
    private boolean[][] nextPENTOMINO;
    private Color nextPENTOMINOColor;
    private int nextPENTOMINORow=0;
    private int nextPENTOMINOCol=0;
    private int nextPENTOMINORows=0;
    private int nextPENTOMINOCols=0;
    private final int[] scoreArray = {0,2,3,8,12,20};
    private static final Color[] COLORS = {Color.magenta, Color.blue, Color.cyan, Color.green, Color.yellow, Color.red,
            Color.orange, Color.pink, Color.black,
            new Color(110, 128, 61),
            new Color(255, 182,139),
            new Color(0, 91, 47)};
    //purple            darkgreenish
    private Color empty = Color.white;
    private String saveDataPath;
    private ArrayList<Score> highScores;

    public Pentomino() {
        try{
            saveDataPath = Pentomino.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            saveDataPath += "saveData.dat";
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.board = new Color[rows][cols];
        //System.out.println(getHiScore(1));
        resetGame();
        readHighScores();
        // Should also pull in the highscore from a file??
    }

    private void readHighScores() {
        try {
            FileInputStream in = new FileInputStream(saveDataPath);
            ObjectInputStream objectin = new ObjectInputStream(in);
            highScores=(ArrayList<Score>) objectin.readObject();
        } catch (IOException e) {
            highScores=new ArrayList<Score>();
        } catch (ClassNotFoundException e) {
            highScores=new ArrayList<Score>();
        }
    }

    /**
	* Appends (notice, not overwrite) the name and the score of a person into the file.
	* Works just fine
	*/
    
    public String getHiScore(int position){
        if (position<highScores.size())
            return highScores.get(position).toString();
        return "No score at the "+(position+1)+"th position";
	}

    public void writeScore(String name){
        Score newScore = new Score(name, score);
        highScores.add(newScore);
        Collections.sort(highScores);
        try {
            FileOutputStream out = new FileOutputStream(saveDataPath);
            ObjectOutputStream objectin = new ObjectOutputStream(out);
            objectin.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    public int getBoardWidth() {
        return cols;
    }

    public int getBoardHeight() {
        return rows;
    }

    public Color[][] getBoard() {
        return board;
    }

    public boolean getalive() {
        return alive;
    }

    public int getScore() {
        return score;
    }

    public int getFallingPENTOMINORows() {
        return fallingPENTOMINORows;
    }

    public int getFallingPENTOMINOCols() {
        return fallingPENTOMINOCols;
    }

    public int getFallingPENTOMINOCol() {
        return fallingPENTOMINOCol;
    }

    public int getFallingPENTOMINORow() {
        return fallingPENTOMINORow;
    }

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public Color getFallingPENTOMINOColor() {
        return fallingPENTOMINOColor;
    }

    public boolean[][] getNextPENTOMINO() {
        return nextPENTOMINO;
    }

    public Color getNextPENTOMINOColor() {
        return nextPENTOMINOColor;
    }

    public int getNextPENTOMINORow() {
        return nextPENTOMINORow;
    }

    public int getNextPENTOMINOCol() {
        return nextPENTOMINOCol;
    }

    public int getNextPENTOMINOCols() {
        return nextPENTOMINOCols;
    }

    public int getNextPENTOMINORows() {
        return nextPENTOMINORows;
    }

    /**
     * Creates a new pentomino to fall.
     */
    public void startFallingPentomino() {
        // JONAS

        // This is assuming that the last penotmino has been placed and overwriting over fallingPENTOMINO will not fuck shit up

        // Uses the next integer of random number mulitplied by 12
        // (or whatever the current length of PENTOMINO_PIECES is, anyway)
        int shape_id = (int) Math.ceil(Math.random() * PENTOMINO_PIECES.length);
        // Takes the random number and sets the corresponding pentomino to the currently falling pentomino
        this.fallingPENTOMINOColor = COLORS[shape_id - 1];
        this.fallingPENTOMINO = PENTOMINO_PIECES[shape_id - 1];
        this.fallingPENTOMINORows = this.fallingPENTOMINO.length;
        this.fallingPENTOMINOCols = this.fallingPENTOMINO[0].length;
        this.fallingPENTOMINORow = 0;
        this.fallingPENTOMINOCol = (cols / 2 - fallingPENTOMINOCols / 2);
        shape_id = (int) Math.ceil(Math.random() * PENTOMINO_PIECES.length);
        nextPENTOMINO = PENTOMINO_PIECES[shape_id - 1];
        nextPENTOMINOColor = COLORS[shape_id - 1];;
        nextPENTOMINORow = 0;
        nextPENTOMINOCol = (cols / 2 - fallingPENTOMINOCols / 2);
        nextPENTOMINORows = this.nextPENTOMINO.length;
        nextPENTOMINOCols = this.nextPENTOMINO[0].length;
    }
    public void newFallingPentomino(){
        this.fallingPENTOMINOColor = nextPENTOMINOColor;
        this.fallingPENTOMINO = nextPENTOMINO;
        this.fallingPENTOMINORows = nextPENTOMINORows;
        this.fallingPENTOMINOCols = nextPENTOMINOCols;
        this.fallingPENTOMINORow = nextPENTOMINORow;
        this.fallingPENTOMINOCol = nextPENTOMINOCol;
        int shape_id = (int) Math.ceil(Math.random() * PENTOMINO_PIECES.length);
        nextPENTOMINO = PENTOMINO_PIECES[shape_id - 1];
        nextPENTOMINOColor = COLORS[shape_id - 1];;
        nextPENTOMINORow = 0;
        nextPENTOMINOCol = (cols / 2 - fallingPENTOMINOCols / 2);
        nextPENTOMINORows = this.nextPENTOMINO.length;
        nextPENTOMINOCols = this.nextPENTOMINO[0].length;
        if (!fallingPENTOMINOIsLegal()){
            alive=false;
        }
    }

    /**
     * Clears the board.
     */
    public void resetGame() {
        //Jonas replaced "row" and "col" for "i" and "j" to avoid confusion
        //Loops through the board array, replacing any value with the empty one
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = empty;
            }
        }
        //Resets state and score
        this.alive = true;
        this.score = 0;

        // Asks for a new pentomino
        startFallingPentomino();
    }

    /**
     * Determines the coordinates when a key is pressed.
     *
     * @param action which key is pressed.
     */
    public void pressedKey(PentominoAction action) {
        //Aside from only keys for the pentomino actions, this should also react to other things



        switch (action) {
            case RESET: {
                resetGame();
                break;
            }
            case LEFT: {
                moveFallingPENTOMINO(0, -1);
                break;
            }
            case RIGHT: {
                moveFallingPENTOMINO(0, 1);
                break;
            }
            case DOWN: {
                if (!moveFallingPENTOMINO(1, 0)){
                    droppedPENTOMINO();
                }
                break;
            }
            case UP: {
                rotateFallingPENTOMINO();
                break;
            }
            case SPACE: {
                dropFallingPENTOMINO();
                break;
            }
            case CHEAT: {
                cheatIblock();
                break;
            }
            default: {
                throw new RuntimeException("undef action: " + action);
            }
        }
    }

    private void cheatIblock() {
        this.fallingPENTOMINOColor = COLORS[4];
        this.fallingPENTOMINO = PENTOMINO_PIECES[4];
        this.fallingPENTOMINORows = this.fallingPENTOMINO.length;
        this.fallingPENTOMINOCols = this.fallingPENTOMINO[0].length;
        this.fallingPENTOMINORow = 0;
        this.fallingPENTOMINOCol = (cols / 2 - fallingPENTOMINOCols / 2);
        if (!fallingPENTOMINOIsLegal()){
            alive=false;
        }
    }

    private void droppedPENTOMINO() {
        for (int row = 0; row < this.fallingPENTOMINORows; row++) {
            for (int col = 0; col < this.fallingPENTOMINOCols; col++) {
                if (this.fallingPENTOMINO[row][col]) {
                    this.board[(row + this.fallingPENTOMINORow)][(col + this.fallingPENTOMINOCol)] = this.fallingPENTOMINOColor;
                }
            }
        }
        removeFullRows();
        newFallingPentomino();
    }

    private void removeFullRows() {
        int newRow = rows - 1;
        int fullRows = 0;
        for (int oldRow = rows - 1; oldRow >= 0; oldRow--) {
            if (isRowFull(oldRow)) {
                fullRows++;
            } else {
                if (oldRow != newRow) {
                    for (int col = 0; col < cols; col++) {
                        board[newRow][col] = board[oldRow][col];
                    }
                }
                newRow--;
            }
        }
        for (int row = 0; row < fullRows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = empty;
            }
        }
        this.score += scoreArray[fullRows];
    }

    public boolean isRowFull(int row) {
        for (int col = 0; col < cols; col++) {
            if (board[row][col] == empty) {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves the pentomino.
     *
     * @param addrow how many rows to move it.
     * @param addcol how many columns to move it.
     * @return if the move is allowed.
     */
    public boolean moveFallingPENTOMINO(int addrow, int addcol) {
            this.fallingPENTOMINORow += addrow;
            this.fallingPENTOMINOCol += addcol;
        if (!fallingPENTOMINOIsLegal()){
            this.fallingPENTOMINORow -= addrow;
            this.fallingPENTOMINOCol -= addcol;
            return false;
        }
        return true;
    }

    /**
     * Drops the block by means of essentially doing the same as pressing the down key the amount of times "rows" is set to
     */
    public void dropFallingPENTOMINO() {
        // Basically the same as pushing the down key like 15 times :P
        for (int i = 0; i < rows; i++)
            if (fallingPENTOMINOIsLegal())
            moveFallingPENTOMINO(1, 0);
    }

    /**
     * Used to rotate the falling piece CCW.
     */
    public void rotateFallingPENTOMINO() {
        boolean[][] oldPentomino = this.fallingPENTOMINO;
        int oldFallingPentominoRow = this.fallingPENTOMINORow;
        int oldFallingPentominoCol = this.fallingPENTOMINOCol;
        int oldRows = this.fallingPENTOMINORows;
        int oldCols = this.fallingPENTOMINOCols;
        fallingPENTOMINORows=oldCols;
        fallingPENTOMINOCols=oldRows;
        this.fallingPENTOMINORow -= (oldCols - oldRows) / 2;
        this.fallingPENTOMINOCol -= (oldRows - oldCols) / 2;
        this.fallingPENTOMINO = new boolean[oldCols][oldRows];
        for (int oldRow = 0; oldRow < oldRows; oldRow++) {
            for (int oldCol = 0; oldCol < oldCols; oldCol++) {
                int newRow = oldCols - 1 - oldCol;
                this.fallingPENTOMINO[newRow][oldRow] = oldPentomino[oldRow][oldCol];
            }
        }
        if (!fallingPENTOMINOIsLegal()) {
            this.fallingPENTOMINORow = oldFallingPentominoRow;
            this.fallingPENTOMINOCol = oldFallingPentominoCol;
            this.fallingPENTOMINORows = oldRows;
            this.fallingPENTOMINOCols = oldCols;
            this.fallingPENTOMINO = oldPentomino;
        }
    }

    /**
     * Checks whether it is allowed to rotate the piece like this
     *
     * @return whether it is allowed.
     */
    public boolean fallingPENTOMINOIsLegal() {
        for (int row = 0; row < this.fallingPENTOMINORows; row++) {
            for (int col = 0; col < this.fallingPENTOMINOCols; col++) {
                int tempcol = col + this.fallingPENTOMINOCol;
                int temprow = row + this.fallingPENTOMINORow;
                if ((temprow < 0) || (temprow >= rows) || (tempcol < 0) || (tempcol >= cols) || ((this.fallingPENTOMINO[row][col]) && (this.board[temprow][tempcol] != empty))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Function to check if a row has been filled
    // And if it has, it has to remove it, move everything above it down
    // Also has to make scores

    // Function which saves the highscore into a file at the end of a game

    public boolean[][] getFallingPnt() {
        return this.fallingPENTOMINO;
    }


}

