package puzzle.console;

/**
 * Represents the coordinates of a square of a two-dimensional game board.
 *
 * @param row the row coordinate
 * @param col the column coordinate
 */
public record Position(int row, int col) {

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }
}
