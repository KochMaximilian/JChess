package Pieces;

import Board.Board;
import Board.Move;
import Board.Tile;
import Board.BoardUtils;
import Game.Alliance;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Board.Move.*;

public class King extends Piece{

    private static final int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    public King(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance,true);
    }

    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance,isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();


        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            // Edge cases:
            if(isFirstCollumnExclusion(this.piecePosition,currentCandidateOffset) || isEighthCollumnExclusion(this.piecePosition,currentCandidateOffset)){
                continue;
            }

            if (BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestiantion = candidateDestinationTile.getPiece();
                    final Alliance pieceAllliance = pieceAtDestiantion.getPieceAlliance();
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestiantion));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoord(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }



    // Move exclusions for the Columns 1 and 8
    private static boolean isFirstCollumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
    }

    private static boolean isEighthCollumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
    }
}
