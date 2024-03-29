import java.util.*;
public class aiTicTacToe {


	public int player; //1 for player 1 and 2 for player 2
	public int depth = 4; //Initialize depth to 3; otherwise, function may take too long
	public List<List<positionTicTacToe>>  winningLines = new ArrayList<>();

	private int isEnded(List<positionTicTacToe> board)
	{
		winningLines = initializeWinningLines();

		//test whether the current game is ended

		//brute-force
		for(int i=0;i<winningLines.size();i++)
		{

			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);

			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);

			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
			{
				//someone wins
				p0.state = state0;
				p1.state = state1;
				p2.state = state2;
				p3.state = state3;

				//print the satisified winning line (one of them if there are several)
				//p0.printPosition();
				//p1.printPosition();
				//p2.printPosition();
				//p3.printPosition();
				return state0;
			}
		}
		for(int i=0;i<board.size();i++)
		{
			if(board.get(i).state==0)
			{
				//game is not ended, continue
				return 0;
			}
		}
		return -1; //call it a draw
	}
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}
	private void addchecker(positionTicTacToe position, List<positionTicTacToe> board, int player) {
		int index = position.x*16+position.y*4+position.z;
		board.get(index).state = player;

	}
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}



	public positionTicTacToe RANDOMALGORITHM (List<positionTicTacToe> board, int player) {
		positionTicTacToe myNextMove;
		do
					{
						Random rand = new Random();
						int x = rand.nextInt(4);
						int y = rand.nextInt(4);
						int z = rand.nextInt(4);
						myNextMove = new positionTicTacToe(x,y,z);
					}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
				return myNextMove;
	}
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		//positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);

		//do
		//	{
		//		Random rand = new Random();
		//		int x = rand.nextInt(4);
		//		int y = rand.nextInt(4);
		//		int z = rand.nextInt(4);
		//		myNextMove = new positionTicTacToe(x,y,z);
		//	}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		//return myNextMove;

		int highestvalue = Integer.MIN_VALUE;
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int z = 0; z < 4; z++) {
					positionTicTacToe potential = new positionTicTacToe(x, y, z);
					if (getStateOfPositionFromBoard(potential,board) == 0) {
						//makeMove(potential, player, board);
						addchecker(potential, board, player);
						int potentialscore = minimax(board, depth ,player);
						if (potentialscore > highestvalue) {
							highestvalue = potentialscore;
							myNextMove = new positionTicTacToe(x, y, z);
						}
						//makeMove(potential, 0, board);
						addchecker(potential, board, 0);

					}
				}
			}
		}
		return myNextMove;	
	}

	public int heuristicval(List<positionTicTacToe> board, int player) {
		// return big values if game ends
		if (isEnded(board) != 0) {
			if (isEnded(board) == 1) {
				return 1000000000;
			}

			if (isEnded(board) == 2) {
				return -1000000000;
			}
		}
		
		// return values based on # of potential winning lines
		else {
			winningLines = initializeWinningLines();
			int value = 0;
			int countthreeinrow1 = 0;
			int countthreeinrow2 = 0;
			for(int i=0;i<winningLines.size();i++)
			{

				positionTicTacToe p0 = winningLines.get(i).get(0);
				positionTicTacToe p1 = winningLines.get(i).get(1);
				positionTicTacToe p2 = winningLines.get(i).get(2);
				positionTicTacToe p3 = winningLines.get(i).get(3);

				int state0 = getStateOfPositionFromBoard(p0,board);
				int state1 = getStateOfPositionFromBoard(p1,board);
				int state2 = getStateOfPositionFromBoard(p2,board);
				int state3 = getStateOfPositionFromBoard(p3,board);
				
				//Check if there's three same checkers in a row; want to stop opponent from winning
				if (state0 == 1) {
					countthreeinrow1++;
				}
				if (state0 == 2) {
					countthreeinrow2++;
				}
				if (state1 == 1) {
					countthreeinrow1++;
				}
				if (state1 == 2) {
					countthreeinrow2++;
				}
				if (state2 == 1) {
					countthreeinrow1++;
				}
				if (state2 == 2) {
					countthreeinrow2++;
				}
				if (state3 == 1) {
					countthreeinrow1++;
				}
				if (state3 == 2) {
					countthreeinrow2++;
				}
				
				//three in a row
				if ((countthreeinrow1 == 3 && countthreeinrow2 == 0) || (countthreeinrow1 == 0 && countthreeinrow2 == 3)) {
					if (player == 1) {
						value += 10000;
					}
					else if (player == 2) {
						value -= 10000;
					}
				}
				
				//two in a row
				if ((countthreeinrow1 == 2 && countthreeinrow2 == 0) || (countthreeinrow1 == 0 && countthreeinrow2 == 2)) {
					if (player == 1) {
						value += 100;
					}
					else if (player == 2) {
						value -= 100;
					}
				}
				
				//one in a row
				if ((countthreeinrow1 == 1 && countthreeinrow2 == 0) || (countthreeinrow1 == 0 && countthreeinrow2 == 1)) {
					if (player == 1) {
						value += 1;
					}
					else if (player == 2) {
						value -= 1;
					}
				}
				
				
				//Otherwise, calculate how many potential winning rows you have
				//if ((state0==1 || state0==0) && (state1==1 || state1==0) && (state2==1 || state2==0) && (state3==1 || state3==0)) {
				//	value++;
				//}
				//else if ((state0==2 || state0==0) && (state1==2 || state1==0) && (state2==2 || state2==0) && (state3==2 || state3==0)) {
					//value--;
				//}
			}
			return value;
		}
		return 0;
	}

	public int minimax(List<positionTicTacToe> board, int depth, int player) {
		/**
		 * IMPLEMENT MINIMAX FUNCTION:
		 */

		// First half: BASE CASE
		if (depth == 0 || isEnded(board) != 0) {
			return heuristicval(board, player);
		}

		// Second half: RECURSIVE CASE

		// For maximizing player:
		if (player == 1) {
			int value = Integer.MIN_VALUE;
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					for (int z = 0; z < 4; z++) {
						positionTicTacToe child = new positionTicTacToe(x, y, z);
						if (getStateOfPositionFromBoard(child, board) == 0) {
							List<positionTicTacToe> clonedboard = deepCopyATicTacToeBoard(board);
							//makeMove(child, player, clonedboard);	
							addchecker(child, clonedboard, player);
							int new_score = Math.max(value, minimax(clonedboard, depth - 1, 2));
							return new_score;
						}

					}
				}
			}

		}

		// For minimizing player:
		else if (player == 2) {
			int value = Integer.MAX_VALUE;
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					for (int z = 0; z < 4; z++) {
						positionTicTacToe child = new positionTicTacToe(x, y, z);
						if (getStateOfPositionFromBoard(child, board) == 0) {
							List<positionTicTacToe> clonedboard = deepCopyATicTacToeBoard(board);
							//makeMove(child, player, clonedboard);
							addchecker(child, clonedboard, player);
							int new_score = Math.min(value, minimax(clonedboard, depth - 1, 1));
							return new_score;
						}

					}
				}
			}
		}
		return 0;

	}

	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();

		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}

		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
			oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
			oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
			oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
			winningLines.add(oneWinCondtion);
		}
		//yz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
			oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
			oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
			oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
			winningLines.add(oneWinCondtion);
		}
		//xy plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
			oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
			oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
			oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
			winningLines.add(oneWinCondtion);
		}

		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
			oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
			oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
			oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
			winningLines.add(oneWinCondtion);
		}
		//yz plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
			oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
			oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
			oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
			winningLines.add(oneWinCondtion);
		}
		//xy plane-4
		for(int i = 0; i<4; i++)
		{
			List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
			oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
			oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
			oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
			oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
			winningLines.add(oneWinCondtion);
		}

		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	

		return winningLines;

	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}
}
