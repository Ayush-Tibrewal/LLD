import java.util.*;

class Player {
    int id;
    String name;

    Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

class Dice {
    int sides;
    int numberOfDice;

    Dice(int sides, int numberOfDice) {
        this.sides = sides;
        this.numberOfDice = numberOfDice;
    }

    int roll() {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += (int) (Math.random() * sides) + 1;
        }
        return total;
    }
}

class Jumper {
    int startpoint;
    int endpoint;

    Jumper(int startpoint, int endpoint) {
        this.startpoint = startpoint;
        this.endpoint = endpoint;
    }
}

class GameBoard {
    Dice dice;
    Queue<Player> nextTurn;
    List<Jumper> snakes;
    List<Jumper> ladders;
    Map<String, Integer> playersCurrentPosition;
    int boardSize;

    GameBoard(Dice dice, Queue<Player> nextTurn, List<Jumper> snakes, List<Jumper> ladders,
              Map<String, Integer> playersCurrentPosition, int boardSize) {
        this.dice = dice;
        this.nextTurn = nextTurn;
        this.snakes = snakes;
        this.ladders = ladders;
        this.playersCurrentPosition = playersCurrentPosition;
        this.boardSize = boardSize;
    }

    void initialise() {
        int n = nextTurn.size();
        for (int i = 0; i < n; i++) {
            Player player = nextTurn.poll();
            playersCurrentPosition.put(player.getName(), 0);
            nextTurn.add(player);
        }
        startGame();
    }

    void startGame() {
        while (!nextTurn.isEmpty()) {
            Player player = nextTurn.poll();
            int currentPosition = playersCurrentPosition.get(player.getName());
            int diceValue = dice.roll();
            int nextCell = currentPosition + diceValue;

            System.out.println(player.getName() + " rolled a " + diceValue);

            if (nextCell > boardSize) {
                System.out.println(player.getName() + " cannot move, stays at " + currentPosition);
                nextTurn.add(player);
                continue;
            }

            // Check snakes
            for (Jumper snake : snakes) {
                if (snake.startpoint == nextCell) {
                    System.out.println(player.getName() + " got bitten by a snake! Moves down to " + snake.endpoint);
                    nextCell = snake.endpoint;
                    break;
                }
            }

            // Check ladders
            for (Jumper ladder : ladders) {
                if (ladder.startpoint == nextCell) {
                    System.out.println(player.getName() + " climbed a ladder! Moves up to " + ladder.endpoint);
                    nextCell = ladder.endpoint;
                    break;
                }
            }

            playersCurrentPosition.put(player.getName(), nextCell);
            System.out.println(player.getName() + " is now at position " + nextCell);
            System.out.println("-------------------------");

            if (nextCell == boardSize) {
                System.out.println(player.getName() + " won the game!");
                // Do not re-add the winner to the queue
            } else {
                nextTurn.add(player);
            }

            // Check if all players finished
            if (nextTurn.size() == 0) {
                break;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player(1, "Alice");
        Player p2 = new Player(2, "Bob");
        Player p3 = new Player(3, "Charlie");

        Queue<Player> turnQueue = new LinkedList<>();
        turnQueue.add(p1);
        turnQueue.add(p2);
        turnQueue.add(p3);

        Dice dice = new Dice(6, 1);

        List<Jumper> snakes = new ArrayList<>();
        snakes.add(new Jumper(14, 7));
        snakes.add(new Jumper(25, 5));
        snakes.add(new Jumper(32, 10));

        List<Jumper> ladders = new ArrayList<>();
        ladders.add(new Jumper(3, 22));
        ladders.add(new Jumper(8, 26));
        ladders.add(new Jumper(20, 29));

        Map<String, Integer> playerPositions = new HashMap<>();
        int boardSize = 30;

        GameBoard board = new GameBoard(dice, turnQueue, snakes, ladders, playerPositions, boardSize);

        board.initialise();
    }
}
