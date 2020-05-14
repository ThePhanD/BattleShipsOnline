# BattleShipsOnline :anchor:

## Game rules:
  
-	The game is played by two player.
-	Each player has a playground, which is consisted of 10 by 10 cells. The rows are marked with letters from A to J and the collumns are marked with numbers from 1 to 10.
-	Each player has on his field:
    -	1 ship consisting of 5 cells - Carrier
    -	2 ship consisting of 4 cells - Cruiser
    -	3 ship consisting of 3 cells - Destroyer
    -	4 ship consisting of 2 cells - Submarine
-	At the beginning of the game, each player places his ships on the field, and they can only be in a straight line (horizontally or vertically)
-	The goal of each player is to hit his opponent's ships, as the players take turns and each has one action per turn.
-	The game ends when one of the players runs out of ships.


## Game Server

The game server is started by HubServer from src/online/hub/server/HubServer.
Then every player can connect to the server and start playing.

```bash
Available commands:
	username <name>
	create <game-name>
	list
	list-save
	join [<game-name>]
	load <game-name>
	delete <game-name>

Before start playing each player must set their username.
- Set username
-> username playerOne
The username playerOne is set.


- Create game
-> create-game my-game


- List current game sessions
-> list
| NAME     | CREATOR | STATUS      | PLAYERS |
|----------+---------+-------------+---------|
| my-game  | pesho   | pending     | 1/2     |
| my-game-2| gosho   | in progress | 2/2     |


- List saved games
-> list-save
saveGame, firstGame


- Join random game
-> join


- Join game
-> join my-game


- Load game
-> load my-game


- Delete game
-> delete my-game

```

## Game Client

The game client is started by PlayerClient from src/online/hub/client/PlayerClient.
Then every player can play the game. 

```bash
Available commands:
	attack <position>
	repair <position>
	place <ship-name> <start-position> <end-position>
	move <any-ship-position> <new-start-position> <new-end-position>
	scan <position>
	save - save the file game at that moment
	
	
- Fire at the map
-> attack A1


- Repair a ship part
-> repair A2


- Place a ship on the map
-> place Carrier A1 A5


- Move a ship on new location on the map
-> move A1 B1 B5


- Scan 3x3 fields on the map
-> scan B2
	SCAN BOARD
   1 2 3 4 5 6 7 8 9 10
   _ _ _ _ _ _ _ _ _ _
A |_|*|_|_|_|_|_|_|_|_|
B |_|*|_|_|_|_|_|_|-|_|
C |_|*|_|_|_|_|-|_|_|_|
D |_|_|_|_|_|_|_|_|_|_|
E |_|_|_|-|_|_|_|_|-|_|
F |_|_|_|_|_|_|_|_|_|_|
G |_|_|_|_|_|_|_|X|_|_|
H |_|_|X|X|X|_|_|X|_|_|
I |_|_|_|_|_|_|_|X|_|_|
J |_|_|_|_|_|_|_|X|_|_|


- Save the game file at that moment
-> save

	
Game interface.
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

PlayerOne's last turn: D9
Enter your turn:
```
