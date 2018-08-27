package Pieces;

import Board.Board;
import Board.BoardUtils;
import Board.Move;
import Board.Tile;
import Game.Alliance;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Board.Move.*;


public class Knight extends Piece{

    // Possible positions of the kigth on the chessboard based on his options to move around the board
    public final static int [] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance,true);
    }

    public Knight(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance,isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate;
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoord(candidateDestinationCoordinate)){
                if(isEighthCollumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isSeventhCollumnExclusipon(this.piecePosition,currentCandidateOffset)||
                        isSecondCollumnExclusion(this.piecePosition,currentCandidateOffset)||
                        isFirstCollumnExclusion(this.piecePosition,currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if(this.pieceAlliance!=pieceAlliance){
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }


    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCoord(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    // Move exclusions for the Columns 1,2,7,8
    private static boolean isFirstCollumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondCollumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhCollumnExclusipon(final int currentPosition,final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthCollumnExclusion(final int currentPosition,final int candidateOffeset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffeset == -15) || (candidateOffeset == -6) || (candidateOffeset == 10) || (candidateOffeset == 17));
    }
}
