package Board;

import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook;

import static Board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoord;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();


    private Move(final Board board, final Piece movedPiece, final int destinationCoord){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoord = destinationCoord;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoord) {
        this.board = board;
        this.destinationCoord = destinationCoord;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoord;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();

        return result;
    }

    @Override
    public  boolean equals(final Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof Move)) {
            return false;
        }

        final  Move otherMove = (Move) other;

        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() && getDestinationCoord() == otherMove.getDestinationCoord() && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return  this.getMovedPiece().getPiecePosition();
    }

    public int getDestinationCoord() {
        return this.destinationCoord;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {

        final Builder builder = new Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if(!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }


    // concrete subclasses

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoord);
        }
    }


    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,final Piece movedPiece,final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public  boolean equals(final Object other) {

            if(this == other) {
                return true;
            }

            if(!(other instanceof AttackMove)) {
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece() == otherAttackMove.getAttackedPiece();
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }
    }


    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord, attackedPiece);
        }
    }


    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoord, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoord, attackedPiece);
        }
    }


    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoord) {
            super(board, movedPiece, destinationCoord);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();
            for (final  Piece piece : this.board.currentPlayer().getActivePieces()) {

                if(!(this.movedPiece.equals(piece))) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    // Castle moves:

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;


        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoord, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();

            for (final  Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAlliance()));  // TODO: 09.08.2018 look into the first move in normal pieces
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }


    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoord, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord, castleRook,castleRookStart,castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }



    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoord, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoord,castleRook,castleRookStart,castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }


    public static final class NullMove extends Move {

        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute the null move");
        }
    }


    public static class MoveFactory {


        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }


        public static Move createMove (final Board board, final int currentCoordinate, final int destinationCoordinate) {

            for (Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoord() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
