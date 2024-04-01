  
package ca.mcgill.ecse223.kingdomino;

import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;

public class KingdominoApplication {

	private static Kingdomino kingdomino;
	private static GameplayStatemachine statemachine;

	public static void main(String[] args) {
		System.out.println("Hello World!");
	}

	//KingdominoModel
	public static Kingdomino getKingdomino() {
		if (kingdomino == null) {
			kingdomino = new Kingdomino();
		}
		return kingdomino;
	}

	public static void setKingdomino(Kingdomino kd) {
		kingdomino = kd;
	}

	public static GameplayStatemachine getStatemachine()
	{
		if (statemachine == null)
			statemachine = new GameplayStatemachine();
		return statemachine;
	}

	public static void setStatemachine(GameplayStatemachine sm)
	{
		statemachine = sm;
	}
}