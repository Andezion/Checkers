import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
 
 
public class Checkers extends JPanel 
{
   public static void main(String[] args) 
   {
      JFrame window = new JFrame("Шашки");
      Checkers content = new Checkers();
      window.setContentPane(content);
      window.pack();
      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
      window.setLocation( (screensize.width - window.getWidth())/2,
            (screensize.height - window.getHeight())/2 );
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      window.setResizable(false);  
      window.setVisible(true);
   }
      
   private JButton newGameButton;  // начинаем новую игру
   private JButton resignButton;   // типа сбрасываем всю игру и 
                                   // начинаем новую
   
   private JLabel message;  // нижняя вещь с текстом
   
   public Checkers() {
      
      setLayout(null);  // I will do the layout myself.
      setPreferredSize( new Dimension(350,250) );
      
      setBackground(new Color(100,150,0));  // зеленый фон
      
      Board board = new Board();  
                               
      add(board);
      add(newGameButton);
      add(resignButton);
      add(message);
      
      /* расстановка позиций и прочей хуйни */
      
      board.setBounds(20,20,164,164); 
      newGameButton.setBounds(210, 60, 120, 30);
      resignButton.setBounds(210, 120, 120, 30);
      message.setBounds(0, 200, 350, 30);
      
   } // конец шаблона
 
   /* ну тут уже описываем методы всякие и прочее   
    */
   private static class CheckersMove {
      int fromRow, fromCol;  // для движения 
      int toRow, toCol;      // квадратэки
      CheckersMove(int r1, int c1, int r2, int c2) {
              // шаблончик дабы раздать значения чиназес
         fromRow = r1;
         fromCol = c1;
         toRow = r2;
         toCol = c2;
      }
      boolean isJump() {
             // проверка можем ли мы сделать ход
         return (fromRow - toRow == 2 || fromRow - toRow == -2);
      }
   }  
   
   
   
   /*ну тут уже показываем всё что у нас есть, таблицу и так далее
    */
   private class Board extends JPanel implements ActionListener, MouseListener {
      
      
      CheckersData board;  // ну тут наша доска и ходы 
      
      boolean gameInProgress; // играем ли мы 
      
      /* дальше только если играем  */
      
      int currentPlayer;      // чья очередь 
      int selectedRow, selectedCol;  // ну куда ходим 
      
      CheckersMove[] legalMoves;  // куда можем ходить, сделано уёбищно
 
      /*создаём доску, кнопки и так далее
       */
      Board() {
         setBackground(Color.BLACK);
         addMouseListener(this);
         resignButton = new JButton("Слиться");
         resignButton.addActionListener(this);
         newGameButton = new JButton("Новая игра");
         newGameButton.addActionListener(this);
         message = new JLabel("",JLabel.CENTER);
         message.setFont(new  Font("Serif", Font.BOLD, 14));
         message.setForeground(Color.green);
         board = new CheckersData();
         doNewGame();
      }
      
      
      /**
       * проверяем чё нажали там 
       */
      public void actionPerformed(ActionEvent evt) {
         Object src = evt.getSource();
         if (src == newGameButton)
            doNewGame();
         else if (src == resignButton)
            doResign();
      }
      
      
      /**
       * новая игра 
       */
      void doNewGame() {
         if (gameInProgress == true) {
               // добавил потому что Петр Дух момент
            message.setText("Закончи текущую игру!");
            return;
         }
         board.setUpGame();   // Set up the pieces.
         currentPlayer = CheckersData.RED;   // красные первые хихи
         legalMoves = board.getLegalMoves(CheckersData.RED);  // проверям куда ходить то можно
         selectedRow = -1;   // пока красный думает 
         message.setText("Красный ходи заебал");
         gameInProgress = true;
         newGameButton.setEnabled(false);
         resignButton.setEnabled(true);
         repaint();
      }
      
      
      /**
       * если кто-то слился 
       */
      void doResign() {
         if (gameInProgress == false) {  // дух момент х2
            message.setText("Игра не начата!");
            return;
         }
         if (currentPlayer == CheckersData.RED)
            gameOver("Красный слился - Чёрный победил");
         else
            gameOver("Чёрный слился - Красный победил");
      }
      
      
      /**
       * игра окончена и мы всё очищаем возвращая на начальные позиции 
       */
      void gameOver(String str) {
         message.setText(str);
         newGameButton.setEnabled(true);
         resignButton.setEnabled(false);
         gameInProgress = false;
      }
      
      
      /**
       * ну проверяем че там клацнули мышкой йоу
       */
      void doClickSquare(int row, int col) {
         
         /* если нажал на то, что не может ходить - то ничего не светиться и юзер попуск */
         
         for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
               selectedRow = row;
               selectedCol = col;
               if (currentPlayer == CheckersData.RED)
                  message.setText("Красный ходи ну ёмаё");
               else
                  message.setText("Чёрный беги ахаха");
               repaint();
               return;
            }
         
         /* если юзер даун и не клацает ничего овощ */
         
         if (selectedRow < 0) {
            message.setText("Нажми чтобы играть ало ");
            return;
         }
         
         /* всё норм и мы ходим спокойно  */
         
         for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                  && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
               doMakeMove(legalMoves[i]);
               return;
            }
         
         /* опять же обработка ошибки  */
         
         message.setText("Нажми на шашку дабы сдвинуть");
         
      }  // end doClickSquare()
      
      
      /**
       * если какой-то длинный ход 
       */
      void doMakeMove(CheckersMove move) {
         
         board.makeMove(move);
         
         /* ищем все возможные ходы еее
          */
         
         if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer,move.toRow,move.toCol);
            if (legalMoves != null) {
               if (currentPlayer == CheckersData.RED)
                  message.setText("Красный ходит дальше");
               else
                  message.setText("Чёрный ходит дальше");
               selectedRow = move.toRow;  // аче двигать
               selectedCol = move.toCol;
               repaint();
               return;
            }
         }
         
         /* ищем хода и в случае чего говорим кто победил */
         
         if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
               gameOver("У чёрных нет прав - красные победили");
            else if (legalMoves[0].isJump())
               message.setText("Чёрный обязан ходить");
            else
               message.setText("Чёрный ходи!!");
         }
         else {
            currentPlayer = CheckersData.RED;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
               gameOver("У красного нет выбора - Чёрный победил");
            else if (legalMoves[0].isJump())
               message.setText("Красный ходииии");
            else
               message.setText("Двигайся бро");
         }
         
         /* никто не выбрал ничего */
         
         selectedRow = -1;
         
         /* если нет выбора - то мы сами показываем что клацать нужно */
         
         if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
               if (legalMoves[i].fromRow != legalMoves[0].fromRow
                     || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                  sameStartSquare = false;
                  break;
               }
            if (sameStartSquare) {
               selectedRow = legalMoves[0].fromRow;
               selectedCol = legalMoves[0].fromCol;
            }
         }
         
         /* если конец - перерисовываем  */
         
         repaint();
         
      }  // end doMakeMove();
      
      
      /**
       * рисование этой фигни 
       */
      public void paintComponent(Graphics g) {
         
         /* ля ля ля рисовашки дупа попа */
         
         g.setColor(Color.black);
         g.drawRect(0,0,getSize().width-1,getSize().height-1);
         g.drawRect(1,1,getSize().width-3,getSize().height-3);
         
         /* рисуем квадраты */
         
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if ( row % 2 == col % 2 )
                  g.setColor(Color.LIGHT_GRAY);
               else
                  g.setColor(Color.GRAY);
               g.fillRect(2 + col*20, 2 + row*20, 20, 20);
               switch (board.pieceAt(row,col)) {
               case CheckersData.RED:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.BLACK:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.RED_KING:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               case CheckersData.BLACK_KING:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               }
            }
         }
         
         /* подсветка норм ходов */      
         
         if (gameInProgress) {
               /* если можно двигать - обрисовуем */
            g.setColor(Color.cyan);
            for (int i = 0; i < legalMoves.length; i++) {
               g.drawRect(2 + legalMoves[i].fromCol*20, 2 + legalMoves[i].fromRow*20, 19, 19);
               g.drawRect(3 + legalMoves[i].fromCol*20, 3 + legalMoves[i].fromRow*20, 17, 17);
            }
               /* если норм ход - то выделяем его */
            if (selectedRow >= 0) {
               g.setColor(Color.white);
               g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
               g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
               g.setColor(Color.green);
               for (int i = 0; i < legalMoves.length; i++) {
                  if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                     g.drawRect(2 + legalMoves[i].toCol*20, 2 + legalMoves[i].toRow*20, 19, 19);
                     g.drawRect(3 + legalMoves[i].toCol*20, 3 + legalMoves[i].toRow*20, 17, 17);
                  }
               }
            }
         }
 
      }  // end paintComponent()
      
      
      /**
       * обработка нажатия на кнопку новая игра
       */
      public void mousePressed(MouseEvent evt) {
         if (gameInProgress == false)
            message.setText("Нажми \"Новая игра\" чтоб начать");
         else {
            int col = (evt.getX() - 2) / 20;
            int row = (evt.getY() - 2) / 20;
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
               doClickSquare(row,col);
         }
      }
      
      
      public void mouseReleased(MouseEvent evt) { }
      public void mouseClicked(MouseEvent evt) { }
      public void mouseEntered(MouseEvent evt) { }
      public void mouseExited(MouseEvent evt) { }
      
      
   }  // конец обработки доски
   
   
   
   /**
    * тут храним всю логику игры
    */
   private static class CheckersData {
      
      /*  типы наших шашек */
 
      static final int
                EMPTY = 0,
                RED = 1,
                RED_KING = 2,
                BLACK = 3,
                BLACK_KING = 4;
      
      
      int[][] board;  // таблица  
      
      
      /**
       * шаблончеГГ для доски
       */
      CheckersData() {
         board = new int[8][8];
         setUpGame();
      }
      
      
      /**
       * расставляем шашки
       */
      void setUpGame() {
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if ( row % 2 == col % 2 ) {
                  if (row < 3)
                     board[row][col] = BLACK;
                  else if (row > 4)
                     board[row][col] = RED;
                  else
                     board[row][col] = EMPTY;
               }
               else {
                  board[row][col] = EMPTY;
               }
            }
         }
      }  // end setUpGame()
      
      
      /**
       * показываем
       */
      int pieceAt(int row, int col) {
         return board[row][col];
      }
            
      
      /**
       * делаем особых ход
       */
      void makeMove(CheckersMove move) {
         makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
      }
      
      
      /**
       * собственно говоря логика хода самого
       */
      void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
         board[toRow][toCol] = board[fromRow][fromCol];
         board[fromRow][fromCol] = EMPTY;
         if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            // убираем там где была шашка
            int jumpRow = (fromRow + toRow) / 2;  // переход ряда
            int jumpCol = (fromCol + toCol) / 2;  // переход колоны
            board[jumpRow][jumpCol] = EMPTY;
         }
         if (toRow == 0 && board[toRow][toCol] == RED)
            board[toRow][toCol] = RED_KING;
         if (toRow == 7 && board[toRow][toCol] == BLACK)
            board[toRow][toCol] = BLACK_KING;
      }
      
      /**
       * проверка и обработка ходов и крутых шашек 
       */
      CheckersMove[] getLegalMoves(int player) {
         
         if (player != RED && player != BLACK)
            return null;
         
         int playerKing;  // у кого крутая шашка
         if (player == RED)
            playerKing = RED_KING;
         else
            playerKing = BLACK_KING;
         
         ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // храним тут ходы
         
         /*  пук пук пук проверка ходов ля ля ля убейте меня
          */
         
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if (board[row][col] == player || board[row][col] == playerKing) {
                  if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                     moves.add(new CheckersMove(row, col, row+2, col+2));
                  if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                     moves.add(new CheckersMove(row, col, row-2, col+2));
                  if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                     moves.add(new CheckersMove(row, col, row+2, col-2));
                  if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                     moves.add(new CheckersMove(row, col, row-2, col-2));
               }
            }
         }
         
         /*  если можно - ходи, если нельзя то мы проверяем можно ли вообще
          */
         
         if (moves.size() == 0) {
            for (int row = 0; row < 8; row++) {
               for (int col = 0; col < 8; col++) {
                  if (board[row][col] == player || board[row][col] == playerKing) {
                     if (canMove(player,row,col,row+1,col+1))
                        moves.add(new CheckersMove(row,col,row+1,col+1));
                     if (canMove(player,row,col,row-1,col+1))
                        moves.add(new CheckersMove(row,col,row-1,col+1));
                     if (canMove(player,row,col,row+1,col-1))
                        moves.add(new CheckersMove(row,col,row+1,col-1));
                     if (canMove(player,row,col,row-1,col-1))
                        moves.add(new CheckersMove(row,col,row-1,col-1));
                  }
               }
            }
         }
         
         /* если и ходить нельзя - то все, конечная - слил ихих */
         
         if (moves.size() == 0)
            return null;
         else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
               moveArray[i] = moves.get(i);
            return moveArray;
         }
         
      }  // end getLegalMoves
      
      
      /**
       * возвращаем список норм ходов
       */
      CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
         if (player != RED && player != BLACK)
            return null;
         int playerKing;  // у кого король???
         if (player == RED)
            playerKing = RED_KING;
         else
            playerKing = BLACK_KING;
         ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // ходааа ууу еее
         if (board[row][col] == player || board[row][col] == playerKing) {
            if (canJump(player, row, col, row+1, col+1, row+2, col+2))
               moves.add(new CheckersMove(row, col, row+2, col+2));
            if (canJump(player, row, col, row-1, col+1, row-2, col+2))
               moves.add(new CheckersMove(row, col, row-2, col+2));
            if (canJump(player, row, col, row+1, col-1, row+2, col-2))
               moves.add(new CheckersMove(row, col, row+2, col-2));
            if (canJump(player, row, col, row-1, col-1, row-2, col-2))
               moves.add(new CheckersMove(row, col, row-2, col-2));
         }
         if (moves.size() == 0)
            return null;
         else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
               moveArray[i] = moves.get(i);
            return moveArray;
         }
      }  // end getLegalMovesFrom()
      
      
      /**
       * проверяем можно-ли так вообще походить или нет 
       */
      private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
         
         if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false;  // за доской 
         
         if (board[r3][c3] != EMPTY)
            return false;  // там уже шашка
         
         if (player == RED) {
            if (board[r1][c1] == RED && r3 > r1)
               return false;  // красные только вверх
            if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
               return false;  // черные не ходят 
            return true;  // ходить нельзя дупка момент
         }
         else {
            if (board[r1][c1] == BLACK && r3 < r1)
               return false;  // черные только вниз
            if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
               return false;  // красные не ходят
            return true;  // ходить нельзя
         }
         
      }  // end canJump()
      
      
      /**
       * ну опять же провека можно ли так делать 
       */
      private boolean canMove(int player, int r1, int c1, int r2, int c2) {
         
         if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false;  // за доской
         
         if (board[r2][c2] != EMPTY)
            return false;  // уже есть
         
         if (player == RED) {
            if (board[r1][c1] == RED && r2 > r1)
               return false;  // красные только вниз
            return true;  // ход нельзя
         }
         else {
            if (board[r1][c1] == BLACK && r2 < r1)
               return false;  // черные только вверх
            return true;  // ход нельзя
         }
         
      }  // конец проверки
      
      
   } // конец логики
   
   
} // конец шашек
