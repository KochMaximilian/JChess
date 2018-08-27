package Player;

import Board.Board;
import Board.Move;
import Board.MoveTransition;
import Board.MoveStatus;
import Game.Alliance;
import Pieces.King;
import Pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public  abstract class Player {

    protected final Board board;
    protected final King palyerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;


    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {

        this.board = board;
        this.palyerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.palyerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    public King getPalyerKing() {
        return this.palyerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {

        final List<Move> attackMove = new ArrayList<>();
        for(final Move move : moves) {
            if(piecePosition == move.getDestinationCoord()) {
                attackMove.add(move);
            }
        }
        return ImmutableList.copyOf(attackMove);
    }


    private King establishKing() {
        for(final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("You shall not chess! (board is not valid)");
    }


    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move); 
    }


    public boolean isInCeck() {
        return this.isInCheck;
    }


    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }


    protected boolean hasEscapeMoves() {

        for (final Move move : this.legalMoves) {

            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }


    public boolean isImStaleMate() {

        return !this.isInCheck && !hasEscapeMoves();
    }


    public boolean isCastled() {
        return false;
    }


    public MoveTransition makeMove(final Move move) {

        if(!this.legalMoves.contains(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPalyerKing().getPiecePosition(),transitionBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public abstract  Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);


}
