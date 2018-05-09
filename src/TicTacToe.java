import java.util.Random;
import java.util.Scanner;


public class TicTacToe {
	
	private static Board board = new Board();;
	
	private static GamePiece currentPlayer = GamePiece.X;
	
	private static GamePiece playerPiece;
	private static GamePiece cpuPiece;
	
	private static ComputerPlayer cpu = new ComputerPlayer(board);
	
	private static int pScore = 0;
	private static int cScore = 0;
	
	private static Scanner scanner = new Scanner(System.in);
	private static boolean isQuit = false;
	
	public static void main(String[] args){
		
		while (isQuit == false){

			// X always goes first
			currentPlayer = GamePiece.X;

			// Randomly assign game pieces
			playerPiece = (new Random().nextInt(2) == 1) ? GamePiece.O : GamePiece.X;
			cpuPiece = (playerPiece == GamePiece.X) ? GamePiece.O : GamePiece.X;

			// Initalize the game
			board.setGameState(GameState.PLAYING);
			board.clearGameArray();

			// Launch the game
			
			playGame();
			
			// Want to go again?

			System.out.println("==================================================");
			System.out.println("Would you like to play again? Type 'quit' to quit.");
			
			String response = scanner.nextLine();
			
			if (response.toUpperCase().equals("QUIT")){
				// Let's get out of here
				isQuit = true;
			}
		}
		
		System.out.println("==================================================");
		System.out.println("Thanks for playing!");
		System.out.println("Final Score:");
		System.out.println("Player: " + pScore + " | CPU: " + cScore);

	}
	
	public static void playGame(){
		// New Game
		playerPiece = GamePiece.X;
		cpuPiece = (playerPiece == GamePiece.X) ? GamePiece.O : GamePiece.X;
		
		cpu.setPlayerType(cpuPiece);
		
		while (Board.getGameState() == GameState.PLAYING){
			
			int[] move = null;
			
			if (currentPlayer == playerPiece){
				
				// Prompt user for a move
				while (move == null){
					System.out.println("Please enter <row> <column> to make a move (Ex. '0 2')");
					
					String userMove = scanner.nextLine();
					String[] userMoveStringArray = userMove.split(" ");
					
					try {
						
						if (userMoveStringArray.length < 2){
							throw new IllegalMoveException("Sorry, " + userMove + " is not a valid move.");
						}
						
						int move_row = Integer.parseInt(userMoveStringArray[0].trim());
						int move_column = Integer.parseInt(userMoveStringArray[1].trim());

						if ((move_row > 2) || (move_column > 2) || (move_row < 0) || (move_column < 0)){
							throw new IllegalMoveException("Rows and columns must be between 0 and 2 inclusively.");
						}
						
						if (Board.checkPlace(move_row, move_column) == false){
							throw new IllegalMoveException("That place is already taken!");
						}

						move = new int[2];
						move[0] = move_row;
						move[1] = move_column;
						
					} catch (IllegalMoveException e){
						System.out.println(e.getMessage());
					} catch (NumberFormatException e){
						System.out.println("Sorry, '" + userMove + "' is not a legal move.");
					}
					
				}
				
			} else if (currentPlayer == cpuPiece){
				
				move = cpu.generateMove();
				
				if (move == null){
					
					System.out.println("The CPU has run out of valid moves! The Player has won by default!");
					
					Board.setGameState((currentPlayer == GamePiece.X) ? GameState.X_WON : GameState.O_WON);
					
					break;
				}
				
			}

			Board.placePiece(move[0], move[1], currentPlayer);
			
			Board.printGame();
			System.out.println("=================================");
			
			Board.calculateGameState();
			
			if (Board.getGameState() == GameState.PLAYING){
				flipPlayer();
			}
			
		}
		
		if (Board.getGameState() == GameState.DRAW){
			
			System.out.println("The game has ended in a draw!");
			
		} else {
			
			GamePiece winner = (Board.getGameState() == GameState.X_WON) ? GamePiece.X : GamePiece.O;
			
			if (winner == cpuPiece){
				cScore++;
				System.out.println("The CPU has won the game!");
				System.out.println('"' + cpu.getRandomTaunt() + '"' + " - CPU");
			} else {
				System.out.println("You won the game!");
				pScore++;
			}
			System.out.println();
			System.out.println("SCORE:");
			System.out.println("Player: " + pScore + " | CPU: " + cScore);
			System.out.println();
			
		}
			
	}
	
	public static void flipPlayer(){
		currentPlayer = (currentPlayer == GamePiece.X) ? GamePiece.O : GamePiece.X;
		System.out.println("Current Turn: [" + currentPlayer + "]");
	}
	

}