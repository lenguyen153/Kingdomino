package ca.mcgill.ecse223.kingdomino.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Property;

/**
 * Calculate score controller manages the calculations of the player's bonus scores, property scores, and the players' rankings.
 * @author Vincent Trinh
 *
 */
public class CalculateScoreController {

	public CalculateScoreController() {}

	/**
	 * Check and mark the selected bonus options from the game.
	 * @author Vincent Trinh
	 * @return 	0: None 1: Harmony, 2: MiddleKingdom, 3: Harmony and MiddleKingdom 
	 */
	private static int checkBonus() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		switch (game.numberOfSelectedBonusOptions()) {
		case 0:
			return 0;
		case 1:
			if(game.getKingdomino().getCurrentGame().getSelectedBonusOption(0).getOptionName().equals("Harmony")) {
				return 1;
			}else {
				return 2;
			}
		default:
			return 3;
		}
	}

	/**
	 * This function calculate the right tile  X's coordinate of the domino.
	 * @author Vincent Trinh
	 * @param domino
	 * @return the X's coordinate of the domino.
	 */
	private static int getRightTileX(DominoInKingdom domino) {
		switch(domino.getDirection()) {
		case Right:
			return domino.getX() + 1;
		case Left:
			return domino.getX() - 1;
		default:
			return domino.getX();
		}
	}

	/**
	 * This function calculate the right tile  Y's coordinate of the domino.
	 * @param domino
	 * @return the Y's coordinate of the domino.
	 */
	private static int getRightTileY(DominoInKingdom domino) {
		switch(domino.getDirection()) {
		case Up:
			return domino.getY() + 1;
		case Down:
			return domino.getY() - 1;
		default:
			return domino.getY();
		}
	}

	/**
	 * This function calculate the maximum  X's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return maximum X coordinate of the current kingdom.
	 */
	private static int CalculateMaxX(Kingdom kingdom) {
		int maxCord = kingdom.getTerritory(0).getX();
		for (KingdomTerritory territory : kingdom.getTerritories()){
			if (territory instanceof Castle) {

				continue;
			}
			DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
			if(territory.getX() > maxCord && territory.getX() < 5) {
				maxCord = territory.getX();
				if(maxCord < getRightTileX(dominoInKingdom) && territory.getX() < 5) {
					return maxCord = getRightTileX(dominoInKingdom);
				}
			}
		}
		return maxCord;
	}

	/**
	 * This function calculate the minimum  X's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return minimum X coordinate of the current kingdom.
	 */
	private static int CalculateMinX(Kingdom kingdom) {
		int minCord = kingdom.getTerritory(0).getX();
		for (KingdomTerritory territory : kingdom.getTerritories()){
			if (territory instanceof Castle) {
				if(territory.getX() < minCord && territory.getX() > -5) {
					minCord = territory.getX();
				}
				continue;
			}
			DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
			if(territory.getX() < minCord && territory.getX() > -5) {
				minCord = territory.getX();
				if(minCord > getRightTileX(dominoInKingdom) && territory.getX() > -5) {
					minCord = getRightTileX(dominoInKingdom);
				}
			}
		}
		return minCord;
	}

	/**
	 * This function calculate the middle  X's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return middle X coordinate of the current kingdom.
	 */
	private static int midCordX(Kingdom kingdom) {
		return (CalculateMinX(kingdom) + CalculateMaxX(kingdom));
	}

	/**
	 * This function calculate the maximum  Y's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return maximum Y coordinate of the current kingdom.
	 */
	private static int CalculateMaxY(Kingdom kingdom) {
		int maxCord = kingdom.getTerritory(0).getY();
		for (KingdomTerritory territory : kingdom.getTerritories()){
			if (territory instanceof Castle) {
				if(territory.getY() > maxCord && territory.getY() < 5) {
					maxCord = territory.getY();
				}
				continue;
			}
			DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
			if(territory.getY() > maxCord && territory.getY() < 5) {
				maxCord = territory.getY();
				if(maxCord < getRightTileY(dominoInKingdom) && territory.getY() < 5) {
					maxCord = getRightTileY(dominoInKingdom);
				}
			}
		}
		return maxCord;
	}

	/**
	 * This function calculate the minimum  Y's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return minimum Y's coordinate of the current kingdom.
	 */
	private static int CalculateMinY(Kingdom kingdom) {
		int minCord = kingdom.getTerritory(0).getY();
		for (KingdomTerritory territory : kingdom.getTerritories()){
			if (territory instanceof Castle) {
				if(territory.getY() < minCord && territory.getX() > -5) {
					minCord = territory.getY();
				}
				continue;
			}
			DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
			if(territory.getY() < minCord && territory.getX() > -5) {
				minCord = territory.getY();
				if(minCord > getRightTileY(dominoInKingdom) && territory.getY() > -5) {
					minCord = getRightTileY(dominoInKingdom);
				}
			}
		}
		return minCord;
	}

	/**
	 * This function calculate the middle  Y's coordinate of the kingdom.
	 * @author Vincent Trinh
	 * @param kingdom the current kingdom of the player.
	 * @return middle Y's coordinate of the current kingdom.
	 */
	private static int midCordY(Kingdom kingdom) {
		return (CalculateMinY(kingdom) + CalculateMaxY(kingdom));
	}


	/**
	 * This functions calculate aPlayer bonus score.
	 * @author Vincent Trinh
	 * @param aPlayer player whose bonus score needed to be calculated
	 * @return true if aPlayer's bonus score is successfully set to the model
	 */
	public static boolean CalculateBonusScore(Player aPlayer) {
		int bonusPoints = 0;
		switch(checkBonus()) {
		case 0:
			return aPlayer.setBonusScore(bonusPoints);

		case 1:
			if(aPlayer.getKingdom().getTerritories().size() == 13) {
				return aPlayer.setBonusScore(bonusPoints + 5);
			}else {
				return aPlayer.setBonusScore(bonusPoints);
			}
		case 2:
			if(midCordX(aPlayer.getKingdom()) == 0 && midCordY(aPlayer.getKingdom())== 0) {
				return aPlayer.setBonusScore(bonusPoints + 10);
			}
			return aPlayer.setBonusScore(bonusPoints);

		case 3:
			if((midCordX(aPlayer.getKingdom()) == 0 && midCordY(aPlayer.getKingdom())== 0) && aPlayer.getKingdom().getTerritories().size() == 13 ) {
				return aPlayer.setBonusScore(bonusPoints + 15);
			}else if(midCordX(aPlayer.getKingdom()) == 0 && midCordY(aPlayer.getKingdom())== 0) {
				return aPlayer.setBonusScore(bonusPoints + 10);
			}else if(aPlayer.getKingdom().getTerritories().size() == 13) {
				return aPlayer.setBonusScore(bonusPoints + 5);
			}else {
				return aPlayer.setBonusScore(bonusPoints);
			}
		}
		return false;

	}

	/**
	 * This functions calculate aPlayer property score.
	 * @author Vincent Trinh
	 * @param aPlayer player whose property score needed to be calculated.
	 * @return true if aPlayer's property score is successfully set to the model.
	 */
	public static boolean CalculatePropertyScore(Player aPlayer) {

		PropertyController pc = new PropertyController(aPlayer.getKingdom());
		pc.identifyProperties();

		int totalPropertyScore = 0;

		for(Property aProperty : aPlayer.getKingdom().getProperties()) {
			PropertyAttributesController.CalculatePropertyCrown(aProperty);
			PropertyAttributesController.CalculatePropertySize(aProperty);
			int properties = aProperty.getSize();
			int crown = aProperty.getCrowns();
			totalPropertyScore = totalPropertyScore + properties*crown;
		}
		return aPlayer.setPropertyScore(totalPropertyScore); //castle

	}


	/**
	 * This function calculates the total score of aPlayer.
	 * @author Vincent Trinh
	 * @param aPlayer player whose total score needed to be calculated
	 */
	public static void CalculatePlayerScore(Player aPlayer) {
		CalculatePropertyScore(aPlayer);
		CalculateBonusScore(aPlayer);
	}

	/**
	 * This function sets the rankings of the players in the game based on their total score.
	 * @author Vincent Trinh
	 * @param game the game the players are playing.
	 */
	public static void CalculateRanking(Game game)
	{

		List<Player> myPlayers = game.getPlayers();
		//Calculate for each player their totalScore
		for(Player aPlayer : myPlayers) {
			CalculatePlayerScore(aPlayer);
		}

		Player[] rankings = new Player[4];
		rankings[0]=myPlayers.get(0);
		rankings[1]=myPlayers.get(1);
		rankings[2]=myPlayers.get(2);
		rankings[3]=myPlayers.get(3);

		//Sets the ranking for each player
		for(int i = 0; i < rankings.length; i++) {
			for(int j=0; j < rankings.length;j++) {
				if(rankings[i].getTotalScore()>rankings[j].getTotalScore()) {
					Player temp = rankings[i];
					rankings[i]=rankings[j];
					rankings[j]=temp;
				}
			}
		}

		for(int i =0; i<rankings.length;i++) {
			rankings[i].setCurrentRanking(i+1);
		}
	}

	/**
	 * F#24 tieBreaker
	 * This method solves a tiebreaker case between all the players in the list
	 * @author Maxens
	 * @param playerList the list of the players in the tie
	 * @param highestPossibleRank the highest rank that a player in the list can have
	 */

	public static void solveTieBreaker(List<Player>list, int highestPossibleRank) {
		int maxSize=-1;
		int maxCrown=-1;
		boolean stillTie=false;
		Player winner=null;
		List<Player>playerList=new ArrayList<Player>();
		List<Player>stillTied=new ArrayList<Player>();
		List<Player>moreTied=new ArrayList<Player>();
		for(Player p:list){
			playerList.add(p);
		}
		//In case of a tie with the kingdom size, puts the players who are still in contest in a new arrayList
		for(int i=0;i<playerList.size();i++) {
			int max=-1;
			Property big=null;
			for(Property p:playerList.get(i).getKingdom().getProperties()){
				PropertyAttributesController.CalculatePropertySize(p);
				if(p.getSize()>max){
					max=p.getSize();
					big=p;
				}
			}

			if(big.getSize()>maxSize) {
				maxSize=big.getSize();
				winner=playerList.get(i);
				stillTied.clear();
				stillTied.add(winner);
				stillTie=false;
			}
			else if(big.getSize()==maxSize){
				stillTied.add(playerList.get(i));
				stillTie=true;
			}
		}




		if(stillTie) {
			for(int i=0;i<stillTied.size();i++) {
				int crownCount=0;

				for(Property p:stillTied.get(i).getKingdom().getProperties()) {
					PropertyAttributesController.CalculatePropertyCrown(p);
					crownCount+=p.getCrowns();
				}

				if(crownCount>maxCrown) {
					maxCrown=crownCount;
					winner=stillTied.get(i);
					stillTie=false;
					moreTied.clear();
					moreTied.add(winner);
				}
				else if(crownCount==maxCrown) {
					moreTied.add(stillTied.get(i));
					stillTie=true;
				}
			}

			if(stillTie) {
				for(int i=0;i<playerList.size();i++) {
					playerList.get(i).setCurrentRanking(highestPossibleRank+1);
				}

				for(int i=0;i<moreTied.size();i++) {
					moreTied.get(i).setCurrentRanking(highestPossibleRank);
				}

				for(Player p:moreTied) {
					playerList.remove(p);
				}
				if(!playerList.isEmpty()) {
					solveTieBreaker(playerList, highestPossibleRank+1);
				}


			}
			else {
				for(int i=0;i<playerList.size();i++) {
					playerList.get(i).setCurrentRanking(highestPossibleRank+1);
				}
				winner.setCurrentRanking(highestPossibleRank);
				playerList.remove(winner);
				if(!playerList.isEmpty()) {
					solveTieBreaker(playerList,highestPossibleRank+1);
				}
			}
		}
		else {
			for(int i=0;i<playerList.size();i++) {
				playerList.get(i).setCurrentRanking(highestPossibleRank+1);
			}
			winner.setCurrentRanking(highestPossibleRank);
			playerList.remove(winner);
			if(!playerList.isEmpty()) {
				solveTieBreaker(playerList,highestPossibleRank+1);
			}


		}


	}



	/**
	 * This method implements F#23:calculate player rank
	 * It is a recursive method that will recurse until all players receive the appropriate rank based on score and tiebreaker rules
	 *
	 * @author Maxens Destine
	 * @param highestPossibleRank to indicate what rankt o give to the 'winner' the first time this method is called, the digit '1' should be used
	 * @param playerList the list that contains the players that must be ranked properly
	 */
	public static void calculateRanking(int highestPossibleRank,List<Player>list){
		List<Player>tieList=new ArrayList<Player>();
		List<Player>temp=new ArrayList<Player>();
		List<Player>playerList=new ArrayList<Player>();
		for(Player p:list){
			playerList.add(p);
		}
		Player winner=null;
		boolean hasTie=false;
		int maxPoint=-1;

		for(Player p:playerList) {
			if(p.getTotalScore()>maxPoint) {
				winner=p;
				maxPoint=p.getTotalScore();
				hasTie=false;
				tieList.clear();
				tieList.add(p);
			}
			else if(p.getTotalScore()==maxPoint) {
				hasTie=true;
				tieList.add(p);
			}
		}

		if(hasTie){
			for(Player p:tieList){
				temp.add(p);
			}
			solveTieBreaker(tieList, highestPossibleRank);
			highestPossibleRank=findLowestRank(temp);
			for(int i=0;i<temp.size();i++) {
				playerList.remove(temp.get(i));
			}
			if(!playerList.isEmpty()) {
				calculateRanking(highestPossibleRank+1, playerList);
			}
		}
		else{
			winner.setCurrentRanking(highestPossibleRank);
			playerList.remove(winner);
			if(!playerList.isEmpty()){
				calculateRanking(highestPossibleRank+1, playerList);
			}

		}
	}


	/**
	 * This method implements F#23:calculate player rank
	 * It is a recursive method that will recurse until all players receive the appropriate rank based on score and tiebreaker rules
	 *
	 * @author Maxens Destine
	 * @param highestPossibleRank to indicate what rankt o give to the 'winner' the first time this method is called, the digit '1' should be used
	 * @param playerList the list that contains the players that must be ranked properly
	 */
	public static void calculateRankingNoTie(int highestPossibleRank,List<Player>temp){
		List<Player>tieList=new ArrayList<Player>();
		List<Player>playerList=new ArrayList<Player>();
		for(Player p:temp) {
			playerList.add(p);
		}
		Player winner=null;
		boolean hasTie=false;
		int maxPoint=-1;

		for(Player p:playerList) {
			if(p.getTotalScore()>maxPoint) {
				winner=p;
				maxPoint=p.getTotalScore();
				hasTie=false;
				tieList.clear();
				tieList.add(p);
			}
			else if(p.getTotalScore()==maxPoint) {
				hasTie=true;
				tieList.add(p);
			}
		}

		if(hasTie){
			for(int i=0;i<tieList.size();i++){
				Player p=tieList.get(i);
				p.setCurrentRanking(highestPossibleRank+i);
			}

			highestPossibleRank=highestPossibleRank+temp.size();
			for(int i=0;i<tieList.size();i++) {
				playerList.remove(tieList.get(i));
			}
			if(!playerList.isEmpty()) {
				calculateRanking(highestPossibleRank, playerList);
			}
		}
		else{
			winner.setCurrentRanking(highestPossibleRank);
			playerList.remove(winner);
			if(!playerList.isEmpty()){
				calculateRanking(highestPossibleRank+1, playerList);
			}

		}
	}

	/**
	 * This method is used for F#23:calculate player rank
	 *
	 * This method finds the lowest rank amongst all players of a list
	 *
	 * @author Maxens Destine
	 * @param playerList the list in which to find the lowest rank
	 * @return the lowest ranking in the list
	 */
	public static int findLowestRank(List<Player>playerList){
		int min=playerList.get(0).getCurrentRanking();
		for(Player p:playerList) {
			if(p.getCurrentRanking()>min) {
				min=p.getCurrentRanking();
			}
		}
		return min;
	}

}