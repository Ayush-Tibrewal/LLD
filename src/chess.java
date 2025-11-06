import java.util.*;

// ====================== PLAYER CLASSES ======================
abstract class Player {
    public boolean whiteSide;
    public boolean humanPlayer;

    public boolean isWhiteSide() {
        return this.whiteSide;
    }

    public boolean isHumanPlayer() {
        return this.humanPlayer;
    }
}

class HumanPlayer extends Player {
    public HumanPlayer(boolean whiteSide) {
        this.whiteSide = whiteSide;
        this.humanPlayer = true;
    }
}

class ComputerPlayer extends Player {
    public ComputerPlayer(boolean whiteSide) {
        this.whiteSide = whiteSide;
        this.humanPlayer = false;
    }
}

// ====================== PIECE & SPOT ======================
abstract class Piece {
    private boolean killed = false;
    private boolean white = false;

    Piece(boolean white) {
        this.white = white;
    }

    public boolean isWhite() {
        return this.white;
    }

    public boolean isKilled() {
        return this.killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public abstract boolean canMove(Spot start, Spot end, Spot[][] board);
}

class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Spot start, Spot end, Spot[][] board) {
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return (x * y == 2);
    }
}

class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Spot start, Spot end, Spot[][] board) {
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x != y)
            return false;

        int xDirection = (end.getX() - start.getX()) > 0 ? 1 : -1;
        int yDirection = (end.getY() - start.getY()) > 0 ? 1 : -1;
        int xCurr = start.getX() + xDirection;
        int yCurr = start.getY() + yDirection;

        while (xCurr != end.getX() && yCurr != end.getY()) {
            if (board[xCurr][yCurr].getPiece() != null)
                return false;
            xCurr += xDirection;
            yCurr += yDirection;
        }
        return true;
    }
}

class King extends Piece {
    public King(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Spot start, Spot end, Spot[][] board) {
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return (x <= 1 && y <= 1);
    }
}

// ====================== SPOT ======================
class Spot {
    private Piece piece;
    private int x;
    private int y;

    public Spot(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

// ====================== BOARD ======================
class Board {
    private Spot[][] board;

    Board() {
        board = new Spot[8][8];
        resetBoard();
    }

    public Spot getBox(int x, int y) {
        return board[x][y];
    }

    public Spot[][] getBoard() {
        return board;
    }

    public void resetBoard() {
        // Initialize empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Spot(i, j, null);
            }
        }

        // Add a few sample pieces (simplified)
        board[0][2] = new Spot(0, 2, new Bishop(true));
        board[0][1] = new Spot(0, 1, new Knight(true));
        board[0][4] = new Spot(0, 4, new King(true));

        board[7][2] = new Spot(7, 2, new Bishop(false));
        board[7][1] = new Spot(7, 1, new Knight(false));
        board[7][4] = new Spot(7, 4, new King(false));
    }
}

// ====================== GAME ======================
class Game {
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private Player winner;
    private boolean gameOver;

    public Game(Player p1, Player p2) {
        this.players = new Player[] { p1, p2 };
        board = new Board();
        currentTurn = p1.isWhiteSide() ? p1 : p2;
        this.winner = null;
        this.gameOver = false;
    }

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY) {
        if (gameOver) {
            System.out.println("Game is already over! Winner: " + getWinnerName());
            return false;
        }

        Spot start = board.getBox(startX, startY);
        Spot end = board.getBox(endX, endY);

        if (start.getPiece() == null) {
            System.out.println("No piece at start position!");
            return false;
        }

        if (player.isWhiteSide() != start.getPiece().isWhite()) {
            System.out.println("You cannot move opponent’s piece!");
            return false;
        }

        if (!start.getPiece().canMove(start, end, board.getBoard())) {
            System.out.println("Invalid move for " + start.getPiece().getClass().getSimpleName());
            return false;
        }

        Piece destPiece = end.getPiece();

        // === Check if the destination piece is a King ===
        if (destPiece instanceof King) {
            destPiece.setKilled(true);
            gameOver = true;
            winner = player;
            System.out.println("🏆 " + getWinnerName() + " wins! King has been captured!");
            return true;
        }

        // Normal valid move
        end.setPiece(start.getPiece());
        start.setPiece(null);

        currentTurn = (currentTurn == players[0]) ? players[1] : players[0];
        System.out.println("Move successful: " + (player.isWhiteSide() ? "White" : "Black") +
                " moved " + end.getPiece().getClass().getSimpleName() +
                " to (" + endX + ", " + endY + ")");
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinnerName() {
        if (winner == null) return "No winner yet";
        return winner.isWhiteSide() ? "White Player" : "Black Player";
    }
}

// ====================== MAIN GAME RUNNER ======================
public class Main {
    public static void main(String[] args) {
        Player p1 = new HumanPlayer(true);   // White
        Player p2 = new ComputerPlayer(false); // Black

        Game game = new Game(p1, p2);

        // Example moves
        game.playerMove(p1, 0, 1, 2, 2); // Knight move
        game.playerMove(p2, 7, 2, 5, 4); // Bishop move
        game.playerMove(p1, 0, 4, 1, 4); // King move

        // Now Black captures White King (to trigger winner)
        game.playerMove(p2, 5, 4, 1, 4); // Bishop kills White King

        if (game.isGameOver()) {
            System.out.println("Final Winner: " + game.getWinnerName());
        }
    }
}
