import java.util.Optional;
import java.util.Random;
import javafx.util.Duration;

import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class TicTacToeGui extends Application {
	
	private static String currentDir = System.getProperty("user.dir");
	
	// Game specific
	private static Board board = new Board();;
	
	private static GamePiece currentPlayer = GamePiece.X;
	
	private static GamePiece playerPiece;
	private static GamePiece cpuPiece;
	
	private static ComputerPlayer cpu = new ComputerPlayer(board);
	
	private static int pScore = 0;
	private static int cScore = 0;
	
	private static String playerHandle = "Player";
	
	// Java FX specific	
	private static Stage pStageReference;
	private static Scene introScene;
	private static Scene gameScene;
	private static Pane boardPane;
	
	private static ScrollPane scrollMoveTracker;
	private static VBox moveTracker;

	private static Text pScoreDisplay = new Text();
	private static Text cScoreDisplay = new Text();
	
	private static Text turnDisplay = new Text();
	private static ImageView humanPlayerIcon;
	private static ImageView cpuPlayerIcon;

	private static String img_Icon_X = "https://i.imgur.com/d1S4Kjq.png";
	private static String img_Icon_O = "https://i.imgur.com/AR4koh1.png";
	
	private static ButtonType buttonTypeOne;
	private static ButtonType buttonTypeTwo;
	private static Alert alert;
	
	private static Button[][] pieceDisplays = new Button[3][3];
	private static int[] holdButtonForMove;
	private static boolean debounce = false;
	
	// Java FX
	public void start(Stage primaryStage){
		
		System.out.println(currentDir);

		/*
			INTRODUCTION SCREEN CREATION
		*/
		Text introText = new Text("Welcome to Tic-Tac-Toe, " + playerHandle + "!");
		introText.setStyle("-fx-font: 36 Gafata;");

		Button introButton = new Button("Play Game");
		Button pHandleButton = new Button("Change Player Handle");
		Button quitApplication = new Button("Quit");
		
		introButton.setMinWidth(240);
		introButton.setMinHeight(50);
		introButton.setStyle("-fx-background-color: #FFF; -fx-border-color: #000");
		appendButtonHoverSequence(introButton, "-fx-background-color:#00ff80", "-fx-background-color: #FFF; -fx-border-color: #000");
		
		pHandleButton.setMinWidth(240);
		pHandleButton.setMinHeight(50);
		pHandleButton.setStyle("-fx-background-color: #FFF; -fx-border-color: #000");
		appendButtonHoverSequence(pHandleButton, "-fx-background-color:#00bfff", "-fx-background-color: #FFF; -fx-border-color: #000");
		
		quitApplication.setMinWidth(240);
		quitApplication.setMinHeight(50);
		quitApplication.setStyle("-fx-background-color: #FFF; -fx-border-color: #000");
		appendButtonHoverSequence(quitApplication, "-fx-background-color:#ff0080", "-fx-background-color: #FFF; -fx-border-color: #000");
		
		VBox introPane = new VBox();
		VBox introPaneInner = new VBox();
		
		String bckurl = "https://i.imgur.com/tg5lUjH.png";
		introPane.setStyle("-fx-background-image: url(" + bckurl + "); -fx-background-repeat: repeat; -fx-background-size: 200, 200; -fx-background-position: center center;");
		
		introPaneInner.setAlignment(Pos.TOP_CENTER);
		introPaneInner.setPadding(new Insets(40,50,50,50));
		introPaneInner.setSpacing(10);
		introPaneInner.getChildren().addAll(introButton, pHandleButton, quitApplication);
		
		introPane.setAlignment(Pos.TOP_CENTER);
		introPane.setPadding(new Insets(60, 50, 50, 50));
		introPane.setSpacing(20);
		introPane.getChildren().addAll(introText, introPaneInner);
		
		/*
			GAME SCREEN CREATION
		*/
		
		BorderPane mainPane = new BorderPane();
		String bckurl3 = "https://i.imgur.com/pU40RKi.png";
		mainPane.setStyle("-fx-background-image: url(" + bckurl3 + "); -fx-background-repeat: repeat; -fx-background-size: 800, 800; -fx-background-position: center center;");
		
		
		// Topbar
		HBox titleBar = new HBox();
		titleBar.setAlignment(Pos.CENTER);
		titleBar.setPadding(new Insets(10,10,10,10));
		titleBar.setPrefHeight(50);
		titleBar.setStyle("-fx-background-color: #3399ff");
		String bckurl2 = "https://image.freepik.com/free-vector/blue-watercolor-texture-background_3785-153.jpg";
		titleBar.setStyle("-fx-background-image: url(" + bckurl2 + "); -fx-background-repeat: repeat; -fx-background-size: 640, 500; -fx-background-position: center center; -fx-border-color: #191970; -fx-border-width: 0 0 2 0;");
		
		mainPane.setTop(titleBar);

		turnDisplay.setText("It's the Player's Turn!");
		turnDisplay.setStyle("-fx-font: 38 Magra; -fx-font-weight: bold;");
		titleBar.getChildren().add(turnDisplay);
		
		// Sidebar
		VBox sideBar = new VBox();
		sideBar.setStyle("-fx-background-color: transparent; -fx-border-color: #CCC; -fx-border-width: 0 1 1 1;");
		sideBar.setPadding(new Insets(10, 0, 10, 0));
		sideBar.setSpacing(10);
		sideBar.setPrefWidth(140);

		// Sidebar -> Player Displays
		VBox playerUnits = new VBox();
		playerUnits.setPadding(new Insets(5, 10, 5, 10));
		playerUnits.setStyle("-fx-background-color: #EEE; -fx-border-color: #CCC; -fx-border-width: 1 0;");
		
		Text playerUnitsTitle = new Text("Players");
		playerUnitsTitle.setStyle("-fx-font-weight: bold");
		playerUnitsTitle.setTextAlignment(TextAlignment.CENTER);
		
		HBox humanPlayerBox = new HBox();
		HBox cpuPlayerBox = new HBox();
		
		Text humanPlayerText = new Text("Player: ");
		Text cpuPlayerText = new Text("CPU: ");
		
		humanPlayerIcon = new ImageView( new Image(img_Icon_X));
		humanPlayerIcon.setFitHeight(20);
		humanPlayerIcon.setFitWidth(20);
		
		cpuPlayerIcon = new ImageView( new Image(img_Icon_O));
		cpuPlayerIcon.setFitHeight(20);
		cpuPlayerIcon.setFitWidth(20);
		
		humanPlayerBox.getChildren().addAll(humanPlayerText, humanPlayerIcon);
		cpuPlayerBox.getChildren().addAll(cpuPlayerText, cpuPlayerIcon);
	
		playerUnits.getChildren().addAll(playerUnitsTitle, humanPlayerBox, cpuPlayerBox);
		
		
		// Sidebar -> Score Displays
		VBox scoreDisplay = new VBox();
		scoreDisplay.setAlignment(Pos.CENTER_LEFT);
		scoreDisplay.setPadding(new Insets(5, 0, 5, 10));
		scoreDisplay.setStyle("-fx-background-color: #EEE; -fx-border-color: #CCC; -fx-border-width: 1 0;");
		
		Text scoreDisplayTitle = new Text("Score");
		scoreDisplayTitle.setStyle("-fx-font-weight: bold");
		scoreDisplayTitle.setTextAlignment(TextAlignment.CENTER);
		
		pScoreDisplay.setText("Player: 0");	
		cScoreDisplay.setText("CPU: 0");
		
		scoreDisplay.getChildren().addAll(scoreDisplayTitle, pScoreDisplay, cScoreDisplay);
		
		// Sidebar -> Move Tracker
		scrollMoveTracker = new ScrollPane();
		scrollMoveTracker.setPrefSize(140,190);
		moveTracker = new VBox();
		moveTracker.setPadding(new Insets(5, 5, 5, 5));
		moveTracker.setStyle("-fx-background-color: transparent; -fx-border-width: 1 0;");
		// moveTracker.setMinHeight(140);
		// moveTracker.setMinWidth(190);
		// moveTracker.setMaxSize(140, 190);
		scrollMoveTracker.setContent(moveTracker);
		scrollMoveTracker.setStyle("-fx-background-color: transparent; -fx-border-color: #CCC; -fx-border-width: 1 0;");
		
		// Sidebar -> Match buttons
		VBox baseButtons = new VBox();
		baseButtons.setSpacing(5);
		
		baseButtons.setAlignment(Pos.BOTTOM_CENTER);
		
		Button restartMatchButton = new Button("Restart");
		Button leaveMatchButton = new Button("Menu");
		
		restartMatchButton.setPrefWidth(130);
		leaveMatchButton.setPrefWidth(130);
		
		restartMatchButton.setStyle("-fx-background-color: #EEE;");
		appendButtonHoverSequence(restartMatchButton, "-fx-background-color:#dae7f3", "-fx-background-color: #EEE;");
		
		leaveMatchButton.setStyle("-fx-background-color: #EEE;");
		appendButtonHoverSequence(leaveMatchButton, "-fx-background-color:#dae7f3", "-fx-background-color: #EEE;");
		
		restartMatchButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("Restarting game");

		    	appendToMoves("New game started!");
		    	playGame();
		    	pStageReference.setScene(gameScene);
		    	pStageReference.show();

		    }
		});
		
		leaveMatchButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	pStageReference.setScene(introScene);
				pStageReference.show();
		    }
		});
		
		baseButtons.getChildren().addAll(restartMatchButton, leaveMatchButton);
		
		// Finish sidebar
		sideBar.getChildren().addAll(playerUnits, scoreDisplay, scrollMoveTracker, baseButtons);
		mainPane.setLeft(sideBar);
		
		// The board
		
		boardPane = new Pane();
		boardPane.setPadding(new Insets(20, 20, 20, 20));
		mainPane.setCenter(boardPane);
		
		Line c1 = new Line();
		c1.setStartX(190);
		c1.setStartY(40);
		c1.setEndX(190);
		c1.setEndY(400);
		c1.setStrokeWidth(3);
		c1.setStroke(Color.web("#BBB"));
		boardPane.getChildren().add(c1);
		
		Line c2 = new Line();
		c2.setStartX(310);
		c2.setStartY(40);
		c2.setEndX(310);
		c2.setEndY(400);
		c2.setStrokeWidth(3);
		c2.setStroke(Color.web("#BBB"));
		boardPane.getChildren().add(c2);
		
		Line r1 = new Line();
		r1.setStartX(70);
		r1.setStartY(160);
		r1.setEndX(430);
		r1.setEndY(160);
		r1.setStrokeWidth(3);
		r1.setStroke(Color.web("#BBB"));
		boardPane.getChildren().add(r1);
		
		Line r2 = new Line();
		r2.setStartX(70);
		r2.setStartY(280);
		r2.setEndX(430);
		r2.setEndY(280);
		r2.setStrokeWidth(3);
		r2.setStroke(Color.web("#BBB"));
		boardPane.getChildren().add(r2);
		
		for (int row = 0; row < 3; row++){
			
			for (int column = 0; column < 3; column++){
				
				Button imgButton = new Button();
				imgButton.setLayoutX((row * 120) + 75);
				imgButton.setLayoutY((column * 120) + 45);
				imgButton.setPrefSize(110, 110);
				imgButton.setStyle("-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;");
				
				imgButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						for (int r = 0; r < 3; r++) {
		    				for (int c = 0; c < 3; c++) {
		    					if (pieceDisplays[r][c] == imgButton) {
									if (Board.checkPlace(r, c)) {
										if (currentPlayer == playerPiece) {
											imgButton.setStyle("fx-background-color:#dae7f3; -fx-padding: 5, 5, 5, 5;");
										} else {
											holdButtonForMove = new int[]{r, c};
										}
										
									} else {
										imgButton.setStyle("-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;");
									}
		    					}
		    				}
						}
					}
				});
				
				imgButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						imgButton.setStyle("-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;"); // 
						holdButtonForMove = null;
					}
				});

				pieceDisplays[row][column] = imgButton;
				boardPane.getChildren().add(imgButton);	
				
			}
		}

		introScene = new Scene(introPane, 640, 500);
		gameScene = new Scene(mainPane, 640, 500);
		
		introScene.getStylesheets().add("https://fonts.googleapis.com/css?family=Gafata");
		gameScene.getStylesheets().add("https://fonts.googleapis.com/css?family=Magra");

		primaryStage.setTitle("Tic-Tac-Toe!");
		primaryStage.setScene(introScene);
		primaryStage.show();
		
		pStageReference = primaryStage;

		introButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.out.println("Starting game");
		    	appendToMoves("Welcome, " + playerHandle);
		    	playGame();
		    	primaryStage.setScene(gameScene);
		    	primaryStage.show();
		    }
		});
		

		TextInputDialog dialog = new TextInputDialog(playerHandle);
		dialog.setTitle("Player Handle Dialog");
		dialog.setHeaderText("Hey! What's your name?");
		dialog.setContentText("Player Tag:");
		
		pHandleButton.setOnAction(e-> {
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    System.out.println("Your name: " + result.get());
			    playerHandle = result.get();
			    introText.setText("Welcome to Tic-Tac-Toe, " + playerHandle + "!");
			}
		});
		
		quitApplication.setOnAction(e -> Platform.exit());

		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Game Dialog");
		alert.setHeaderText("An event has occured . . .");
		alert.setContentText("Make a decision.");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
		
		buttonTypeOne = new ButtonType("Play Again");
		buttonTypeTwo = new ButtonType("Back to Menu");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		
	}
	
	// Application launch
	public static void main(String[] args){
		launch();
	}
	
	// Game handler
	public static void playGame(){
		// New Game
		
		// Randomly assign game pieces
		playerPiece = (new Random().nextInt(2) == 1) ? GamePiece.X : GamePiece.O;
		cpuPiece = (playerPiece == GamePiece.X) ? GamePiece.O : GamePiece.X;
		
		cpu.setPlayerType(cpuPiece);
		
		humanPlayerIcon.setImage(new Image((playerPiece == GamePiece.X) ? img_Icon_X : img_Icon_O));
		cpuPlayerIcon.setImage(new Image((cpuPiece == GamePiece.X) ? img_Icon_X : img_Icon_O));

		// X always goes first
		currentPlayer = GamePiece.X;

		// Initalize the game
		Board.setGameState(GameState.PLAYING);
		Board.clearGameArray();
		
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				Button btn = pieceDisplays[row][column];
				setButtonImage(btn , "");
			}
		}
		
		// If the CPU has to go first . . .
		if (currentPlayer == cpuPiece) {
        	
        	System.out.println("CPU Moves First");
        	
            int[] move = cpu.generateMove();
			
			if (move == null){
				
				System.out.println("The CPU has run out of valid moves! The Player has won by default!");

			} else {
				
				Timeline fWait = artificialWait(800);
				fWait.setOnFinished(f0 -> {
					
					Board.placePiece(move[0], move[1], currentPlayer);
					Timeline a0 = setPieceDisplay(move[0], move[1], currentPlayer);
					a0.play();
					
					Board.calculateGameState();
					
					System.out.println("CPU making move . . .");
					
					if (Board.getGameState() == GameState.PLAYING){
						flipPlayer();
					}
					
				});
				fWait.play();
	
			}
        }
		
		// Game button click events
		for (int row = 0; row < 3; row++){
			
			for (int column = 0; column < 3; column++){
					
				Button btn = pieceDisplays[row][column];
				
				btn.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	
				    	if (debounce == true) {
				    		return;
				    	}
				    	
				    	debounce = true;
				        
				    	if (Board.getGameState() == GameState.PLAYING) {
				    		
				    		int[] move = null;
				    		
				    		if (currentPlayer == playerPiece) {
				    			
				    			for (int r = 0; r < 3; r++) {
				    				for (int c = 0; c < 3; c++) {
				    					if (pieceDisplays[r][c] == btn) {
				    						
				    						try {
				    							
					    						if (Board.checkPlace(r, c)) {
					    							
					    							System.out.println("MAKING MOVE");
					    							
					    							move = new int[2];
									    			move[0] = r;
									    			move[1] = c;
									    			
									    			Board.placePiece(move[0], move[1], currentPlayer);
									    			
									    			btn.setStyle("-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;");
													
									    			Timeline a1 = setPieceDisplay(move[0], move[1], currentPlayer);
									    			a1.setOnFinished(l1 -> {
									    				
														Board.calculateGameState();
														
														if (Board.getGameState() == GameState.PLAYING){
															flipPlayer();
															
															if (currentPlayer == cpuPiece) {
													        	
													        	System.out.println("CPU's Move");
													        	
													            int[] cpuMove = cpu.generateMove();
																
																if (cpuMove == null){
																	
																	System.out.println("The CPU has run out of valid moves! The Player has won by default!");

																} else {
																	
																	Timeline fWait = artificialWait(700);
																	fWait.setOnFinished(f0 -> {
																		
																		Board.placePiece(cpuMove[0], cpuMove[1], currentPlayer);
																		Timeline a2 = setPieceDisplay(cpuMove[0], cpuMove[1], currentPlayer);
																		a2.play();
																		
																		Board.calculateGameState();
																		
																		System.out.println("CPU making move . . .");
																		
																		if (Board.getGameState() == GameState.PLAYING){
																			flipPlayer();
																		} else {
																			finishGame();
																		}
																		
																	});
																
																	fWait.play();
																	
																}
													        }
														} else {
															finishGame();
														}
														
									    			});
									    			a1.play();

					    						} else {
					    							throw new IllegalMoveException("Sorry, that move is unavailable");
					    						}
					    						
				    						} catch (IllegalMoveException err) {
				    							System.out.println(err.getMessage());
				    						}
				    						
				    						
				    					}
				    				}
				    			}

				    		}
				    		
				    	}
				    	
				    	debounce = false;
				    	
				    }
				});
				
			}
		}
		
	}
	
	public static void finishGame() {
		if (Board.getGameState() == GameState.DRAW){
			
			System.out.println("The game has ended in a draw!");
			appendToMoves("A draw has occured!");
			alert.setHeaderText("The game has ended in a draw!");
			alert.setContentText("Play again?");
			
		} else {
			
			GamePiece winner = (Board.getGameState() == GameState.X_WON) ? GamePiece.X : GamePiece.O;
			
			if (winner == cpuPiece){
				cScore++;
				
				appendToMoves("The CPU has won!");
				alert.setHeaderText("The CPU has won the game!");
				alert.setContentText('"' + cpu.getRandomTaunt() + '"' + " - CPU");
				
				System.out.println("The CPU has won the game!");
			} else {
				
				appendToMoves(((playerHandle == "Player") ? "The Player" : playerHandle) + " has won!");
				
				alert.setHeaderText("You won the game!");
				alert.setContentText("Play again?");
				
				System.out.println("You won the game!");
				pScore++;
			}
			
			pScoreDisplay.setText("Player: " + pScore);
			cScoreDisplay.setText("CPU: " + cScore);
			
		}
		
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

		alert.setOnHidden(x -> {
			ButtonType result = alert.getResult();
				
			if (result == buttonTypeOne){
				
				System.out.println("Restarting game");

		    	playGame();
		    	pStageReference.setScene(gameScene);
		    	pStageReference.show();
		    	
			} else if (result == buttonTypeTwo) {
				
				pStageReference.setScene(introScene);
				pStageReference.show();
		    	
			}
			
		});
		alert.show();

		
	}
	
	public static void flipPlayer(){
		currentPlayer = (currentPlayer == GamePiece.X) ? GamePiece.O : GamePiece.X;
		turnDisplay.setText("It's " + ((currentPlayer == playerPiece) ? (((playerHandle == "Player") ? "the " : "") + playerHandle) : "the CPU")+ "'s Turn!");
		System.out.println("Current Turn: [" + currentPlayer + "]");
		
		if ((currentPlayer == playerPiece) && (holdButtonForMove != null) && (Board.checkPlace(holdButtonForMove[0], holdButtonForMove[1]))) {
			Button btn = pieceDisplays[holdButtonForMove[0]][holdButtonForMove[1]];
			btn.setStyle("fx-background-color:#dae7f3; -fx-padding: 5, 5, 5, 5;");
		}
	}
	
	// Helper methods
	
	public void appendButtonHoverSequence(Button button, String css_in, String css_out) {
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				button.setStyle(css_in); // -fx-background-color:#dae7f3;
			}
		});
		
		button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				button.setStyle(css_out); // -fx-background-color: #FFF; -fx-border-color: #000
			}
		});
	}
	
	public static void setButtonImage(Button imgButton, String imgUrl) {
		
		if (imgUrl != "") {
			
			Image piece = new Image(imgUrl);
			ImageView pieceDisplay = new ImageView(piece);
			pieceDisplay.setFitWidth(100);
			pieceDisplay.setFitHeight(100);
			
			imgButton.setGraphic(pieceDisplay);
			
		} else {
			imgButton.setGraphic(null);
		}

	}
	
	public static void appendToMoves(String message) {
		
		Text newMove = new Text(message);
		newMove.setStyle("-fx-font: 14 Gafata;");
		moveTracker.getChildren().add(newMove);
		
		scrollMoveTracker.setVvalue(1.0);
		
	}
	
	public static Timeline artificialWait(int t) {
		
		Timeline aWait = new Timeline(
			new KeyFrame(Duration.millis(t), e -> {
				System.out.println("Waiting for " + t + " milliseconds");
			})
		);
		aWait.setCycleCount(1);
		
		return aWait;
		
	}
	
	public static Timeline setPieceDisplay(int row, int column, GamePiece piece){
		
		appendToMoves(piece + " placed at " + (row + 1) + ", " + (column + 1));

		Button btn = pieceDisplays[row][column];
		setButtonImage(btn, (piece == GamePiece.X) ? img_Icon_X : img_Icon_O);
		
		int curX = (int)btn.getLayoutX();
		int curY = (int)btn.getLayoutY();
		
		btn.setLayoutX(curX - 20);
		btn.setLayoutY(curY - 30);
		
		Timeline animation = new Timeline(
			new KeyFrame(Duration.millis(20), e -> {
				btn.setLayoutX(curX);
				btn.setLayoutY(curY);
				System.out.println("Drop animation");
			})
		);
		animation.setCycleCount(1);

		return animation;
		
	}

}