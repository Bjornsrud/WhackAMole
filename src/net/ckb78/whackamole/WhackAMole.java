package net.ckb78.whackamole;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WhackAMole {
    int boardWidth = 590;
    int boardHeight = 700;

    JFrame frame = new JFrame("Whack-A-Mole");
    JLabel textLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon bombIcon;
    ImageIcon explosionIcon;

    JButton currMoleTile;
    JButton currBombTile;

    Random random = new Random();
    Timer setMoleTimer;
    Timer setBombTimer;
    int score = 0;
    boolean gameStarted = false;

    public static void main(String[] args) {
        new WhackAMole();
    }

    WhackAMole() {
        initializeFrame();
        initializeTextPanel();
        initializeBoard();
        loadIcons();
        initializeTimers();
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void initializeTextPanel() {
        textLabel.setFont(new Font("Courier New", Font.PLAIN, 40));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("WHACK-A-MOLE!");
        textLabel.setOpaque(true);
        textLabel.setBackground(Color.BLACK);
        textLabel.setForeground(Color.ORANGE);

        scoreLabel.setFont(new Font("Courier New", Font.PLAIN, 30));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setText("Christian Bjørnsrud 2025");
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setForeground(Color.ORANGE);

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.BLACK);
        textPanel.setPreferredSize(new Dimension(boardWidth - 120, 150));

        JPanel linePanelTop = new JPanel();
        linePanelTop.setBackground(Color.ORANGE);
        linePanelTop.setPreferredSize(new Dimension(boardWidth - 120, 3));

        JPanel linePanelBottom = new JPanel();
        linePanelBottom.setBackground(Color.ORANGE);
        linePanelBottom.setPreferredSize(new Dimension(boardWidth - 120, 3));

        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BorderLayout());
        textContainer.setBackground(Color.BLACK);
        textContainer.add(textLabel, BorderLayout.NORTH);
        textContainer.add(scoreLabel, BorderLayout.CENTER);

        textPanel.add(linePanelTop);
        textPanel.add(textContainer);
        textPanel.add(linePanelBottom);

        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setPreferredSize(new Dimension(120, 150));

        JButton newGameButton = createNewGameButton();
        buttonPanel.add(newGameButton, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.add(textPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton("<html><CENTER>START<BR>GAME</CENTER></html>");
        newGameButton.setFont(new Font("Courier New", Font.PLAIN, 30));
        newGameButton.setForeground(Color.ORANGE);
        newGameButton.setBackground(Color.DARK_GRAY);
        newGameButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(e -> startNewGame());
        return newGameButton;
    }

    private void initializeBoard() {
        boardPanel.setLayout(new GridLayout(3, 3));
        frame.add(boardPanel, BorderLayout.CENTER);

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            tile.addActionListener(e -> handleTileClick((JButton) e.getSource()));
            setRandomTileBackgroundColor(tile);
        }
    }

    private void setRandomTileBackgroundColor(JButton tile) {
        int red = 0;
        int green = random.nextInt(50) + 50;
        int blue = 0;
        tile.setBackground(new Color(red, green, blue));
    }

    private void loadIcons() {
        String resourcePath = "resources/";

        Image moleImage = new ImageIcon(resourcePath + "Mole.png").getImage();
        moleIcon = new ImageIcon(moleImage.getScaledInstance(160, 160, java.awt.Image.SCALE_SMOOTH));

        Image bombImage = new ImageIcon(resourcePath + "Bomb.png").getImage();
        bombIcon = new ImageIcon(bombImage.getScaledInstance(170, 170, java.awt.Image.SCALE_SMOOTH));

        Image explosionImage = new ImageIcon(resourcePath + "Explosion.png").getImage();
        explosionIcon = new ImageIcon(explosionImage.getScaledInstance(160, 160, java.awt.Image.SCALE_SMOOTH));
    }


    private void initializeTimers() {
        setMoleTimer = new Timer(1000, e -> setMole());
        setBombTimer = new Timer(1500, e -> setBomb());
    }

    private void handleTileClick(JButton tile) {
        if (tile == currMoleTile) {
            score += 10;
            scoreLabel.setText("Score: " + score);
            adjustMoleTimer();
        } else if (tile == currBombTile) {
            endGame();
        }
    }

    private void setMole() {
        if (currMoleTile != null) {
            currMoleTile.setIcon(null);
            currMoleTile = null;
        }

        JButton tile;
        do {
            tile = board[random.nextInt(9)];
        } while (tile == currBombTile);

        currMoleTile = tile;
        currMoleTile.setIcon(moleIcon);
    }

    private void setBomb() {
        if (currBombTile != null) {
            currBombTile.setIcon(null);
            currBombTile = null;
        }

        JButton tile;
        do {
            tile = board[random.nextInt(9)];
        } while (tile == currMoleTile);

        currBombTile = tile;
        currBombTile.setIcon(bombIcon);
    }

    private void endGame() {
        textLabel.setText("GAME OVER!");
        textLabel.setFont(new Font("Courier New", Font.BOLD, 40));
        scoreLabel.setText("Final Score: " + score);
        scoreLabel.setFont(new Font("Courier New", Font.PLAIN, 30));

        if (currBombTile != null) {
            currBombTile.setIcon(explosionIcon);
        }

        setMoleTimer.stop();
        setBombTimer.stop();

        for (JButton jButton : board) {
            jButton.setEnabled(false);
        }

        JButton newGameButton = getNewGameButton();
        assert newGameButton != null;
        newGameButton.setText("<html><CENTER>NEW<br>GAME</CENTER></html>");
        newGameButton.setBackground(Color.DARK_GRAY);
    }

    private JButton getNewGameButton() {
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                return (JButton) component;
            }
        }
        return null;
    }

    private void startNewGame() {
        score = 0;
        scoreLabel.setText("Score: " + score);
        resetBoard();

        setMoleTimer.setDelay(1000);

        setMoleTimer.restart();
        setBombTimer.restart();

        currMoleTile = null;
        currBombTile = null;

        setMoleTimer.start();
        setBombTimer.start();
        gameStarted = true;

        textLabel.setText("WHACK-A-MOLE!");
        textLabel.setFont(new Font("Courier New", Font.BOLD, 40));
    }

    private void resetBoard() {
        for (JButton jButton : board) {
            jButton.setEnabled(true);
            jButton.setIcon(null);
        }
    }

    private void adjustMoleTimer() {
        if (score % 50 == 0) {
            int newInterval = Math.max(200, setMoleTimer.getDelay() - 200); // Minimum interval is 200ms
            setMoleTimer.setDelay(newInterval);
        }
    }
}
