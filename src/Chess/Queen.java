package Chess;

class Queen extends ChessPiece {
    public Queen(String chessColor) {
        super(chessColor);
        this.symbol = "Q";
    }

    @Override
    boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn) {
        int range;
        //Проверяем, что нет выхода за поле и что ход не совершается в ту же точку
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0 || toLine == line && toColumn == column) {
            return false;
        }
        // Проверяем, что конечной точкой маршрута не является фигура игрока
        if (chessBoard.checkMineFig(line, column, toLine, toColumn)){
            return false;
        }
        // Реализуем алгоритм хода по диагонали либо по одной из линий
        if (Math.abs(toLine - line) == Math.abs(toColumn - column) || toLine == line || toColumn == column) {
            // В зависимости от направления движения задаем расстояние для трассировки хода
            if (Math.abs(toLine - line) > Math.abs(toColumn - column))
                range = Math.abs(toLine - line);
            else range = Math.abs(toColumn - column);
            // Реализуем проверку маршрута движения на наличие мешающих передвижению фигур
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
                if (toLine == line && toColumn > column){
                    if(chessBoard.checkFig(line, column + 1 + i))
                        return false;
                }
                if (toLine == line && toColumn < column){
                    if(chessBoard.checkFig(line, column - 1 - i))
                        return false;
                }
                if (toLine > line && toColumn == column){
                    if(chessBoard.checkFig(line + 1 + i, column))
                        return false;
                }
                if (toLine < line && toColumn == column){
                    if(chessBoard.checkFig(line - 1 - i, column))
                        return false;
                }
            }
            return true; // Если ничего не мешает совершаем ход
        }
        else return false;
    }
    @Override
    String getSymbol(){
        return symbol;
    }
}
