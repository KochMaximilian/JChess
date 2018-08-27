package Gui;


import Board.Move;
import Pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static Gui.Table.*;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel soutPanel;

    private static final Color PANEL_COLOR = Color.decode("0xfdf5e6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.soutPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.soutPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.soutPanel, BorderLayout.SOUTH);
        this.setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {

        this.soutPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final  List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceAlliance().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("You shall not pass");
                }
            }
        }
        Collections.sort(whiteTakenPieces, (o1, o2) -> Ints.compare(o1.getPieceValue(), o2.getPieceValue()));

        Collections.sort(blackTakenPieces, (o1, o2) -> Ints.compare(o1.getPieceValue(), o2.getPieceValue()));

        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("/art/pieces/plain" + takenPiece.getPieceAlliance().toString().substring(0,1) +
                        "" + takenPiece.toString()));
                final ImageIcon  icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.soutPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("/art/pieces/plain" + takenPiece.getPieceAlliance().toString().substring(0,1) +
                        "" + takenPiece.toString()));
                final ImageIcon  icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.soutPanel.add(imageLabel);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}

