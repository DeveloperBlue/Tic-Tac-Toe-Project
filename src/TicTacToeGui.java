import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TicTacToeGui extends Application {
	
	private static Board board = new Board();;
	
	private static GamePiece currentPlayer = GamePiece.X;
	
	private static GamePiece playerPiece;
	private static GamePiece cpuPiece;
	
	private static ComputerPlayer cpu = new ComputerPlayer(board);
	
	private static int pScore = 0;
	private static int cScore = 0;
	
	private static Scanner scanner = new Scanner(System.in);
	private static boolean isQuit = false;
	
	public static Pane boardPane;
	
	public void start(Stage primaryStage){
		
		BorderPane mainPane = new BorderPane();
		
		HBox titleBar = new HBox();
		titleBar.setAlignment(Pos.CENTER);
		titleBar.setPadding(new Insets(10,10,10,10));
		titleBar.setStyle("-fx-background-color: #3399ff");
		mainPane.setTop(titleBar);
		
		Text turnDisplay = new Text();
		turnDisplay.setText("Turn: Player NULL");
		turnDisplay.setStyle("-fx-font: 26 Arial");
		titleBar.getChildren().add(turnDisplay);
		
		VBox sideBar = new VBox();
		sideBar.setAlignment(Pos.CENTER_LEFT);
		sideBar.setStyle("-fx-background-color: #ffffff");
		sideBar.setPadding(new Insets(10, 10, 10, 10));
		sideBar.setPrefWidth(100);
		mainPane.setLeft(sideBar);
		
		Text pScoreDisplay = new Text();
		pScoreDisplay.setText("Player: NULL");
		sideBar.getChildren().add(pScoreDisplay);
		
		Text cScoreDisplay = new Text();
		cScoreDisplay.setText("CPU: NULL");
		sideBar.getChildren().add(cScoreDisplay);
		
		boardPane = new Pane();
		boardPane.setPadding(new Insets(20, 20, 20, 20));
		boardPane.setStyle("-fx-background-color: #eeeeee");
		mainPane.setCenter(boardPane);
		
		Image boardImage = new Image("http://www.myfreetextures.com/wp-content/uploads/2014/10/seamless-wood-planks-5.jpg");
		ImageView boardDisplay = new ImageView(boardImage);
		boardDisplay.setFitWidth(520);
		boardDisplay.setPreserveRatio(true);
		boardPane.getChildren().add(boardDisplay);
		
		Line c1 = new Line();
		c1.setStartX(190);
		c1.setStartY(40);
		c1.setEndX(190);
		c1.setEndY(400);
		c1.setStrokeWidth(3);
		boardPane.getChildren().add(c1);
		
		Line c2 = new Line();
		c2.setStartX(310);
		c2.setStartY(40);
		c2.setEndX(310);
		c2.setEndY(400);
		c2.setStrokeWidth(3);
		boardPane.getChildren().add(c2);
		
		Line r1 = new Line();
		r1.setStartX(70);
		r1.setStartY(160);
		r1.setEndX(430);
		r1.setEndY(160);
		r1.setStrokeWidth(3);
		boardPane.getChildren().add(r1);
		
		Line r2 = new Line();
		r2.setStartX(70);
		r2.setStartY(280);
		r2.setEndX(430);
		r2.setEndY(280);
		r2.setStrokeWidth(3);
		boardPane.getChildren().add(r2);
		
		ImageView[][] pieceDisplays = new ImageView[3][3];
		
		for (int row = 0; row < 3; row++){
			
			for (int column = 0; column < 3; column++){
				
				Image piece = new Image("https://i.imgur.com/nHQW9VY.jpg");
				ImageView pieceDisplay = new ImageView(piece);
				pieceDisplay.setFitWidth(100);
				pieceDisplay.setFitHeight(100);
				pieceDisplay.setX((row * 120) + 80);
				pieceDisplay.setY((column * 120) + 50);
				pieceDisplays[row][column] = pieceDisplay;
				boardPane.getChildren().add(pieceDisplay);
				
			}
		}
		

		Scene scene = new Scene(mainPane, 600, 500);

		primaryStage.setTitle("Tic-Tac-Toe!");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	

	public static void setPieceDisplay(int row, int column, GamePiece piece){

		Image pieceImage = new Image((piece == GamePiece.X) ? "https://i.imgur.com/AR4koh1.png" : "https://i.imgur.com/AR4koh1.png");
		ImageView pieceDisplay = new ImageView(pieceImage);
		pieceDisplay.setFitWidth(100);
		pieceDisplay.setPreserveRatio(true);
		
		boardPane.getChildren().add(pieceDisplay);
	}
	
	public static void main(String[] args){
		
		launch();
		
		while (isQuit == false){
			
			playGame();
			
			System.out.println("==================================================");
			System.out.println("Would you like to play again? Type 'quit' to quit.");
			
			String response = scanner.nextLine();
			
			if (response.toUpperCase() == "QUIT"){
				isQuit = true;
			}
		}
		
		System.out.println("Thanks for playing!");
		
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
					
					// Board.setWinner(playerPiece);
					
					break;
				}
				
			}

			Board.placePiece(move[0], move[1], currentPlayer);
			setPieceDisplay(move[0], move[1], currentPlayer);
		
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
			
		}
			
	}
	
	public static void flipPlayer(){
		currentPlayer = (currentPlayer == GamePiece.X) ? GamePiece.O : GamePiece.X;
		System.out.println("Current Turn: [" + currentPlayer + "]");
	}

}