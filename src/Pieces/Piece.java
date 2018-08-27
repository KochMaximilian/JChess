package Pieces;

import Board.Board;
import Board.Move;
import Game.Alliance;

import java.util.Collection;


public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType ,final int piecePosition, Alliance pieceAlliance, final boolean isFirstMove){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode  = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof Piece)) {
            return false;
        }

        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {

        PAWN("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q",900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        // its over 9000!
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        private  int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;

        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        public int getPieceValue() {
            return this.pieceValue;
        }
    }
}
