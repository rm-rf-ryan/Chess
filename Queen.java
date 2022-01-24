import java.awt.Image;
import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Tile t, Image i, boolean black) {
        super(t,i, black);
    }

    // combines the lateral and diagonal lists of tiles
    public ArrayList<Tile> getMovableTiles() {
        ArrayList<Tile> laterals = getTile().getGame().getUnblockedLateralTiles(getTile(), isBlack());
        ArrayList<Tile> diagonals = getTile().getGame().getUnblockedDiagonalTiles(getTile(), isBlack());

        for(Tile t: laterals) {
            diagonals.add(t);
        }
        return diagonals;
    }
}
