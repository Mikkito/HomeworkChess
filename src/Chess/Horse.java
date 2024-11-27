package Chess;

class Horse extends ChessPiece{

    public Horse(String chessColor){
        super(chessColor);
        this.symbol = "H";
    }
    @Override
    boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn){
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0)
            return false; // Проверка, что игрок не выходит за пределы поля
        if (chessBoard.checkMineFig(line, column, toLine, toColumn)){
            return false; // Проверка, что конечная позиция не занята своей фигурой
        }
        // Реализация алгоритма передвижения для коня
        if (Math.abs(toLine - line) == 2 && Math.abs(toColumn - column) == 1
                || Math.abs(toLine - line) == 1 && Math.abs(toColumn - column) == 2)
            return true;
        else return false;
    }
    @Override
    String getSymbol(){
        return symbol;
    }
}
