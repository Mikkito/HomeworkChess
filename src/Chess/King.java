package Chess;

class King extends ChessPiece{
    boolean checkMate = false;
    public King(String chessColor){
        super(chessColor);
        this.symbol = "K";
    }
    @Override
    boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn){
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0){
            return false;} // Проверка, что игрок остается в пределях доски
        if (chessBoard.checkMineFig(line, column, toLine, toColumn)){
            return false; // Проверка, что конечная позиция не занята другой фигурой игрока
        }
        // Алгоритм движения
        if ((Math.abs(toLine - line) == 1 || toLine == line) && (Math.abs(toColumn-column) == 1 || toColumn == column)){
            if (toLine == line && toColumn == column) return false; // Запрет указания исходной точки как конечной
            return true;}
        else return false;
    }
    @Override
    String getSymbol(){
        return symbol;
    }
}
