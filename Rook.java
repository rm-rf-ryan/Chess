import java.awt.Image;
import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Tile t, Image i, boolean black) { super(t,i, black); }

    public ArrayList<Tile> getMovableTiles() {
        return getTile().getGame().getUnblockedLateralTiles(getTile(), isBlack());
    }
}
