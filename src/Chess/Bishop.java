package Chess;

class Bishop extends ChessPiece{
    public Bishop (String chessColor){
        super(chessColor);
        this.symbol = "B";
    }
    @Override
    boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn){
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0){
            return false;} // Проверяем, что игрок не пытается сходить за доску
        // Проверяем не занята ли конечная позиция фигурой игрока, совершающего ход
        if (chessBoard.checkMineFig(line, column, toLine, toColumn)){
            return false;
        }
        /*
            Задаем условие движения по диагонали и производим трассировку маршрута, чтобы убедится
            в отсутствии фигур между начальной и конечной точкой движения.
         */
        if (Math.abs(toLine - line) == Math.abs(toColumn - column) && toLine != line){
            int range = Math.abs(toLine - line);
            for (int i = 0; i < range - 1; i++){
                if (toLine < line && toColumn < column){
                    if(chessBoard.checkFig(line - 1 - i, column - 1 - i))
                        return false;
                }
                if (toLine > line && toColumn < column){
                    if(chessBoard.checkFig(line + 1 + i, column - 1 - i))
                        return false;
                }
                if (toLine > line && toColumn > column){
                    if(chessBoard.checkFig(line + 1 + i, column + 1 + i))
                        return false;
                }
                if (toLine < line && toColumn > column){
                    if(chessBoard.checkFig(line - 1 - i, column + 1 + i))
                        return false;
                }
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