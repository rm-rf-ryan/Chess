import java.awt.Image;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Tile t, Image i, boolean black) { super(t,i, black); }

    public ArrayList<Tile> getMovableTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        ChessGame game = getTile().getGame();
        int curRow = getTile().getRow();
        int curCol = getTile().getColumn();
        // holds the location of the 8 tiles maximum a knight can move to
        Tile[] prospectiveTiles = {
                game.getTileAt(curRow - 2, curCol - 1),
                game.getTileAt(curRow -  2, curCol + 1),
                game.getTileAt(curRow + 1, curCol + 2),
                game.getTileAt(curRow - 1, curCol + 2),
                game.getTileAt(curRow + 2, curCol - 1),
                game.getTileAt(curRow + 2, curCol + 1),
                game.getTileAt(curRow + 1, curCol - 2),
                game.getTileAt(curRow - 1, curCol - 2)
        };
        for(Tile t: prospectiveTiles) {
            if(canMoveTo(t)) tiles.add(t);
        }
        return tiles;
    }
}
