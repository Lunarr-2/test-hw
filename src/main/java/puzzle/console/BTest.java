package puzzle.console;


import puzzle.TwoPhaseMoveState;
import puzzle.direction.Direction;
import puzzle.solver.BreadthFirstSearch;

import java.util.HashSet;
import java.util.Set;

public class BTest implements TwoPhaseMoveState<Position> {

    private static final Set<TwoPhaseMove<Position>> MOVES = new HashSet<TwoPhaseMove<Position>>();
    public static final int BOARD_SIZE = 4;
    private boolean[][] board;


    static {
        for (int fromRow = 0; fromRow < BOARD_SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < BOARD_SIZE; fromCol++) {
                for (int toRow = 0; toRow < BOARD_SIZE; toRow++) {
                    for (int toCol = 0; toCol < BOARD_SIZE; toCol++) {
                        if (fromRow != toRow || fromCol != toCol) {
                            MOVES.add(new TwoPhaseMove<>(new Position(fromRow, fromCol), new Position(toRow, toCol)));
                        }
                    }
                }
            }
        }
    }

//    last edit
//    static {
//        // For performance reasons, all possible moves are generated in advance
//
//        for (int row = 0; row < BOARD_SIZE; row++) {
//            for (int col = 0; col < BOARD_SIZE; col++) {
//                if (row != col) {
//                    for (int r = 0; r < BOARD_SIZE; r++) {
//                        for (int c = 0; c < BOARD_SIZE; c++) {
//                            if (r != c) {
//                                Position from = new Position(row, col);
//                                Position to = new Position(r, c);
//                                TwoPhaseMove<Position> move = new TwoPhaseMove<>(from, to);
//                                MOVES.add(move);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }




    //        Set<TwoPhaseMove<Position>> legalMoves = new HashSet<>();
//        for (var row = 0; row < BOARD_SIZE; row++) {
//            for (var col = 0; col < BOARD_SIZE; col++) {
//                Position from = new Position(row, col);
//                if (isLegalToMoveFrom(from)) {
//                    for (var direction : Direction.values()) {
//                        Position to = new Position(from.row() + direction.getRowOffset(), from.col() + direction.getColOffset());
//                        TwoPhaseMove<Position> move = new TwoPhaseMove<>(from, to);
//                        if (isLegalMove(move)) {
//                            legalMoves.add(move);
//                        }
//                    }
//                }
//            }
//        }
//        return legalMoves;
//



    public BTest(){
        board = new boolean[BOARD_SIZE][BOARD_SIZE];
        board[1][1] = board[1][2] = board[2][1] = board[2][2] = true;
       // board[0][0] = board[0][3] = board[3][0] = board[3][3] = true;
    }


    @Override
    public boolean isLegalToMoveFrom(Position position) {
        return isValidPosition(position) && board[position.row()][position.col()] && hasAdjacentCoin(position);
    }

    @Override
    public TwoPhaseMoveState<Position> clone() {
        BTest copy;
        try{
            copy = (BTest) super.clone();
        }catch (CloneNotSupportedException e){
            throw new  AssertionError();
        }
        this.board = board.clone();
        return copy;
    }

    @Override
    public boolean isSolved() {
        return board[0][0] && board[0][3] && board[3][0] && board[3][3]
                && !board[1][1] && !board[1][2] && !board[2][1] && !board[2][2];
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        Position from = positionTwoPhaseMove.from();
        Position to = positionTwoPhaseMove.to();
        if (!isLegalToMoveFrom(from) || !isValidPosition(to) || board[to.row()][to.col()]) {
            return false;
        }

        // Ensure the move is either horizontal or vertical
        if (from.row() != to.row() && from.col() != to.col()) {
            return false;
        }

        // Ensure the path is clear
        return isEmptyPath(from, to);
    }

    @Override
    public void makeMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        Position from = positionTwoPhaseMove.from();
        Position to = positionTwoPhaseMove.to();

        if (isLegalMove(positionTwoPhaseMove)) {
            board[to.row()][to.col()] = true;
            board[from.row()][from.col()] = false;
        }
    }

    @Override
    public Set<TwoPhaseMove<Position>> getLegalMoves() {

        Set<TwoPhaseMove<Position>> legalMoves = new HashSet<>();
        for (TwoPhaseMove<Position> move : MOVES) {
            if (isLegalMove(move)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;



//        Set<TwoPhaseMove<Position>> legalMoves = new HashSet<>();
//        for (var row = 0; row < BOARD_SIZE; row++) {
//            for (var col = 0; col < BOARD_SIZE; col++) {
//                Position from = new Position(row, col);
//                if (isLegalToMoveFrom(from)) {
//                    for (var direction : Direction.values()) {
//                        Position to = new Position(from.row() + direction.getRowOffset(), from.col() + direction.getColOffset());
//                        TwoPhaseMove<Position> move = new TwoPhaseMove<>(from, to);
//                        if (isLegalMove(move)) {
//                            legalMoves.add(move);
//                        }
//                    }
//                }
//            }
//        }
//        return legalMoves;
    }

    private boolean isValidPosition(Position p) {
        int row = p.row();
        int col = p.col();
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    private boolean hasAdjacentCoin(Position p) {
        for (Direction direction : Direction.values()) {
            int row = p.row() + direction.getRowOffset();
            int col = p.col() + direction.getColOffset();
            if (isValidPosition(new Position(row, col)) && board[row][col]) {
                return true;
            }
        }
        return false;
    }
    private boolean isEmptyPath(Position from, Position to) {
        if (from.row() == to.row()) {
            int minCol = Math.min(from.col(), to.col());
            int maxCol = Math.max(from.col(), to.col());
            for (int col = minCol + 1; col < maxCol; col++) {
                if (board[from.row()][col]) {
                    return false;
                }
            }
            return true;
        }

        if (from.col() == to.col()) {
            int minRow = Math.min(from.row(), to.row());
            int maxRow = Math.max(from.row(), to.row());
            for (int row = minRow + 1; row < maxRow; row++) {
                if (board[row][from.col()]) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

//    @Override
//    public TwoPhaseMoveState<Position> clone() {
//        BTest clone = new BTest();
//        for (int row = 0; row < BOARD_SIZE; row++) {
//            for (int col = 0; col < BOARD_SIZE; col++) {
//                clone.board[row][col] = this.board[row][col];
//            }
//        }
//        return clone;
//    }



    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j] ? 'O' : '_').append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var bfs = new BreadthFirstSearch<TwoPhaseMove<Position>>();
        bfs.solveAndPrintSolution(new BTest());
        var state = new BTest();
       System.out.println("Initial State:\n" + state.isSolved());



        //var solution = bfs.solveAndPrintSolution(state);
        var solution = bfs.solve(state);
        System.out.println("The Solution :" + solution);
    }
}
