package Board;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;

import Pieces.Piece;

public abstract class  Tile {
// Class for creating the chess tiles (single tile)

    protected final int tileCord; // tileNumber

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile>createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i <= BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        // or unmodifiableMap(emptyTileMap)
        return ImmutableMap.copyOf(emptyTileMap);
    }

    // only this class can create tiles
    public static Tile createTile(final int tileCord, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCord, piece): EMPTY_TILES.get(tileCord);
    }


    // Constructor to create an individual tile
    private	Tile(final int tileCord) {
        this.tileCord = tileCord;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece(); // get the piece off an empty tile

    public int getTileCord() {
        return this.tileCord;
    }

    // Subclasses for empty or occupied tiles

    public static final class EmptyTile extends Tile{
        private EmptyTile(final int coordinate) {
            super(coordinate);
        }


        @Override
        public String toString() {
            return "-";

        }

        @Override
        public boolean isTileOccupied() {
            // there is no piece on the tile;
            return false;
        }

        @Override
        public Piece getPiece() {

            return null; // no piece to return
        }

    }


    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile;

        private OccupiedTile(int tileCord,final Piece pieceOnTile) {
            super(tileCord);
            this.pieceOnTile = pieceOnTile;
        }

        @Override public String toString(){
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            // there is a piece on the tile
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile; // return the piece from the tile
        }


    }
}
