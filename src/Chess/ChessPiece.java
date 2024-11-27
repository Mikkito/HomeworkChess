package Chess;

/*
Интерфейс для шахматных фигур тут задаем свойства для проверки первого хода фигуры, отслеживания шаха,
контроля возможности взятия на проходе, а также свойств цвета и символа
 */
abstract class ChessPiece{
    String color;
    boolean check = true;
    boolean checkMate = false;
    boolean take = false;
    boolean passingShort;
    String symbol;
    public ChessPiece(String chessColor){
        this.color = chessColor;
    }
    public String getColor(){
        return color;
    }
    abstract boolean canMoveToPosition(ChessBoard chessBoard,int line, int column, int toLine, int toColumn);
    abstract String getSymbol();
}