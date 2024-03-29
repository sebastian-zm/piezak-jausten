package main_game;

import score.SQLiteKudeatu;
import score.Score;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import model.Board;
import score.ScoreenTableModela;
import score.ScoresTable;

@SuppressWarnings("serial")
public class TetrisJPanel
        extends JPanel
{

    public static final int CANVAS_WIDTH = 916;
    public static final int CANVAS_HEIGHT = 840;
    private final int cellSize = 40;
    private Board board;
    private BoardUpdater bu;

    public TetrisJPanel()
    {
        bu = new BoardUpdater();
        final int BOARD_TOP = 0;
        final int BOARD_LEFT = 250;
        final int BOARD_WIDTH = 10;
        final int BOARD_HEIGHT = 20;
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT, BOARD_LEFT, BOARD_TOP);
        KeyListener listener = new MyKeyListener();
        super.addKeyListener(listener);
        super.setFocusable(true);
    }

    public class MyKeyListener
            implements KeyListener
    {

        @Override
        public void keyTyped(KeyEvent e)
        {
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            switch (keyCode)
            {
                case KeyEvent.VK_NUMPAD0:
                    board.rotateRight = true;
                    break;
                case KeyEvent.VK_UP:
                    board.rotateLeft = true;
                    break;
                case KeyEvent.VK_LEFT:
                    board.moveLeft = true;
                    board.movedLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    board.moveRight = true;
                    board.movedRight = false;
                    break;
                case KeyEvent.VK_DOWN:
                    board.fallFast = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            switch (keyCode)
            {
                case KeyEvent.VK_LEFT:
                    board.moveLeft = false;
                    if (!board.movedLeft)
                    {
                        board.moveLeftOnce = true;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    board.moveRight = false;
                    if (!board.movedRight)
                    {
                        board.moveRightOnce = true;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    board.fallFast = false;
                    board.ignoreFallFast = false;
                    break;
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        board.paint(g2d, cellSize);
    }

    public static void main(String[] args)
            throws InterruptedException
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.
                    getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.
                    getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.
                    getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.
                    getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        final String TITLE = "Tetris";
        JFrame frame = new JFrame(TITLE);
        TetrisJPanel game = new TetrisJPanel();
        frame.add(game);
        frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true)
        {
            if (!game.bu.update(game.board))
            {
                break;
            }
            game.repaint();
        }
        game.repaint();
        boolean transp = false;
        for (int i = 0; i < 6; ++i)
        {
            TimeUnit.MILLISECONDS.sleep(500);
            game.board.setTetrominoTransparent(transp ^= true);
            game.repaint();
        }
        frame.setVisible(false);
        frame.remove(game);
        String name = JOptionPane.showInputDialog(null, "Inserta tu nombre:");
        Score scr = new Score(name, game.board.toSend);
        SQLiteKudeatu.gehitu(scr);
        frame.dispose();
        ScoresTable st = new ScoresTable();
        st.setVisible(true);
    }
}
