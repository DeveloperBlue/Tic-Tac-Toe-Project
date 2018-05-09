import java.util.Random;

public class ComputerPlayer
{
   private int[][] preferredMoves = {
         {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
         {0, 1}, {1, 0}, {1, 2}, {2, 1}};

   private Board board;
   private GamePiece playerType;
   
   private String[] tauntingMessages = {
		   
		   "I won? I never win! Wow you must suck!",
		   "Your mother was a hamster, and your father smelled of elderberries! Now go away, or I will taunt you a second time!",
		   "Haha! I've won!",
		   "Incompetent.",
		   "You're the reason the gene pool needs a lifeguard.",
		   "I'd love to insult you, but I won't do as well as nature did."
   };

   /** Constructor with reference to game board */
   public ComputerPlayer(Board board) 
   {
      this.board = board;
   }
   
   public ComputerPlayer(Board board, GamePiece playerType) 
   {
      this.board = board;
      this.playerType = playerType;
   }
 
   public void setPlayerType(GamePiece playerType){
	   this.playerType = playerType;
   }
   
   public GamePiece getPlayerType(){
	   return playerType;
   }
   
   public String getRandomTaunt(){
	return tauntingMessages[new Random().nextInt(tauntingMessages.length)];
	   
   }
   
   /** Search for the first empty cell, according to the preferences
    *  @return int array of two values [row, col]
    */
   public int[] generateMove() 
   {
   		 for (int[] move : preferredMoves) 
   		 {
   		 	 // checks for empty space on board 
   		 	 // (i.e. checks if this "move" is available, if the space is empty its available)
	         if (Board.checkPlace(move[0],move[1]) == true) 
	         {
	        	 System.out.println("Successful move at " + move[0] + ", " + move[1]);
	            return move;
	         }
         }

   		 System.out.println("The CPU has run out of moves!");
         return null;
   }
}