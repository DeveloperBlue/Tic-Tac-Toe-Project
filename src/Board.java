public class Board {
	
	private static GamePiece[][] gameArray = new GamePiece[3][3];
	private static GameState gameState = GameState.PLAYING;
	
	public Board() {}
	
	public Board(GamePiece[][] gameArray) {
		
		// Constructor for loading a previous/generated game in
		
		Board.gameArray = gameArray;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Piece Placement ///////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public static boolean checkPlace(int x, int y){
		return (gameArray[x][y] == null) ? true : false;
	}
	
	public static void placePiece(int x, int y, GamePiece piece){
		gameArray[x][y] = piece;
		System.out.println("[" + piece + "] was placed at " + x + ", " + y);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Board Handling ////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public static GamePiece[][] getGameArrayData(){
		return gameArray;
	}
	
	public static void clearGameArray(){
		gameArray = new GamePiece[3][3];
		System.out.println("The game field was reset");
	}
	
	public static GameState getGameState(){
		return gameState;
	}

	public static void setGameState(GameState gameState){
		Board.gameState = gameState;
	}
	
	
	public static void calculateGameState(){
		
		// Check the columns in each row
		for (int row = 0; row < gameArray.length; row++){
			if ((gameArray[row][0] != null) && (gameArray[row][0] == gameArray[row][1]) && (gameArray[row][0] == gameArray[row][2])){
				gameState = (gameArray[row][0].name() == "X") ? GameState.X_WON : GameState.O_WON;
				return;
			}
		}
		
		// Check the rows in each column
		for (int column = 0; column < gameArray[0].length; column++){
			if ((gameArray[0][column] != null) && (gameArray[0][column] == gameArray[1][column]) && (gameArray[0][column] == gameArray[2][column])){
				gameState = (gameArray[0][column].name() == "X") ? GameState.X_WON : GameState.O_WON;
				return;
			}
		}
		
		// Check diagonal (Top-Left -> Bottom-Right)
		if ((gameArray[0][0] != null) && (gameArray[0][0] == gameArray[1][1]) && (gameArray[0][0] == gameArray[2][2])){
			gameState = (gameArray[0][0].name() == "X") ? GameState.X_WON : GameState.O_WON;
			return;
		}
		
		// Check diagonal (Bottom-Left -> Top-Right)
		if ((gameArray[2][0] != null) && (gameArray[2][0] == gameArray[1][1]) && (gameArray[2][0] == gameArray[0][2])){
			gameState = (gameArray[2][0].name() == "X") ? GameState.X_WON : GameState.O_WON;
			return;
		}
		
		// Check for open places
		for (int row = 0; row < gameArray.length; row++){
			
			for (int column = 0; column < gameArray[row].length; column++){

				if (gameArray[row][column] == null){
					
					gameState = GameState.PLAYING;
					return;
				}
				
			}
			
		}

		gameState = GameState.DRAW;
		return;
		
	}

	///////////////////////////////////////////////////////////////////////////
	// Board Display /////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public static String getDisplayString(int x, int y){
		return (gameArray[x][y] != null) ? gameArray[x][y].name() : " ";
	}
	
	public static void printGame(){
		
		System.out.println("");
		System.out.println(" " + getDisplayString(0,0) + " | " + getDisplayString(0,1) + " | " + getDisplayString(0,2) + " ");
		System.out.println("-----------");
		System.out.println(" " + getDisplayString(1,0) + " | " + getDisplayString(1,1) + " | " + getDisplayString(1,2) + " ");
		System.out.println("-----------");
		System.out.println(" " + getDisplayString(2,0) + " | " + getDisplayString(2,1) + " | " + getDisplayString(2,2) + " ");
		
	}
	
}