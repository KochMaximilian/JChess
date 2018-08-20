import Board.Board;
import Gui.Table;

public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);

        System.out.println("Lowercase = Black / Uppercase = White");

        Table table = new Table();

    }
}