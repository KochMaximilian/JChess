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

public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-8,-7,-1,1,7,8,9};

    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance,true);
    }

    public Queen(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance,isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){

            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCoord(candidateDestinationCoordinate)){

                if(isFirstColumnExeption(candidateDestinationCoordinate,candidateCoordinateOffset) && isEighthColumnExeption(candidateDestinationCoordinate,candidateCoordinateOffset)){
                    continue;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;

                if(BoardUtils.isValidTileCoord(candidateDestinationCoordinate)) {

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    } else {

                        final Piece piecAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = piecAtDestination.getPieceAlliance();

                        if(this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,piecAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getDestinationCoord(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    // exclusions for the bishop on col 1 and 8
    private static boolean isFirstColumnExeption(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -1) || (candidateOffset == -9) || (candidateOffset == 7));
    }

    private static boolean isEighthColumnExeption(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 1) || (candidateOffset == -7) || (candidateOffset == 9));
    }


}
