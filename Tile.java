import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tile extends JPanel {
    // specifies if tile is light or dark color
    private final boolean isColored;

    //specifies the location of the tile
    private final int row;
    private final int column;

    // holds a reference to the game the tile is within
    // not static in case multiple ChessGames are run at the same time
    private final ChessGame game;

    // holds whether or not a tile is highlighted (a piece can move there)
    private boolean highlighted;
    // holds the chessPiece on the tile
    private Piece chessPiece;

    public Tile(boolean colored, int r, int c, ChessGame g) {
        isColored = colored;
        row = r;
        column = c;
        highlighted = false;
        game = g;
    }

    public Piece getChessPiece() { return chessPiece; }

    public int getRow() { return row; }

    public int getColumn() { return column; }

    public ChessGame getGame() { return game; }

    public boolean isHighlighted() { return highlighted; }

    public void setHighlighted(boolean h) { highlighted = h; }

    public void setChessPiece(Piece p) { chessPiece = p; }

    // highlights the tiles available for the piece to move to
    public void beSelected() {
        if(getChessPiece() == null) return;

        ArrayList<Tile> tileList = getChessPiece().getMovableTiles();
        if(tileList.size() == 0) return;
        for(Tile t: tileList) {
            t.setHighlighted(true);
        }
    }

    // deselects this tile, de-highlighting all tiles the chessPiece could have gone to
    public void deselectSelf() {
        // if there's no chess piece all tiles are un-highlighted
        if(chessPiece == null) {
            game.deselectAll();
            return;
        }
        for(Tile t: chessPiece.getMovableTiles()) {
            t.setHighlighted(false);
        }
    }

    public void paintComponent(Graphics g) {
        Color tileColor;
        if(isColored) tileColor = new Color(140, 67, 3);
        else tileColor = new Color(255,248,231);

        // highlights the tile if it is highlighted
        if(highlighted) {
            g.setColor(Color.YELLOW);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(tileColor);
            g.fillRect(10, 10, this.getWidth() - 20, this.getHeight() - 20);
        } else {
            g.setColor(tileColor);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        // writes the location of the tile
        if(isColored) g.setColor(new Color(255,248,231));
        else g.setColor(new Color(140,67,3));
        g.drawString(toString(), 0, 10);

        if(chessPiece != null) g.drawImage(chessPiece.getImage(), 0, 0, this.getWidth() ,this.getHeight(),this);
    }

    // returns this tile's location as a string
    public String toString() {
        String[] colVal = {"a","b","c","d","e","f","g","h"};
        return colVal[column] + (8 - row);
    }

    // equals another tile if they have the same location
    public boolean equals(Object other) {
        if(!(other instanceof Tile)) return false;
        Tile otherTile = (Tile) other;
        return row == otherTile.getRow() && column == otherTile.getColumn();
    }
}
