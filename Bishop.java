import java.awt.Image;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Tile t, Image i, boolean black) { super(t,i, black); }

    public ArrayList<Tile> getMovableTiles() {
        return getTile().getGame().getUnblockedDiagonalTiles(getTile(), isBlack());
    }
}
