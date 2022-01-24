import java.awt.Image;
import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(Tile t, Image i, boolean black) { super(t,i, black); }

    public boolean canMoveTo(Tile t) {
        if(!super.canMoveTo(t)) return false;
        boolean isBlocked;
        int currentRow = getTile().getRow();
        int currentCol = getTile().getColumn();
        ChessGame game = t.getGame();

        // checks if a piece is in front
        if(isBlack()) isBlocked = game.getTileAt(currentRow + 1, currentCol).getChessPiece() != null;
        else isBlocked = game.getTileAt(currentRow - 1, currentCol).getChessPiece() != null;

        if(!getHasMoved()) {
            // checks if there's a piece 2 tiles in front
            boolean secondBlocked;
            if(isBlack()) secondBlocked = game.getTileAt(currentRow + 2, currentCol).getChessPiece() != null;
            else secondBlocked = game.getTileAt(currentRow - 2, currentCol).getChessPiece() != null;

            // if the space is empty for 2 tiles in front then the pawn can move 2 spaces forward
            if(!isBlocked && !secondBlocked) {
                if (isBlack() && t.getRow() == currentRow + 2) return true;
                else if (!isBlack() && t.getRow() == currentRow - 2) return true;
            }
        }
        // checks if the pawn can capture a piece
        if(t.getColumn() != currentCol && t.getChessPiece() != null && t.getChessPiece().isBlack() != isBlack()) return true;

        if(!isBlocked && t.getColumn() == currentCol) {
            if (isBlack() && t.getRow() == currentRow + 1) return true;
            if (!isBlack() && t.getRow() == currentRow - 1) return true;
        }

        return false;
    }

    // returns a list of locations the pawn can move
    // tests all 4 ways a pawn can move
    public ArrayList<Tile> getMovableTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        // tests the 3 tiles immediately in front of the pawn
        Tile current = getTile();
        Tile adjacent;
        int currentRow = current.getRow();
        int currentCol = current.getColumn();
        ChessGame game = current.getGame();
        for(int i = -1; i < 2; i++) {
            if(isBlack())
                adjacent = game.getTileAt(currentRow + 1,currentCol + i);
            else
                adjacent = game.getTileAt(currentRow - 1, currentCol + i);

            if(canMoveTo(adjacent))
                tiles.add(adjacent);
        }
        // tests tile 2 spaces away
        if(isBlack()) adjacent = game.getTileAt(currentRow + 2,currentCol);
        else adjacent = game.getTileAt(currentRow - 2, currentCol);
        if(canMoveTo(adjacent)) tiles.add(adjacent);

        return tiles;
    }

    // gets the 2 tiles it attacks in its front left and front right
    public ArrayList<Tile> getAttackingTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        Tile candidate1;
        Tile candidate2;
        if(isBlack()) {
            candidate1 = getTile().getGame().getTileAt(getTile().getRow() + 1,getTile().getColumn() - 1);
            candidate2 = getTile().getGame().getTileAt(getTile().getRow() + 1,getTile().getColumn() + 1);
        } else {
            candidate1 = getTile().getGame().getTileAt(getTile().getRow() - 1,getTile().getColumn() - 1);
            candidate2 = getTile().getGame().getTileAt(getTile().getRow() - 1,getTile().getColumn() + 1);
        }

        tiles.add(candidate1);
        tiles.add(candidate2);
        return tiles;
    }

    // moves pawn and checks if promoted
    public boolean move(Tile t) {
        boolean result = super.move(t);
        if(isBlack() && getTile().getRow() == 7 || !isBlack() && getTile().getRow() == 0)
            getTile().getGame().promotePawn(getTile());
        return result;
    }
}
