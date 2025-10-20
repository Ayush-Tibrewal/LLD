import java.util.Scanner;

class User {
    int id; 
    String name; 
    PieceType type;

    User(int id, String name, PieceType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
enum PieceType{
    X, 
    O, 
    A, 
    P
}

class Board{
    PieceType[][] board;
    int n;
    Board(int n){
    this.n = n;
    board = new PieceType[n][n];
  }
}


class Game {
    Board board; 
    User u1; 
    User u2; 

    Game(Board board, User u1, User u2) {
        this.board = board; 
        this.u1 = u1;
        this.u2 = u2;
    }
    
    void playtictactoe() {
        Scanner sc = new Scanner(System.in);
        boolean winner = false;
        User currentPlayer = u1;

        while(!winner) {
            System.out.println("Player " + currentPlayer.name + " turn (" + currentPlayer.type + ")");
            System.out.print("Enter row and column (0-based): ");
            int row = sc.nextInt();
            int col = sc.nextInt();

            if(board.board[row][col] == null) {
                board.board[row][col] = currentPlayer.type;
            } else {
                System.out.println("Cell already occupied! Try again.");
                continue;
            }

            printBoard();

            if(checkWin(currentPlayer.type)) {
                System.out.println("Player " + currentPlayer.name + " wins!");
                winner = true;
            } else if(isBoardFull()) {
                System.out.println("It's a draw!");
                winner = true;
            }

            // Switch player
            currentPlayer = (currentPlayer == u1) ? u2 : u1;
        }
        sc.close();
    }

    boolean checkWin(PieceType type) {
        for(int i=0;i<board.n;i++){
            if(board.board[i][0] == type && board.board[i][1] == type && board.board[i][2] == type) return true;
            if(board.board[0][i] == type && board.board[1][i] == type && board.board[2][i] == type) return true;
        }
        if(board.board[0][0] == type && board.board[1][1] == type && board.board[2][2] == type) return true;
        if(board.board[0][2] == type && board.board[1][1] == type && board.board[2][0] == type) return true;

        return false;
    }

    boolean isBoardFull() {
        for(int i=0;i<board.n;i++){
            for(int j=0;j<board.n;j++){
                if(board.board[i][j] == null) return false;
            }
        }
        return true;
    }

    void printBoard() {
        for(int i=0;i<board.n;i++){
            for(int j=0;j<board.n;j++){
                System.out.print((board.board[i][j] == null ? "-" : board.board[i][j]) + " ");
            }
            System.out.println();
        }
    }
}
class Main{
    public static void main(String[] args){
        User u1 = new User(1  , "ayush" , PieceType.X);
        User u2 = new User(2 , "Priya" ,PieceType.O);
        
        Board board = new Board(3);
        System.out.println(board.board[0][0]);
        
        Game game = new Game(board , u1  , u2);
        game.playtictactoe();
    }
}
