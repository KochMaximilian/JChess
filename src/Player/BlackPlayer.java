package Player;

import Board.Board;
import Board.Move;
import Board.Tile;
import Game.Alliance;
import Pieces.Piece;
import Pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Board.Move.*;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> whiteStandartMove,final Collection<Move> blackStandartMove) {
        super(board,blackStandartMove,whiteStandartMove);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if(this.palyerKing.isFirstMove() && !this.isInCeck()){
            // Black king side castle:
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5,opponentLegals).isEmpty() && Player.calculateAttacksOnTile(6,opponentLegals).isEmpty()
                            && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.palyerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCord(), 5));
                    }
                }
            }
            if(!this.board.getTile(1).isTileOccupied() && ! this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && Player.calculateAttacksOnTile(2,opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(3,opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {

                    kingCastles.add(new QueenSideCastleMove(this.board, this.palyerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCord(), 3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
