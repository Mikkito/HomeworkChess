package Chess;

class  Rook extends ChessPiece{
    public Rook(String chessColor){
        super(chessColor);
        this.symbol = "R";
    }
    @Override
    boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn){
        int range;
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0){
            return false;} // Проверка, что не выходим за доску
        if (chessBoard.checkMineFig(line, column, toLine, toColumn)){
            return false; // Проверка, что конечная позиция не занята фигурой игрока
        }
        // Проверяем что идем по одной из линий и совершаем ход
        if (toLine == line || toColumn == column){
            if (toLine == line && toColumn == column) return false;//Запрет ходить туда же откуда начинаем ход
            // Проверяем куда ходим и определяем расстояние
            if (Math.abs(toLine - line) > Math.abs(toColumn - column))
                range = Math.abs(toLine - line);
            else range = Math.abs(toColumn - column);
            if (range - 1 == 0) return true;
            //Проверяем, что на маршруте нет других фигур
            for (int i = 0; i < range - 1; i++){
                if (toColumn > column){
                    if(chessBoard.checkFig(line, column + 1 + i))
                        return false;
                }
                if (toColumn < column){
                    if(chessBoard.checkFig(line, column - 1 - i))
                        return false;
                }
                if (toLine > line){
                    if(chessBoard.checkFig(line + 1 + i, column))
                        return false;
                }
                if (toLine < line){
                    if(chessBoard.checkFig(line - 1 - i, column))
                        return false;}
            }
            return true;
        }
        else return false;
    }
    @Override
    String getSymbol(){
        return symbol;
    }
}