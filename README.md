# Checkers
This is a small Checkers project for two players.
## Table of contents
* [General info](#general-info)
* [Gameplay](#gameplay)
* [Technologies and features](#technologies-and-features)
* [Setup](#setup)

## General info
This is my little Checkers game for two people, unfortunately not online :(
I used the Swing library in java and the usual methods like arrays, classes and the like. The beauty of this program is that there is nothing specific here, everything is quite simple and thanks to that we got a short but working code with GUI!

Let's move on to what I think are the interesting points!

First, a function that checks who is walking and whether he can walk.
```
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
```
The point is that we use an additional table where we store our board with the specified possible moves!

Since in Checkers you have to hit for as long as you hit - I handle that in this function! As for me - this is a very interesting and simple solution to such a problem :)
```
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
```

For convenience and intuitive understanding, I added highlighting of possible moves, which will definitely be used in future projects!
```
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
```

You can see the rest directly in the code!

## Gameplay

https://github.com/Andezion/Checkers/assets/115638748/a7387738-24bb-46a4-b59a-829493d5b32d

## Technologies and features
Project is created with:
* java.awt a set of tools for creating graphical user interfaces (GUIs) in Java. The library provides classes for creating windows, panels, buttons, text fields, and other user interface components.
* java.awt.event package contains classes and interfaces for handling events such as keystrokes, mouse clicks, focus changes, etc. It is used to create interactive graphical applications.
* javax.swing a library that extends AWT capabilities by providing more complex and flexible GUI components such as buttons, text fields, panels, tables, trees, etc. Swing components are lightweight and platform independent, allowing you to create homogeneous interfaces on different operating systems.
* java.util.ArrayList a collection that provides a variable-length array that can be dynamically resized. It is used to store ordered collections of objects and allows you to add, remove and retrieve items by index.
* Professor Nazdrovich's W rizz

## Setup
Download my latest release if you're watching from Windows, or if you're using linux-like systems - then copy the Checkers.java file into your file and paste: 
```
javac Checkers.java
java Checkers
```
