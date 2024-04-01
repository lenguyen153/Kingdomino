package ca.mcgill.ecse223.kingdomino.view;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.DraftOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.PlayerOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.TurnOperationCallback;

public class Main {
	
	public static void main(String[] args) {

		 GameplayStatemachine sm = new GameplayStatemachine();
	        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
	        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
	        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback().new DraftOperationCallbackDummy());
	        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback().new GameOperationCallbackDummy());
	        KingdominoApplication.setStatemachine(sm); 
	        sm.init();

		Home home = new Home();
		
	}
	
}
