import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;

public class TicTacToeGUI {

    private static HashMap<String, String> board = new HashMap<>();
    private static String p1, p2, p1_symbol, p2_symbol;
    private static int totalMoves = 0;
    private static int playerTurn = 1; // 1: p1's turn, 2: p2's turn
    private static String currentPlayer;

    public static void main(String[] args) {
        resetBoard();
        setupGUI();
    }

    private static void resetBoard() {
        for (int i = 1; i <= 9; i++) {
            board.put(String.valueOf(i), " ");
        }
    }

    private static void setupGUI() {
        // Main frame settings
        JFrame frame = new JFrame("Tic-Tac-Toe Multiplayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(255, 255, 204)); // Light yellow background

        // Create the game board panel (3x3 grid of buttons)
        JPanel panel = new JPanel(new GridLayout(3, 3));
        panel.setBackground(new Color(230, 230, 250)); // Light lavender for the grid background
        JButton[] buttons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton(" ");
            final int index = i + 1;
            
            // Customize button appearance
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));  // Large bold font for text
            buttons[i].setBackground(new Color(173, 216, 230));   // Light blue button background
            buttons[i].setForeground(Color.DARK_GRAY);            // Dark gray text color

            buttons[i].setFocusPainted(false); // Remove focus outline on buttons
            
            // Button action listener for game logic
            buttons[i].addActionListener(e -> {
                if (buttons[index - 1].getText().equals(" ")) {
                    if (playerTurn == 1) {
                        buttons[index - 1].setText(p1_symbol);
                        buttons[index - 1].setForeground(Color.MAGENTA);  // Player 1 symbol color
                        board.put(String.valueOf(index), p1_symbol);
                        currentPlayer = p1;
                    } else {
                        buttons[index - 1].setText(p2_symbol);
                        buttons[index - 1].setForeground(Color.BLUE);     // Player 2 symbol color
                        board.put(String.valueOf(index), p2_symbol);
                        currentPlayer = p2;
                    }
                    totalMoves++;
                    if (checkWinner()) {
                        JOptionPane.showMessageDialog(frame, currentPlayer + " won the match!");
                        resetBoard();
                        resetButtons(buttons);
                        return;
                    } else if (totalMoves == 9) {
                        JOptionPane.showMessageDialog(frame, "It's a tie!");
                        resetBoard();
                        resetButtons(buttons);
                        return;
                    }
                    playerTurn = (playerTurn == 1) ? 2 : 1;
                }
            });
            panel.add(buttons[i]);
        }

        frame.add(panel, BorderLayout.CENTER);

        // Player setup panel (inputs for names, symbol choice, and start button)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2));
        infoPanel.setBackground(new Color(255, 228, 225)); // Light pink background for player setup

        JTextField p1Name = new JTextField();
        JTextField p2Name = new JTextField();
        JComboBox<String> p2SymbolChoice = new JComboBox<>(new String[]{"x", "o"});

        JButton startButton = new JButton("Start Game");
        startButton.setBackground(new Color(240, 128, 128));  // Light coral background for start button
        startButton.setForeground(Color.WHITE);               // White text on start button
        startButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font for start button

        startButton.addActionListener(e -> {
            p1 = p1Name.getText();
            p2 = p2Name.getText();
            if (p1.equals(p2) || p1.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Player names must be unique and non-empty.");
                return;
            }
            p2_symbol = (String) p2SymbolChoice.getSelectedItem();
            p1_symbol = p2_symbol.equals("x") ? "o" : "x";
            currentPlayer = p1;
            playerTurn = doToss() == 1 ? 1 : 2;
        });

        infoPanel.add(new JLabel("Player 1 Name:"));
        infoPanel.add(p1Name);
        infoPanel.add(new JLabel("Player 2 Name:"));
        infoPanel.add(p2Name);
        infoPanel.add(new JLabel("Player 2 Symbol (x/o):"));
        infoPanel.add(p2SymbolChoice);
        infoPanel.add(new JLabel(""));
        infoPanel.add(startButton);

        frame.add(infoPanel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private static void resetButtons(JButton[] buttons) {
        for (int i = 0; i < 9; i++) {
            buttons[i].setText(" ");
            buttons[i].setForeground(Color.DARK_GRAY); // Reset text color
        }
        totalMoves = 0;
        playerTurn = 1;
    }

    private static int doToss() {
        Random random = new Random();
        int tossResult = random.nextInt(2);
        if (tossResult == 1) {
            JOptionPane.showMessageDialog(null, p1 + " won the toss and goes first.");
        } else {
            JOptionPane.showMessageDialog(null, p2 + " won the toss and goes first.");
        }
        return tossResult;
    }

    private static boolean checkWinner() {
        String[][] winCombinations = {
                {"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"},
                {"1", "4", "7"}, {"2", "5", "8"}, {"3", "6", "9"},
                {"1", "5", "9"}, {"3", "5", "7"}
        };

        for (String[] combination : winCombinations) {
            if (board.get(combination[0]).equals(board.get(combination[1])) &&
                    board.get(combination[1]).equals(board.get(combination[2])) &&
                    !board.get(combination[0]).equals(" ")) {
                return true;
            }
        }
        return false;
    }
}
