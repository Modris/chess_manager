# Chess_Manger. A Puzzle piece for SlyChess project.

<div align="center">
  <img src="/project_images/chess_manager_highlight.png" alt="oidc">
</div>

* Responsible for WebSocket with Stomp and RabbitMQ message broker configuration.

* Delegates Stockfish engine best move to Chess_Backend by sending the user payload to a RabbitMQ Exchange called "main_exchange". This exchange
is bound to a queue called "direct_bestmove" which the Chess_Backend listens to and returns the best move to the user subscription endpoint.

* Responsible for Saving Chess games into MySQL database with Spring Data JPA.

* Protected endpoints which require authentication. It's an Oauth2 Resource Server. It requires Authorization Header with a Bearer token for verification.

* Client payload and especially FEN Validation. FEN is required so the Stockfish engine knows the board situation and can calculate the best move accordingly.

* Custom Heartbeats (Ping Pong) to keep WebSocket connection alive. Why Custom? Because the WebStomp Client library used on the frontend didn't allow to listen to the heartbeats. WebSocket.close() can range up to 15 minutes if somebody pulls the cord but i wanted the reconnectivity to happen within 8 seconds instead.

# Main Dependencies

* Oauth2 Resource Server.

* Spring Boot Starter AMQP

* Spring Boot Starter WebSocket

* Spring Data JPA + MySQL Connector
  
# Why WebSockets?

* Enables real time communication between the Server and The Client. Although the best chess move response time is limited by the response time of Chess_Backend.

* Personally i wanted to know why WebSockets are used in the real world. What are the benefits and downsides to using them. And since most ideas were already executed like
  a chatbot or stock market i decided a low latency in a chess game against computer would be a nice scenario for WebSockets.

* Simple Text Oriented Messaging Protocol (STOMP) is used as the messaging protocol so i don't have to invent a way which messages are valid or invalid in the frontend or server side.

# Why RabbitMQ as the message broker?

* Persistent messages. This means that even if the broker or the system experiences failures, the messages are stored on disk and can be recovered when the system is back up. 

*   Durability, Reliability and Message Acknowledgements. Unacknowledged messages won't be lost but will still be in the queue.

*   So i can delegate the best move calculation to another service (Chess_Backend) by sending the message to a queue that the Chess_Backend listens to. I knew from the beginning that it will take around 100-200 ms calculation per move that it would be a weak point in the system in terms of scaling. I can solve this easily by just adding new Chess_Backend containers since they will be listening to the same queue.

# @RestController GameHistoryController endpoints


| Endpoint                    | HTTP Method | Request Body | Status | Response Body | Description                                         |
| --------------------------- | ----------- | ------------ | ------ | -------------- | --------------------------------------------------- |
| /save               | POST         | @Valid SaveGame, Authentication      | 400    |                | If SaveGame is not valid then 400 Bad Request is returned.                                                          |
| /save   | POST         | @Valid SaveGame, Authentication       | 201   |                | For successful saving operation into database.            |
| /save   | POST         | @Valid SaveGame, Authentication      | 401    |                | Unauthenticated users can't access this endpoint.            |
| /statistics                      | GET         |  Authentication            | 200    | Statistics     | Fetches all game statistics for specific username (wins, losses, draws).             |
| /statistics                      | GET         |    Authentication          | 204    |   Statistics           | No statistics found for such username.  Returns 204 and "X-SERVER-RESPONSE" Header.                              
| /statistics               | GET         | Authentication         | 401    | Statistics           | Unauthenticated users can't access this endpoint |
| /get/game/{id}   | GET         | Path variable id      | 200    |     SaveGame           | Fetches a game with specified ID.           |
| /get/game/{id}   | GET         | Path variable id      | 204    |     SaveGame          | No Game with such Path ID is found. Returns 204 and "X-SERVER-RESPONSE" Header.           |
| /getGameHistory                | GET         | page, pageSize, Authentication          | 200    |    Page< SaveGame >           | We return paged response sorted by ID Descending.                 |
| /getGameHistory              | GET         | page, pageSize, Authentication       | 204    |          | No history found for such username. Returns 204 and "X-SERVER-RESPONSE" Header.      |
| /getGameHistory              | GET         | page, pageSize, Authentication       | 401    |         | Unauthenticated users can't access this endpoint.      |

# @RestController MessageController endpoints 


| Endpoint                    | WebSocket  | Request Body  | Response Body | Description                                         |
| --------------------------- | ----------- | ------------ |  -------------- | --------------------------------------------------- |
| /app/websocket               | ✅         |   @Valid ClientObject  |                 | If Fen is invalid we return error message to /topic/bestmove+${userId} subscription endpoint.                                                          |
| /topic/bestmove+${userId}   | Subscription         | Random UUID userID       | 4 chars with best move e.g. "a4a8"   |               For now frontend must guarantee each userID is UUID compliant. In the future perhaps we'll have a user with custom username and password with limited permissions in RabbitMQ.           |
| /topic/bestmove+${userId}    | Subscription         | ClientObject Fen with "Ping"      | "Pong"                   | A Fen with "Ping" will instantly return a "Pong".          |

# @RestControllerAdvice

I decided To use 204 No Content if something is empty because the request succeeded so it must be a 2XX HTTP Call. However some developers would use 200 OK with empty body. My API returns 204 No Content because that's exactly what happened - the request succeeded but it has no content.

* Empty GET /game/with/{id} will throw GameNotFoundException which returns 204 Content and "X-SERVER-RESPONSE" Header with String: "Game with game id: `"+gameID+"` not found.".

* Empty GET /Statistics will throw StatisticsNotFoundException which returns 204 Content and "X-SERVER-RESPONSE" Header with String: "Statistics for user id: `"+username_id+"` not found.".

* GET /getGameHistory with Empty History will throw HistoryIsEmptyException which returns 204 No Content and "X-SERVER-RESPONSE" Header with String: "History for user id `"+username_id+"` is empty.".

* Invalid SaveGame input from POST /save will throw MethodArgumentNotValidException which returns 400 Bad Request and error message for the invalid field(s).

* Invalid FEN from POST /save will throw FenIsInvalidException which returns 400 Bad Request and error message for the invalid field.


# Oauth2 Resource Server

* We have stateless session policy because we have @RestControllers.

* We disable CSRF because of stateless session management.

* We have Method Security which only authenticated users with Authorization header and correct Bearer Token can access.

* For protected endpoints which are accessed unathenticated we will return 401 unauthorized instead of 302 redirect.
  
# Payload Validation and FenValidator + open issue in regards to Fen Validation.

Spring Boot Starter Validation allows us to Validate inputs. This was my first time using it. It throws MethodArgumentNotValidException which @RestControllerAdvice class catches and throws the appropriate 400 bad request response.

FEN is the abbreviation of Forsyth-Edwards Notation, and it is the standard notation to describe positions of a chess game. And it's required for the Stockfish engine to calculate the best move. However the Stockfish engine will crash the program upon incorrect FEN. So i had to write FenValidation logic and Tests. However i can only test the syntax and upon doing research i found this is quite the problem in chess community that is somewhat overlooked by other Chess Game implementation libraries like pychess. The problem is that in chess if an opponent is in "check" then he has to move out of the way or prevent from being "checked" by the enemy. However in a FEN String the opponent could be in "check" and the enemy could have the current turn instead of the "checked" oppponent. That is an impossible situation in a Chess Game hence the invalid Fen.

 We can do this by simply swapping a single letter in the FEN. e.g: "rnbqkbnr/pppp1ppp/8/8/8/4Q3/PPPPPPPP/RNB1KBNR **w** KQkq - 0 1." [Here is the position in Lichess Board Editor](https://lichess.org/editor/rnbqkbnr/pppp1ppp/8/8/8/4Q3/PPPPPPPP/RNB1KBNR_w_KQkq_-_0_1?color=white). Black is in Check but White has the move. It's an invalid FEN. But i can't check that with only a syntax validation. I would need a Chess Board application or API, set the FEN position, swap colors and ask the Chess Board if i am in check. I decided not to implement this as it's quite unlikely a bad actor would send such a FEN to my website and it's a extra resource consumption overhead for an edge cases that's quite unlikely to happen however it's a weakness nontheless. 

# End
