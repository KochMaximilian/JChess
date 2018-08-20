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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board, final Collection<Move> whiteStandartMove, final Collection<Move> blackStandartMove) {
        super(board,whiteStandartMove,blackStandartMove);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE ;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {

        final List<Move> kingCastles = new ArrayList<>();

        if(this.palyerKing.isFirstMove() && !this.isInCeck()){
            // White king side castle:
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61,opponentLegals).isEmpty() && Player.calculateAttacksOnTile(62,opponentLegals).isEmpty()
                            && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.palyerKing, 62 , (Rook)rookTile.getPiece(), rookTile.getTileCord(), 61));
                    }
                }
            }
            if(!this.board.getTile(59).isTileOccupied() && ! this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final  Tile rookTile = this.board.getTile(56);
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() && Player.calculateAttacksOnTile(58,opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(59,opponentLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new QueenSideCastleMove(this.board, this.palyerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCord(), 59));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
