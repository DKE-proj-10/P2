/**
 * Created by Rico Montulet on 8-11-2014.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.util.*;
import java.util.Timer;

public class Main extends JFrame implements KeyListener{
    private Pentomino pentomino;
    private static final int boardMargin = 18;
    private static final int cellMargin = 3;
    private static final int cellSize = 45;
    private static final int cellNextMargin = 3;
    private static final int cellNextSize = 30;
    private static final Color backgroundColor = Color.lightGray;
    private static final Color gridColor = Color.darkGray;
    private static final Color scoreColor = Color.black;
    private static boolean saved;
    private static int tickCount=0;
    private static JFrame frame;
    private Color empty = Color.white;
    private boolean startGame=false;
    private  boolean onlyOnce=true;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new Main();
                frame.setTitle("Pentomino Tetris");
                frame.setResizable(false);
                frame.setFocusable(true);
                frame.setFocusTraversalKeysEnabled(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
        public Main(){
            pentomino = new Pentomino();
            setSize((pentomino.getBoardWidth() * cellMargin + pentomino.getBoardWidth() * cellSize - cellMargin + 2 * boardMargin)+(5 * cellNextMargin + 5 * cellNextSize - cellNextMargin), ((pentomino.getBoardHeight()) * (cellMargin + cellSize) - cellMargin + 2 * boardMargin));
            setMinimumSize(new Dimension((pentomino.getBoardWidth() * cellMargin + pentomino.getBoardWidth() * cellSize - cellMargin + 2 * boardMargin)+(5 * cellNextMargin + 5 * cellNextSize - cellNextMargin), ((pentomino.getBoardHeight()) * (cellMargin + cellSize) - cellMargin + 2 * boardMargin)));
            addKeyListener(this);
            setFocusable(true);
            setFocusTraversalKeysEnabled(false);
            saved = false;
        }


    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if (startGame) {
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            paintBoard(g2);
            paintNextPiece(g2);
            paintFallingPiece(g2);
            paintScore(g2);
        }
        else {
            paintMenu(g2);
        }
        if (!pentomino.getalive()) {
            paintGameOver(g2);
        }
    }
    private void runTimer(){
        new Timer().schedule(new TimerTask() {
            public void run()  {
                tickCount++;
                int deviser = 0;
                if(pentomino.getScore()>15)
                    deviser=35;
                else if (pentomino.getScore()>10)
                    deviser=50;
                else if (pentomino.getScore()>5)
                    deviser=75;
                else
                    deviser=100;
                if (tickCount%deviser==0 && pentomino.getalive()) {
                    pentomino.pressedKey(PentominoAction.DOWN);
                    frame.repaint();
                }
            }
        }, 0, 10);
    }

    private void paintMenu(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setFont(new Font("Arial", 1, 36));
        g2.drawString("THE ULTIMATE CHALLENGE!", 25, 150);
        g2.drawString("TETRIS WITH PENTOMINOS!", 25, 190);

        g2.setFont(new Font("Arial", 1, 20));
        g2.drawString("PRESS 'ENTER' TO START THE CHALLENGE" , 70, 230);
    }

    public void paintNextPiece(Graphics2D g2){
        boolean[][] nextPentomino = pentomino.getNextPENTOMINO();
        Color[][] nextPieceBoard = new Color[5][5];
        Color nextColor = pentomino.getNextPENTOMINOColor();
        for (int row = 0; row < nextPieceBoard.length; row++) {
            for (int col = 0; col < nextPieceBoard[0].length; col++) {
                int cellLeft = frame.getWidth() - boardMargin - (5-col) * cellNextSize;
                int cellTop = boardMargin + row * cellNextSize;
                g2.setColor(gridColor);
                g2.fillRect(cellLeft, cellTop, cellNextSize, cellNextSize);

                if (col <= nextPentomino[0].length-1 && row <= nextPentomino.length-1 && nextPentomino[row][col]) {
                    int innerSize = cellNextSize - 2 * cellNextMargin;
                    g2.setColor(nextColor);
                    g2.fillRect(cellLeft + cellNextMargin, cellTop + cellNextMargin, innerSize, innerSize);
                }
                else{
                    int innerSize = cellNextSize - 2 * cellNextMargin;
                    g2.setColor(empty);
                    g2.fillRect(cellLeft + cellNextMargin, cellTop + cellNextMargin, innerSize, innerSize);
                }
            }
        }

    }

    public void paintScore(Graphics2D graphics){
        graphics.setColor(scoreColor);
        graphics.setFont(new Font("Arial", 1, 36));
        graphics.drawString(""+pentomino.getScore(),5,getHeight()-10);

    }

    public void paintGameOver(Graphics2D g2){
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.orange);
        g2.setFont(new Font("Arial", 1, 36));
        g2.drawString("Game Over!", 25, 150);

        g2.setFont(new Font("Arial", 1, 20));
        g2.drawString("Press 'r' to reset", 70, 180);
        g2.drawString("Press 'h' to save the score", 70, 200);
        g2.setFont(new Font("Arial", 1, 16));
        g2.drawString("Prev highscores:", 70, 220);
        g2.setFont(new Font("Arial", 1, 13));
        for (int i = 0; i<10;i++) {
            g2.drawString(pentomino.getHiScore(i), 85, 240 + i * 20);
        }
    }

    public void paintFallingPiece(Graphics2D g2) {
        boolean[][] currentPentomino = pentomino.getFallingPnt();
        Color current = pentomino.getFallingPENTOMINOColor();
        for (int row = 0; row < pentomino.getFallingPENTOMINORows(); row++) {
            for (int col = 0; col < pentomino.getFallingPENTOMINOCols(); col++) {
                if (currentPentomino[row][col]) {
                    paintCell(g2, row + pentomino.getFallingPENTOMINORow(), col + pentomino.getFallingPENTOMINOCol(), current);
                }
            }
        }
    }

    public void paintBoard(Graphics2D g2)
    {
        Color[][] board = pentomino.getBoard();
        for (int row = 0; row < Pentomino.getRows(); row++) {
            for (int col = 0; col < Pentomino.getCols(); col++) {
                paintCell(g2, row, col, board[row][col]);
            }
        }
        g2.setColor(Color.black);
        for (int i = 0; i<10;i++) {
            g2.drawString(pentomino.getHiScore(i), frame.getWidth() - 185, 240 + i * 20);
        }
    }

    public void paintCell(Graphics2D g2, int row, int col, Color cellColor)
    {
        int cellLeft =boardMargin + col * cellSize;
        int cellTop = boardMargin + row * cellSize;
        g2.setColor(gridColor);
        g2.fillRect(cellLeft, cellTop, cellSize, cellSize);
        int innerSize = cellSize - 2 * cellMargin;
        g2.setColor(cellColor);
        g2.fillRect(cellLeft + cellMargin, cellTop + cellMargin, innerSize, innerSize);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            pentomino.pressedKey(PentominoAction.RIGHT);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT){
            pentomino.pressedKey(PentominoAction.LEFT);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP){
            pentomino.pressedKey(PentominoAction.UP);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN){
            pentomino.pressedKey(PentominoAction.DOWN);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE){
            pentomino.pressedKey(PentominoAction.SPACE);
            frame.repaint();
        }
        
        else  if (e.getKeyCode() == KeyEvent.VK_D){
            pentomino.pressedKey(PentominoAction.RIGHT);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_A){
            pentomino.pressedKey(PentominoAction.LEFT);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_W){
            pentomino.pressedKey(PentominoAction.UP);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_S){
            pentomino.pressedKey(PentominoAction.DOWN);
            frame.repaint();
        }

        else if (e.getKeyCode() == KeyEvent.VK_R){
            saved=false;
            pentomino.pressedKey(PentominoAction.RESET);
        }
        else if (e.getKeyCode() == KeyEvent.VK_H) {
            if (!pentomino.getalive() && !saved) {
                final JFrame parent = new JFrame();
                String name = JOptionPane.showInputDialog(parent,
                        "Type in your username, please?", null);
                pentomino.writeScore(name);
                saved = true;
                frame.repaint();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_I){
            pentomino.pressedKey(PentominoAction.CHEAT);
            frame.repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_ENTER){
            startGame=true;
            if (onlyOnce) {
                runTimer();
                onlyOnce=false;
            }
            frame.repaint();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
	


}
