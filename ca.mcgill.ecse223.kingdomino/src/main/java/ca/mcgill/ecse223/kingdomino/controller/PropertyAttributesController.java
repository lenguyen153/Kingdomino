package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.model.Property;

/**
 * Property attribute controller takes in a property of the player's kingdom and calculate the attributes of the property.
 * @author Vincent Trinh
 *
 */
public class PropertyAttributesController {

	public PropertyAttributesController() {}

	/**
	 * This function takes in a property and calculate the total amount of crown.
	 * @author Vincent Trinh
	 * @param aProperty property that is needed to be calculate the total amount of crown.
	 * @return total amount of crown in aProperty.
	 */
	public static boolean CalculatePropertyCrown(Property aProperty) {
		int totalCrown = 0;
		for(int i = 0; i < aProperty.getIncludedDominos().size(); i++) {
			if(aProperty.getPropertyType().equals(aProperty.getIncludedDomino(i).getLeftTile())) {
				int leftCrown = aProperty.getIncludedDomino(i).getLeftCrown();
				totalCrown = totalCrown + leftCrown;
			}else if(aProperty.getPropertyType().equals(aProperty.getIncludedDomino(i).getRightTile())) {
				int rightCrown = aProperty.getIncludedDomino(i).getRightCrown();
				totalCrown = totalCrown  + rightCrown;
			}
		}

		return aProperty.setCrowns(totalCrown);
	}

	/**
	 * This function takes in a property and calculate the total amount of tile.
	 * @author Vincent Trinh
	 * @param aProperty property that is needed to be calculate the total amount of tile.
	 * @return total amount of tile in aProperty.
	 */
	public static boolean CalculatePropertySize(Property aProperty) {
		int size = 0;
		for(int i = 0; i < aProperty.getIncludedDominos().size(); i++) {
			if(aProperty.getPropertyType().equals(aProperty.getIncludedDomino(i).getLeftTile())) {
				size++;
			}
			if(aProperty.getPropertyType().equals(aProperty.getIncludedDomino(i).getRightTile())) {
				size++;
			}
		}
		return aProperty.setSize(size);
	}
}
