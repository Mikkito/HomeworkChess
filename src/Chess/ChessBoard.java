package Chess;

import java.util.Scanner;

public class ChessBoard {
    public ChessPiece[][] board = new ChessPiece[8][8]; // creating a field for game
    String nowPlayer;
    int[] savedWKingPosition = new int[] {0, 4};
    int[] savedBKingPosition = new int[] {7, 4};

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    public boolean moveToPosition(int startLine, int startColumn, int endLine, int endColumn) {
        if (checkPos(startLine) && checkPos(startColumn)) {
            if (board[startLine][startColumn] == null) return false; // Проверка, ходищь фигурой, а не пустотой
            if (!nowPlayer.equals(board[startLine][startColumn].getColor())) return false; // Проверка, что ходишь своими
            if (board[endLine][endColumn] != null && nowPlayer.equals(board[endLine][endColumn].getColor())) return false;
            if (board[startLine][startColumn].canMoveToPosition(this, startLine, startColumn, endLine, endColumn)) {
                /* Имитируем ход и, если в ходе хода, возникает шах себе или не снимается, ранее поставленный, шах,
                    запрещаем ход*/
                if (!moveImitation(startLine, startColumn, endLine, endColumn)){
                    return false;}
                board[endLine][endColumn] = board[startLine][startColumn]; // if piece can move, we moved a piece
                /* После передвижения пешки для взятия на проходе осуществляет удаление пешки опонента
                    и соответственно обнуляет вспомогательное свойство позволяющее выполнить данную операцию.
                 */
                if (board[endLine][endColumn].getSymbol().equals("P") && board[endLine][endColumn].passingShort){
                    board[startLine][endColumn] = null;
                    board[endLine][endColumn].passingShort = false;
                }
                // Сохранение позиций королей
                if (board[startLine][startColumn].getSymbol().equals("K") && nowPlayerColor().equals("White")){
                    savedWKingPosition[0] = endLine;
                    savedWKingPosition[1] = endColumn;
                }
                if (board[startLine][startColumn].getSymbol().equals("K") && nowPlayerColor().equals("Black")){
                    savedBKingPosition[0] = endLine;
                    savedBKingPosition[1] = endColumn;
                }

                // Контроль передвижения ладьи и короля
                if (board[startLine][startColumn].getSymbol().equals("K") || board[startLine][startColumn].getSymbol().equals("R")){
                    board[endLine][endColumn].check = false;
                }
                // Контроль первого передвижения пешки для запрета передвижения на два хода
                if (board[startLine][startColumn].getSymbol().equals("P")){
                    board[endLine][endColumn].check = false;
                }

                board[startLine][startColumn] = null; // устанавливаем значение null для стартовой позиции

                // Проверка достижения пешкой конца доски
                if (board[endLine][endColumn].getSymbol().equals("P") && checkEvolution(endLine, endColumn))
                    evolution(endLine, endColumn);
                // Проверка шаха
                check();
                //Очистка свойства пешек разрешающего взятие на проходе
                resetTake();
                //Смена игрока и завершение хода
                this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
                return true;
            } else return false;
        } else return false;
    }

    public void printBoard() {  //print board in console
        System.out.println("Turn " + nowPlayer);
        System.out.println();
        System.out.println("Player 2(Black)");
        System.out.println();
        System.out.println("\t0\t1\t2\t3\t4\t5\t6\t7");

        for (int i = 7; i > -1; i--) {
            System.out.print(i + "\t");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.print(board[i][j].getSymbol() + board[i][j].getColor().substring(0, 1).toLowerCase() + "\t");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("Player 1(White)");
    }
    // Проверка что в указанной точке есть фигура
    public boolean checkFig(int endLine, int endColumn){
        if (board[endLine][endColumn] == null)
            return false;
        return true;
    }
    // Проверяет наличие вражеской фигуры в указанной координате
    public boolean checkAlienFig(int startLine, int startColumn, int endLine, int endColumn){
        if (checkFig(endLine, endColumn) && !board[startLine][startColumn].getColor().equals(board[endLine][endColumn].getColor()))
            return true;
        return false;
    }
    // Проверка есть ли в заданной координате другая фигура игрока
    public boolean checkMineFig(int startLine, int startColumn, int endLine, int endColumn){
        if (checkFig(endLine, endColumn) && board[startLine][startColumn].getColor().equals(board[endLine][endColumn].getColor()))
            return true;
        return false;
    }
    // Проверяем наличие вражеской пешки которую допустимо взять на проходе
    public boolean checkAlienPawn(int endLine, int endColumn){
        if (checkFig(endLine, endColumn)){
            if (!this.nowPlayerColor().equals(board[endLine][endColumn].getColor()) && board[endLine][endColumn].take)
                return true;}
        return false;
    }
    // Для проверки что заданное значение не выходит за пределы доски
    public boolean checkPos(int pos) {
        return pos >= 0 && pos <= 7;
    }
    // Функции реализующие рокировки длинную и короткую соответственно
    public boolean castlingShort(){
        if (nowPlayer.equals("White")){
            if (board[0][4] == null || board[0][7] == null) return false;
            if (board[0][4].getSymbol().equals("K") && board[0][7].getSymbol().equals("R") &&
                    board[0][4].check && board [0][7].check &&
                    board[0][7].canMoveToPosition(this, 0, 7, 0, 5) &&
            !moveTracing(0,5) && !moveTracing(0, 6) && !board[0][4].checkMate) {
                board[0][5] = board[0][7];
                board[0][6] = board[0][4];
                board[0][7] = null;
                board[0][4] = null;
                savedWKingPosition[0] = 0;
                savedWKingPosition[1] = 6;
                resetTake();
                this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
                return true;}
        }
        if (nowPlayer.equals("Black")){
            if (board[7][4] == null || board[7][7] == null) return false;
            if (board[7][4].getSymbol().equals("K") && board[7] [7].getSymbol().equals("R") &&
                    board[7][4].check && board [7][7].check &&
                    board[7][7].canMoveToPosition(this, 7, 7, 7, 5) &&
                    !moveTracing(7, 5) && !moveTracing(7, 6) &&
                    !board[7][4].checkMate) {
                board[7][5] = board[7][7];
                board[7][6] = board[7][4];
                board[7][7] = null;
                board[7][4] = null;
                savedBKingPosition[0] = 7;
                savedBKingPosition[1] = 6;
                resetTake();
                this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
                return true;}
        }
        return false;
    }
    public boolean castlingLong(){
        if (nowPlayer.equals("White")){
            if (board[0][4] == null || board[0][0] == null) return false;
            if (board[0][4].getSymbol().equals("K") && board[0][0].getSymbol().equals("R") &&
                    board[0][4].check && board [0][0].check &&
                    board[0][0].canMoveToPosition(this, 0, 0, 0, 3) &&
                !moveTracing(0,3) && !moveTracing(0,2) && !moveTracing(0,1)
                && !board[0][4].checkMate) {
                board[0][3] = board[0][0];
                board[0][2] = board[0][4];
                board[0][0] = null;
                board[0][4] = null;
                savedWKingPosition[0] = 0;
                savedWKingPosition[1] = 2;
                this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
                return true;}
        }
        if (nowPlayer.equals("Black")){
            if (board[7][4] == null || board[7][0] == null) return false;
            if (board[7][4].getSymbol().equals("K") && board[7][0].getSymbol().equals("R") &&
                    board[7][4].check && board [7][0].check &&
                    board[7][0].canMoveToPosition(this, 7, 0, 7, 3) &&
                !moveTracing(7, 3) && !moveTracing(7, 2) && !moveTracing(7, 1)
                && !board[7][4].checkMate) {
                board[7][3] = board[7][0];
                board[7][2] = board[7][4];
                board[7][0] = null;
                board[7][4] = null;
                savedBKingPosition[0] = 7;
                savedBKingPosition[1] = 2;
                this.nowPlayer = this.nowPlayerColor().equals("White") ? "Black" : "White";
                return true;}
        }
        return false;
    }
    /* Упрощенная имитация ходов (теоретически можно упростить поправив логику имитации ходов)
     используется в рокировках для проверки что поля не под ударом*/
    private boolean moveTracing(int endLine, int endColumn){
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[endLine][endColumn] != null) {
                    if (!board[i][j].getColor().equals(board[endLine][endColumn].getColor())
                            && board[i][j].canMoveToPosition(this, i, j, endLine, endColumn)) {
                        return true;
                    }
                }
                if (board[i][j] != null){
                    if (board[i][j].canMoveToPosition(this, i, j, endLine, endColumn) && !board[i][j].getColor().equals(nowPlayer))
                        return true;
                }
            }
        }
        return false;
    }
    /*Проходит по всем фигурам и проверяет может ли фигура опонента взять фигуру расположенную по заданным координатам
    * используется для проверки шаха на короля*/
    private boolean checkControl(int yPosition, int xPosition){
        if (board[yPosition][xPosition] == null){
            return false;}
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].canMoveToPosition(this, i, j, yPosition, xPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /* Данная функция осуществляет имитацию хода и запрещает его если в результате не будет снят шах
    или наоборот возникнет шах королю игрока.
     */
    private boolean moveImitation(int startLine, int startColumn, int endLine, int endColumn){
        int[] localWKing = new int[]{savedWKingPosition[0], savedWKingPosition[1]};
        int[] localBKing = new int[]{savedBKingPosition[0], savedBKingPosition[1]};
        if (board[startLine][startColumn].canMoveToPosition(this, startLine, startColumn, endLine, endColumn)) {
            ChessPiece endPosition = null;
            endPosition = board[endLine][endColumn];
            board[endLine][endColumn] = board[startLine][startColumn];
            board[startLine][startColumn] = null;
            if (board[endLine][endColumn].getSymbol().equals("K") && nowPlayer.equals("White")){
                localWKing[0] = endLine;
                localWKing[1] = endColumn;
            }
            if (board[endLine][endColumn].getSymbol().equals("K") && nowPlayer.equals("Black")){
                localBKing[0] = endLine;
                localBKing[1] = endColumn;
            }
            if (board[endLine][endColumn].getSymbol().equals("P") && board[endLine][endColumn].passingShort){
                ChessPiece savedPawn= board[startLine][endColumn];
                board[startLine][endColumn] = null;
                if (this.nowPlayerColor().equals("White") && checkControl(localWKing[0], localWKing[1])){
                    board[startLine][endColumn] = savedPawn;
                    board[startLine][startColumn] = board[endLine][endColumn];
                    board[endLine][endColumn] = endPosition;
                    return false;}
                if (this.nowPlayerColor().equals("Black") && checkControl(localBKing[0], localBKing[1])){
                    board[startLine][endColumn] = savedPawn;
                    board[startLine][startColumn] = board[endLine][endColumn];
                    board[endLine][endColumn] = endPosition;
                    return false;}
                board[startLine][endColumn] = savedPawn;
                board[startLine][startColumn] = board[endLine][endColumn];
                board[endLine][endColumn] = endPosition;
                return true;
            }
            if (this.nowPlayerColor().equals("White") && checkControl(localWKing[0], localWKing[1])){
                board[startLine][startColumn] = board[endLine][endColumn];
                board[endLine][endColumn] = endPosition;
                return false;
            }
            if (this.nowPlayerColor().equals("Black") && checkControl(localBKing[0], localBKing[1])){
                board[startLine][startColumn] = board[endLine][endColumn];
                board[endLine][endColumn] = endPosition;
                return false;}
            board[startLine][startColumn] = board[endLine][endColumn];
            board[endLine][endColumn] = endPosition;
            return true;
        }
        return false;
    }
    // Проверяем, что пешка дошла до последней клетки и реализуем превращение в выбранную фигуру
    private void evolution(int endLine, int endColumn){
        System.out.println("Пешке доступна эваолюция укажите буквой (Q, R, B, H) фигуру в которую желаете эволюционировать:");
        Scanner scanner = new Scanner(System.in);
        String Fig = scanner.nextLine();
        switch(Fig){
            case ("Q"):
                board[endLine][endColumn] = new Queen(nowPlayer);
                break;
            case ("R"):
                board[endLine][endColumn] = new Rook(nowPlayer);
                break;
            case ("B"):
                board[endLine][endColumn] = new Bishop(nowPlayer);
                break;
            case ("H"):
                board[endLine][endColumn] = new Horse(nowPlayer);
                break;
            case ("P"):
                System.out.println("Фигура не может остаться пешкой!");
                board[endLine][endColumn] = new Queen(nowPlayer);
                break;
            case ("K"):
                System.out.println("Фигура не может стать королем!");
                board[endLine][endColumn] = new Queen(nowPlayer);
                break;
        }
    }
    // Проверяем сам факт достижения пешкой конца
    private boolean checkEvolution(int endLine, int endColumn){
        if (nowPlayer.equals("White") && endLine == 7){
            return true;
        }
        if (nowPlayer.equals("Black") && endLine == 0){
            return true;
        }
        return false;
    }
    // Проверяем поставлен ли мат
    private void check(){
        if (checkControl(savedBKingPosition[0], savedBKingPosition[1]))
            board[savedBKingPosition[0]][savedBKingPosition[1]].checkMate = true;
        if (!checkControl(savedBKingPosition[0], savedBKingPosition[1]))
            board[savedBKingPosition[0]][savedBKingPosition[1]].checkMate = false;
        if (checkControl(savedWKingPosition[0], savedWKingPosition[1]))
            board[savedWKingPosition[0]][savedWKingPosition[1]].checkMate = true;
        if (!checkControl(savedWKingPosition[0], savedWKingPosition[1]))
            board[savedWKingPosition[0]][savedWKingPosition[1]].checkMate = false;
    }
    // Функция для очистки свойства позволяющего взятие на проходе для всех пешек опонента
    private void resetTake(){
        for (int i = 0; i < 8; i++){
            if (nowPlayer.equals("White")){
                if (board[4][i] != null && board[4][i].getSymbol().equals("P"))
                    board[4][i].take = false;
            }
            if (nowPlayer.equals("Black")){
                if (board[3][i] != null && board[3][i].getSymbol().equals("P"))
                    board[3][i].take = false;
            }
        }
    }
    // Возвращает информацию о шахе
    public String checkInfo(){
        if (board[savedBKingPosition[0]][savedBKingPosition[1]].checkMate)
            return "ЧЕРНЫМ ПОСТАВЛЕН ШАХ!";
        if (board[savedWKingPosition[0]][savedWKingPosition[1]].checkMate)
            return "БЕЛЫМ ПОСТАВЛЕН ШАХ!";
        return " ";
    }
}
