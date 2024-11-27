package Chess;

class Pawn extends ChessPiece{
    public Pawn(String chessColor){
        super(chessColor);
        this.passingShort = false;// Свойство выдаваемое пешке для корректной реализации взятия на проходе из ChessBoard
        this.symbol = "P";
    }
    @Override
    boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn){
        if (toLine > 7 || toLine < 0 || toColumn > 7 || toColumn < 0){
            return false;}
        // Алгоритм хода для белой пешки
        if (this.color.equals("White")){
            //Проверяем наличие по диагонали фигуры противника и при наличии разрешаем ход
            if (chessBoard.checkAlienFig(line, column, toLine, toColumn) && toLine - line == 1 && Math.abs(toColumn - column) == 1){
                return true;
            }
            // Проверяем наличие рядом с пешкой другой пешки со свойством разрешающим взятие на проходе
            if (chessBoard.checkAlienPawn(line, toColumn) && toLine - line == 1 && Math.abs(toColumn - column) == 1
                    && chessBoard.board[line][toColumn].take ){
                //Даем нашей пешке свойство позволяющее забрать рядом стоящую пешку
                this.passingShort = true;
                return true;
            }
            /*
                Стандартный ход. Проверяем ходит ли пешка впервые и если да позволяем движение на две клетки в противном
                случае только на одну
             */
            if (toLine - line == 1 && toColumn == column || this.check && toLine - line == 2 && toColumn == column){
                if (toLine - line == 2 && chessBoard.board[toLine - 1][toColumn] != null) return false;
                if (chessBoard.board[toLine][toColumn] != null) return false; //Проверяем нет ли на пути фигуры
                if (toLine - line == 2){
                    this.take = true; // Если сходили на две клетки даем свойство разрешающее взятие на проходе
                }
                return true;}
            else return false;}
        // Алгоритм хода для черной пешки аналогичен белой только с учетом движения в обратную сторону
        if (this.color.equals("Black")){
            if (chessBoard.checkAlienFig(line, column, toLine, toColumn) && toLine - line == -1 && Math.abs(toColumn - column) == 1){
                return true;
            }
            if (chessBoard.checkAlienPawn(line, toColumn) && toLine - line == -1 && Math.abs(toColumn - column) == 1
                    && chessBoard.board[line][toColumn].take){
                this.passingShort = true;
                return true;
            }
            if (toLine - line == -1 && toColumn == column || this.check && toLine - line == -2 && toColumn == column){
                if (chessBoard.board[toLine][toColumn] != null) return false;
                if (toLine - line == -2 && chessBoard.board[toLine + 1][toColumn] != null) return false;
                if (toLine - line == -2){
                    this.take = true;
                }
                return true;}
            else return false;
        }
        else return false;
    }
    @Override
    String getSymbol(){
        return symbol;
    }
}