import java.awt.Image;

import java.util.ArrayList;

public abstract class Piece {
    // holds the image of the piece to be painted on the board
    private Image img;
    // holds whether the piece is white or black
    private boolean black;
    // holds a reference to the tile it's located at
    private Tile tile;
    // returns if this piece has moved
    private boolean hasMoved;

    public Piece(Tile t, Image i, boolean b) {
        tile = t;
        img = i;
        black = b;
        hasMoved = false;
    }

    // returns the image of the piece
    public Image getImage() {
        return img;
    }

    // returns the tile on which the piece is on
    public Tile getTile() { return tile; }

    // returns the affiliation of the piece
    public boolean isBlack() { return black; }

    public boolean getHasMoved() { return hasMoved; }

    // returns whether or not the piece can move to the Tile provided
    public boolean canMoveTo(Tile t) {
        // getTileAt() in ChessGame returns null if the tile location is outside boundaries
        if(t == null) return false;
        if(t.getChessPiece() == null) return true;
        return t.getChessPiece().black != black;
    }

    // returns a list of tiles that can be moved to by a piece
    public abstract ArrayList<Tile> getMovableTiles();

    // moves the piece to another tile
    public boolean move(Tile to) {
        if(canMoveTo(to)) {
            to.setChessPiece(this);
            tile.setChessPiece(null);
            tile = to;
            hasMoved = true;
            return true;
        }
        return false;
    }
}
