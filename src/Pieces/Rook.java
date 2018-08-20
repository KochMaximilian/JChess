package Pieces;


import Board.Board;
import Board.Move;
import Board.BoardUtils;
import Board.Tile;
import Game.Alliance;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Board.Move.*;

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8,-1,1,8};

    public Rook(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.ROOK, piecePosition, pieceAlliance,true);
    }

    public Rook(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(PieceType.ROOK, piecePosition, pieceAlliance,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {

            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {

                if (isFirstColumnExeption(candidateDestinationCoordinate, candidateCoordinateOffset) && isEighthColumnExeption(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    continue;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;

                if (BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {

                        final Piece piecAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = piecAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, piecAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(final Move move) {
        return new Rook(move.getDestinationCoord(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    // exclusions for the rook on col 1 and 8
    private static boolean isFirstColumnExeption(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -1));
    }

    private static boolean isEighthColumnExeption(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 1));
    }


}
