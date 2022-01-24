import java.awt.Image;
import java.util.ArrayList;

public class King extends Piece {
    public King(Tile t, Image i, boolean black) { super(t,i, black); }

    // because not all the tiles able to be attacked by the King will show up in getMoveableTiles()
    // includes tiles occupied by a piece of the opposite color
    public ArrayList<Tile> getRawAttackingTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        for(int dy = -1; dy < 2; dy++) {
            for(int dx = -1; dx < 2; dx++) {
                Tile t = getTile().getGame().getTileAt(getTile().getRow() + dy, getTile().getColumn() + dx);
                if(t != null && (t.getChessPiece() == null || t.getChessPiece().isBlack() != isBlack())) tiles.add(t);
            }
        }

        return tiles;
    }

    // only implemented a portion of the king movement restrictions
    public boolean canMoveTo(Tile t) {
        // doesn't allow king to move to an attacked tile, although it still doesn't work 100% of the time in some fringe cases
        for(Tile attackedTile: getTile().getGame().getAllAttackedTiles(!isBlack())) {
            if(t.equals(attackedTile)) return false;
        }
        return super.canMoveTo(t);
    }

    public ArrayList<Tile> getMovableTiles() {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        // adds tiles it attacks if they can be moved to
        for(Tile t: getRawAttackingTiles()) {
            if(canMoveTo(t)) tiles.add(t);
        }

        // checks if it can castle
        ChessGame game = getTile().getGame();
        if(isBlack()) {
            if(clearToCastleKingside(true)) tiles.add(game.getTileAt(0,6));
            if(clearToCastleQueenside(true)) tiles.add(game.getTileAt(0,2));
        } else {
            if(clearToCastleKingside(false)) tiles.add(game.getTileAt(7,6));
            if(clearToCastleQueenside(false)) tiles.add(game.getTileAt(7,2));
        }

        return tiles;
    }

    // doesn't actually check castling through/over check
    // manually checks all tiles/pieces involved
    public boolean clearToCastleKingside(boolean black) {
        ChessGame game = getTile().getGame();
        // holds whether there's no pieces in the way to castle
        boolean empty = true;
        if (black) {
            for (int col = 5; col < 7; col++) {
                if (game.getTileAt(0, col).getChessPiece() != null) {
                    empty = false;
                    break;
                }
            }
            return empty && game.getTileAt(0, 7).getChessPiece() != null && !game.getTileAt(0, 7).getChessPiece().getHasMoved();
        } else {
            for (int col = 5; col < 7; col++) {
                if(game.getTileAt(7,col).getChessPiece() != null) {
                    empty = false;
                    break;
                }
            }
            return empty && game.getTileAt(7, 7).getChessPiece() != null && !game.getTileAt(7, 7).getChessPiece().getHasMoved();

        }
    }

    // doesn't actually check castling through/over check
    // manually checks all tiles/pieces involved
    public boolean clearToCastleQueenside(boolean black) {
        ChessGame game = getTile().getGame();
        // holds whether there's no pieces in the way to castle
        boolean empty = true;
        if(black) {
            for (int col = 1; col < 4; col++) {
                if (game.getTileAt(0, col).getChessPiece() != null) {
                    empty = false;
                    break;
                }
            }
            return empty && game.getTileAt(0,0).getChessPiece() != null && !game.getTileAt(0,0).getChessPiece().getHasMoved();
        } else {
            for (int col = 1; col < 4; col++) {
                if (game.getTileAt(7, col).getChessPiece() != null) {
                    empty = false;
                    break;
                }
            }
            return empty && game.getTileAt(7,0).getChessPiece() != null && !(game.getTileAt(7,0).getChessPiece().getHasMoved());
        }
    }

    public boolean move(Tile to) {
        ChessGame game = to.getGame();
        // checks if the tile selected means the player's castling
        if(!getHasMoved()) {
            if(isBlack()) {
                if (game.getTileAt(0, 6).equals(to)) {
                    castles(true);
                    return true;
                }
                else if(game.getTileAt(0,2).equals(to)) {
                    castles(false);
                    return true;
                }
            } else {
                if(game.getTileAt(7, 6).equals(to)) {
                    castles(true);
                    return true;
                }
                else if(game.getTileAt(7,2).equals(to)) {
                    castles(false);
                    return true;
                }
            }
        }

        return super.move(to);
    }

    private void castles(boolean kingside) {
        ChessGame game = getTile().getGame();
        if(isBlack()) {
            if (kingside) {
                super.move(game.getTileAt(0, 6));
                game.getTileAt(0,7).getChessPiece().move(game.getTileAt(0,5));
            } else {
                super.move(game.getTileAt(0, 2));
                game.getTileAt(0,0).getChessPiece().move(game.getTileAt(0,3));
            }
        } else {
            if(kingside) {
                super.move(game.getTileAt(7, 6));
                game.getTileAt(7,7).getChessPiece().move(game.getTileAt(7,5));
            } else {
                super.move(game.getTileAt(7, 2));
                game.getTileAt(7,0).getChessPiece().move(game.getTileAt(7,3));
            }
        }
    }
}
