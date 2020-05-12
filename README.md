# BattleShipsOnline :anchor:

## Game rules:
  
-	The game is played by two player.
-	Each player has a playground, which is consisted of 10 by 10 cells. The rows are marked with letters from A to J and the collumns are marked with numbers from 1 to 10.
-	Each player has on his field:
    -	1 ship consisting of 5 cells;
    -	2 ship consisting of 4 cells;
    -	3 ship consisting of 3 cells;
    -	4 ship consisting of 2 cells;
-	At the beginning of the game, each player places his ships on the field, and they can only be in a straight line (horizontally or vertically)
-	The goal of each player is to hit his opponent's ships, as the players take turns and each has one action per turn.
-	The game ends when one of the players runs out of ships.


## Game Server

Да се създаде Game Server със следните функционалности:

-	Създаване на игра
-	Извеждане на списък с всички игри, активни в момента, с информация дали играта е започнала и броя на играчите в нея.
-	Присъединяване към вече създадена игра (всяка игра трябва да има уникален идентификатор), ако има свободно място.
-	Присъединяване към случайна игра, в която има място.
-	Запазване на състоянието на играта, в която сме в момента.
-	Извеждане на всички запазени игри, в които сме участвали.
-	Възстановяване на запазена игра и присъединяване към нея.
-	Изтриване на запазена игра.

## Game Client

Да се създаде клиент за сървъра, който има конзолен интерфейс.

Примерен интерфейс

-	Създаване на игра

```bash
$ java run-client.java --username gosho

# Извеждане на възможните команди
Available commands:
	create-game <game-name>
	join-game [<game-name>] // ако липсва името, присъединяване към случайна игра.
	saved-games
	load-game <game-name>
	delete-game
	...

menu> create-game my-game
Created game "my-game", players 1/2
```

-	Присъединяване към игра

```bash
$ java run-client.java --username tosho

# извеждане на възможните команди

menu> list-games
| NAME     | CREATOR | STATUS      | PLAYERS |
|----------+---------+-------------+---------|
| my-game  | pesho   | pending     | 1/2     |
| my-game-2| gosho   | in progress | 2/2     |

menu> join-game my-game
Joined game "my-game"
PLAYERS: 2/2, type "start" to start the game
```
-	Въвеждане на ход

```bash

       YOUR BOARD
   1 2 3 4 5 6 7 8 9 10
   _ _ _ _ _ _ _ _ _ _
A |_|*|_|-|_|_|_|*|*|_|                         Legend:
B |_|*|_|_|_|_|_|_|_|_|				* - ship field
C |_|*|_|_|_|_|_|_|_|_|				X - hit ship field
D |_|X|_|_|*|*|*|_|-|_|				О - hit empty field
E |_|_|_|_|_|_|_|_|_|_|
F |_|_|-|_|_|_|-|_|_|_|
G |_|_|_|_|_|_|_|_|_|_|
H |_|*|_|_|_|X|X|X|X|_|
I |_|*|_|_|_|_|_|_|_|_|
J |_|*|_|_|_|_|_|_|_|_|


      ENEMY BOARD
   1 2 3 4 5 6 7 8 9 10
   _ _ _ _ _ _ _ _ _ _
A |_|_|_|_|_|_|_|_|_|_|
B |_|_|_|_|_|_|_|_|-|_|
C |_|-|_|_|_|_|-|_|_|_|
D |_|_|_|_|_|_|_|_|_|_|
E |_|_|_|-|_|_|_|_|-|_|
F |_|_|_|_|_|_|_|_|_|_|
G |_|_|_|_|_|_|_|X|_|_|
H |_|_|X|X|X|_|_|X|_|_|
I |_|_|_|_|_|_|_|X|_|_|
J |_|_|_|_|_|_|_|X|_|_|

gosho's last turn: D9
Enter your turn:
```
