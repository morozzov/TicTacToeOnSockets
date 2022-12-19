# TicTacToeOnSockets
Overly simple tic-tac-toe on sockets.

## About
The project consists of two applications: a client and a server.

Once started, the server waits for the two clients to connect. Each of the clients, immediately after start-up, enters its name.
The server then lets the clients take turns to play until one of them wins or if there is a draw.

The move is done by sending the cell position to the client (from 1 to 9).

## Game field
<img width="57" alt="image" src="https://user-images.githubusercontent.com/76492047/208329595-cf934048-340d-4a7d-ac09-1733875964d3.png">
