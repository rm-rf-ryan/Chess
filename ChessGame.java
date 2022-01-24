import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

// everything except the images of the pieces is my original work
public class ChessGame {
    private JFrame frame;

    // holds references to tiles upon chessBoard
    private Tile[][] tileBoard;

    // the tile of the currently selected piece
    private Tile selected;

    // false if it's white's turn, true if it's black's
    private boolean blackTurn;

    // game is stopped if stalemate or checkmate occurs
    private boolean isStopped;

    public static void main(String[] args) {
        new ChessGame().start();
    }

    // starts the game and GUI
    public void start() {
        frame = new JFrame("chess i guess");

        isStopped = false;

        tileBoard = new Tile[8][8];

        // creates the chessBoard panel, instantiates all 64 Tiles
        JPanel chessBoard = new JPanel(new GridLayout(8,8));
        boolean colored = false;
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                Tile chessTile = new Tile(colored, row, col, this);
                // for when a player clicks on a piece
                chessTile.addMouseListener(new MouseListener() {
                    public void mousePressed(MouseEvent e) {

                        // makes sure nothing can happen to the board if the game is over
                        if(isStopped) return;

                        if(selected == null) {
                            // if there's nothing selected and there's no piece in the tile clicked nothing happens
                            if(chessTile.getChessPiece() == null) return;

                            // if the tile clicked has a piece, there's no piece currently selected, and it's that player's turn
                            if(chessTile.getChessPiece().isBlack() == blackTurn) {
                                chessTile.beSelected();
                                selected = chessTile;
                            }
                        } else if(!chessTile.isHighlighted()) {
                            // deselects piece if a move to an available move isn't made
                            selected.deselectSelf();
                            selected = null;
                        } else {
                            // moves the piece and clears highlighting then moves on to the next player's turn
                            selected.getChessPiece().move(chessTile);
                            deselectAll();
                            selected = null;
                            changeTurn();
                        }
                        frame.repaint();
                    }

                    public void mouseClicked(MouseEvent e) {}
                    public void mouseReleased(MouseEvent e) {}
                    public void mouseEntered(MouseEvent e) {}
                    public void mouseExited(MouseEvent e) {}
                });
                tileBoard[row][col] = chessTile;
                chessBoard.add(chessTile);
                colored = !colored; // switches coloring of the tiles
            }
            colored = !colored;
        }

        setBoard();

        // creates the button that resets the game
        JButton resetButton = new JButton("New Game");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBoard();
                setBoard();
                deselectAll();
                blackTurn = false;
                isStopped = false;
                frame.repaint();
            }
        });

        // sets the bottom bar, which displays whose turn it is as well as the reset button
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Helvetica", Font.BOLD, 20));
                if(blackTurn)
                    g.drawString("Black's Turn",5,20);
                else
                    g.drawString("White's Turn",5,20);
            }
        };
        bottomBar.add(resetButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame.getContentPane().add(BorderLayout.SOUTH, bottomBar);
        frame.getContentPane().add(BorderLayout.CENTER, chessBoard);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1/2 of the average of the dimensions of the screen, thus 1/4 area of average lengths of screen
        // bigger than 1/4 area of screen by l^2/2 + w^2/2
        int avgLen = (screenSize.width + screenSize.height) / 4;
        frame.setSize(avgLen,avgLen + bottomBar.getHeight());
        // puts the frame at the center of the screen
        frame.setLocation(screenSize.width / 2 - 500, screenSize.height / 2 - 500);
        frame.setVisible(true);
    }

    // places pieces on board
    public void setBoard() {
        // sets pawns on board
        Image bPawnImg = new ImageIcon(getClass().getResource("/ChessPieceImages/BlackPawn.png")).getImage();
        Image wPawnImg = new ImageIcon(getClass().getResource("/ChessPieceImages/WhitePawn.png")).getImage();
        for(int col = 0; col < 8; col++) {
            // black pawns
            tileBoard[1][col].setChessPiece(new Pawn(tileBoard[1][col], bPawnImg, true));
            // white pawns
            tileBoard[6][col].setChessPiece(new Pawn(tileBoard[6][col], wPawnImg, false));
        }

        // sets rooks
        Image bRookImg = new ImageIcon(getClass().getResource("/ChessPieceImages/BlackRook.png")).getImage();
        tileBoard[0][0].setChessPiece(new Rook(tileBoard[0][0], bRookImg, true));
        tileBoard[0][7].setChessPiece(new Rook(tileBoard[0][7], bRookImg, true));
        Image wRookImg = new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteRook.png")).getImage();
        tileBoard[7][0].setChessPiece(new Rook(tileBoard[7][0],wRookImg,false));
        tileBoard[7][7].setChessPiece(new Rook(tileBoard[7][7],wRookImg,false));

        // sets knights
        Image bKnightImg = new ImageIcon(getClass().getResource("/ChessPieceImages/BlackKnight.png")).getImage();
        tileBoard[0][1].setChessPiece(new Knight(tileBoard[0][1], bKnightImg, true));
        tileBoard[0][6].setChessPiece(new Knight(tileBoard[0][6], bKnightImg, true));
        Image wKnightImg = new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteKnight.png")).getImage();
        tileBoard[7][1].setChessPiece(new Knight(tileBoard[7][1], wKnightImg, false));
        tileBoard[7][6].setChessPiece(new Knight(tileBoard[7][6], wKnightImg, false));

        // sets bishops
        Image bBishopImg = new ImageIcon(getClass().getResource("/ChessPieceImages/BlackBishop.png")).getImage();
        tileBoard[0][2].setChessPiece(new Bishop(tileBoard[0][2], bBishopImg, true));
        tileBoard[0][5].setChessPiece(new Bishop(tileBoard[0][5], bBishopImg, true));
        Image wBishopImg = new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteBishop.png")).getImage();
        tileBoard[7][2].setChessPiece(new Bishop(tileBoard[7][2], wBishopImg, false));
        tileBoard[7][5].setChessPiece(new Bishop(tileBoard[7][5], wBishopImg, false));

        // sets queens
        tileBoard[0][3].setChessPiece(new Queen(tileBoard[0][3], new ImageIcon(getClass().getResource("/ChessPieceImages/BlackQueen.png")).getImage(),true));
        tileBoard[7][3].setChessPiece(new Queen(tileBoard[7][3], new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteQueen.png")).getImage(),false));

        // sets kings
        tileBoard[0][4].setChessPiece(new King(tileBoard[0][4], new ImageIcon(getClass().getResource("/ChessPieceImages/BlackKing.png")).getImage(),true));
        tileBoard[7][4].setChessPiece(new King(tileBoard[7][4], new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteKing.png")).getImage(),false));
    }

    // removes all pieces from board
    public void clearBoard() {
        for(Tile[] row: tileBoard) {
            for(Tile t: row) {
                t.setChessPiece(null);
            }
        }
    }

    // returns the tile at (row, col) and returns null if it is outside tileBoard's bounds
    public Tile getTileAt(int row, int col) {
        if(row < 0 || row > 7 || col < 0 || col > 7) return null;
        return tileBoard[row][col];
    }

    // for use with getting unblocked tiles, breaks loop if there's no tile or it's occupied by same color piece
    private boolean radialCheck(Tile t, boolean black) {
        if(t == null) return false;
        if(t.getChessPiece() == null) return true;
        return t.getChessPiece().isBlack() != black;
    }

    // gets available lateral tiles by looking at each tile radially (outward from the central provided tile)
    // for use with available rook and queen moves
    public ArrayList<Tile> getUnblockedLateralTiles(Tile t, boolean black) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        int curRow = t.getRow();
        int curCol = t.getColumn();
        for(int row = t.getRow() + 1; radialCheck(getTileAt(row, curCol), black); row++) {
            Tile checkedTile = getTileAt(row, curCol);
            tiles.add(checkedTile);

            // breaks loop after first piece of opposite color found
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int row = t.getRow() - 1; radialCheck(getTileAt(row, curCol), black); row--) {
            Tile checkedTile = getTileAt(row, curCol);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int col = t.getColumn() + 1; radialCheck(getTileAt(curRow, col), black); col++) {
            Tile checkedTile = getTileAt(curRow, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int col = t.getColumn() - 1; radialCheck(getTileAt(curRow, col), black); col--) {
            Tile checkedTile = getTileAt(curRow, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        return tiles;
    }

    // gets available diagonal tiles by looking at each tile radially (outward from the central provided tile)
    // for use with available bishop and queen moves
    public ArrayList<Tile> getUnblockedDiagonalTiles(Tile t, boolean black) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for(int row = t.getRow() - 1, col = t.getColumn() - 1; radialCheck(getTileAt(row,col),black); row--, col--) {
            Tile checkedTile = getTileAt(row, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int row = t.getRow() - 1, col = t.getColumn() + 1; radialCheck(getTileAt(row,col),black); row--, col++) {
            Tile checkedTile = getTileAt(row, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int row = t.getRow() + 1, col = t.getColumn() + 1; radialCheck(getTileAt(row,col),black); row++, col++) {
            Tile checkedTile = getTileAt(row, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        for(int row = t.getRow() + 1, col = t.getColumn() - 1; radialCheck(getTileAt(row,col),black); row++, col--) {
            Tile checkedTile = getTileAt(row, col);
            tiles.add(checkedTile);
            if(checkedTile.getChessPiece() != null) break;
        }
        return tiles;
    }

    // de-highlights all tiles
    public void deselectAll() {
        for(Tile[] row: tileBoard) {
            for(Tile t: row) {
                t.setHighlighted(false);
            }
        }
    }

    // creates the prompt that allows a pawn that reaches the end to be promoted
    public void promotePawn(Tile t) {
        JDialog promoteWindow = new JDialog(frame, "Pawn Promotion", true);
        promoteWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(new JLabel("Pick a piece to promote to   "));

        JComboBox pieceList = new JComboBox(new String[] {"Queen", "Rook", "Bishop", "Knight"});
        panel.add(pieceList);

        JButton selectionButton = new JButton("Select");
        selectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedPieceName = (String) pieceList.getSelectedItem();
                if(t.getChessPiece().isBlack()) {
                    if (selectedPieceName.equals("Queen")) {
                        t.setChessPiece(new Queen(t, new ImageIcon(getClass().getResource("/ChessPieceImages/BlackQueen.png")).getImage(), true));
                    } else if(selectedPieceName.equals("Rook")) {
                        t.setChessPiece(new Rook(t, new ImageIcon(getClass().getResource("/ChessPieceImages/BlackRook.png")).getImage(), true));
                    } else if(selectedPieceName.equals("Bishop")) {
                        t.setChessPiece(new Bishop(t, new ImageIcon(getClass().getResource("/ChessPieceImages/BlackBishop.png")).getImage(), true));
                    } else {
                        t.setChessPiece(new Knight(t, new ImageIcon(getClass().getResource("/ChessPieceImages/BlackKnight.png")).getImage(), true));
                    }
                } else {
                    if (selectedPieceName.equals("Queen")) {
                        t.setChessPiece(new Queen(t, new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteQueen.png")).getImage(), false));
                    } else if(selectedPieceName.equals("Rook")) {
                        t.setChessPiece(new Rook(t, new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteRook.png")).getImage(), false));
                    } else if(selectedPieceName.equals("Bishop")) {
                        t.setChessPiece(new Bishop(t, new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteBishop.png")).getImage(), false));
                    } else {
                        t.setChessPiece(new Knight(t, new ImageIcon(getClass().getResource("/ChessPieceImages/WhiteKnight.png")).getImage(), false));
                    }
                }
                promoteWindow.setVisible(false);
            }
        });
        panel.add(selectionButton);

        promoteWindow.add(BorderLayout.CENTER, panel);
        promoteWindow.pack();

        // sets location in middle of screen
        promoteWindow.setLocation((int) (frame.getLocation().x + (frame.getSize().getWidth() - promoteWindow.getWidth()) / 2),(int)(frame.getLocation().y + (frame.getSize().getHeight() - promoteWindow.getHeight()) / 2));
        promoteWindow.setVisible(true);
        deselectAll();
    }

    // changes turn after a player makes a move, checking (ideally) for check, checkmate, or stalemate
    public void changeTurn() {
        checkWin(blackTurn);
        blackTurn = !blackTurn;
        staleMateCheck();
    }

    // returns all the pieces of one side still on board
    public ArrayList<Piece> getAllLivingPieces(boolean black) {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for(Tile[] row: tileBoard) {
            for(Tile t: row) {
                Piece p = t.getChessPiece();
                if(p != null && p.isBlack() == black) pieces.add(t.getChessPiece());
            }
        }
        return pieces;
    }

    // returns a list of all tiles attacked by one side's pieces
    public ArrayList<Tile> getAllAttackedTiles(boolean black) {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        // kings and pawns can't/won't necessarily be able to capture on tiles they can move to
        for(Piece p: getAllLivingPieces(black)) {
            if(p instanceof King) {
                for(Tile t: ((King) p).getRawAttackingTiles()) {
                    tiles.add(t);
                }
            } else if(p instanceof Pawn) {
                for (Tile t : ((Pawn) p).getAttackingTiles()) {
                    tiles.add(t);
                }
            } else {
                for (Tile t : p.getMovableTiles()) {
                    tiles.add(t);
                }
            }
        }

        return tiles;
    }

    // counts the amount of available moves for each side's pieces for stalemate detection
    public int countAvailableMoves(boolean black) {
        int count = 0;
        for(Piece p: getAllLivingPieces(black)) {
            count += p.getMovableTiles().size();
        }
        return count;
    }

    // executes if staleMate is detected
    public void staleMate() {
        JOptionPane.showMessageDialog(frame, "Stalemate","Game Ended",JOptionPane.INFORMATION_MESSAGE);
        isStopped = true;
    }

    public void staleMateCheck() {
        if(countAvailableMoves(blackTurn) == 0) staleMate();
    }

    // checks if someone took the king, thus winning the game
    public void checkWin(boolean black) {
        boolean win = true;
        for(Piece p: getAllLivingPieces(!black)) {
            if(p instanceof King) {
                win = false;
                break;
            }
        }

        if(win) {
            if(black) {
                JOptionPane.showMessageDialog(frame,"Black Wins!","Game Ended",JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,"White Wins!","Game Ended",JOptionPane.INFORMATION_MESSAGE);
            }
            isStopped = true;
        }
    }
}