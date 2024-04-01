package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.model.*;
import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Property controller manages detecting and setting of all properties in a given kingdom.
 * It stores some private member variables which are used to keep track of some state data while the controller is running.
 * While this is not typical for a controller, it is very useful in this specific case and checks have been made to ensure that
 * there are no issues that can arise from this.
 * @author Roey Borsteinas
 */
public class PropertyController
{
    // Store some private member variables here which will always be reset
    // each time this controller is used. this is just here to simplify the
    // recursive steps
    private Kingdom kingdom = null;
    private TerrainType[][] terrainMap = null;
    private DominoInKingdom[][] dominoMap = null;

    /**
     * Initialize this instance of the property controller with a kingdom
     * @param kingdom kingdom to map out for the property controller.
     * @author Roey Borsteinas
     */
    public PropertyController(Kingdom kingdom)
    {
        this.kingdom = kingdom;
        terrainMap = getTerrainMap(kingdom);
        dominoMap = getDominoMap(kingdom);
    }


    /**
     * This function will just reset the known properties and rebuild the entire list from scratch
     * @author Roey Borsteinas
     */
    public void identifyProperties()
    {
        // Reset property list
        for (Property p : kingdom.getProperties())
        {
            kingdom.removeProperty(p);
        }

        // In case this function is being run multiple times per instance of this controller,
        // make sure to regenerate these from scratch each time.
        if (terrainMap == null)
            terrainMap = getTerrainMap(kingdom);
        if (dominoMap == null)
            dominoMap = getDominoMap(kingdom);

        // Get a list of all the dominoes which need to be placed in properties
        List<DominoInKingdom> dominoesInKingdom = new LinkedList<>();
        for (KingdomTerritory kingdomTerritory : kingdom.getTerritories())
        {
            if (kingdomTerritory instanceof Castle)
                continue;

            dominoesInKingdom.add((DominoInKingdom)kingdomTerritory);
        }

        // For each domino in the kingdom, build up its property by checking each of it's neighbours
        // recursively and constructing properties while the terrain types are the same.
        for (int i = 0; i < dominoMap.length; ++i)
        {
            for (int j = 0; j < dominoMap[0].length; ++j)
            {
                if (dominoMap[i][j] != null)
                    recursivePropertyBuild(i, j, null);
            }
        }

        // Clear these since they may change between runs and need to guarantee fresh maps each time.
        terrainMap = null;
        dominoMap = null;
    }

    /**
     * Build up a property by recursing over all neighbours of the same type
     * as the current node.
     * @author Roey Borsteinas
     * @param adjustedX x + 4 position of the node to check
     * @param adjustedY y + 4 position of the node to check
     * @param property current property. Set this to null to create a new one
     */
    private void recursivePropertyBuild(int adjustedX, int adjustedY, Property property)
    {
        if (dominoMap[adjustedX][adjustedY] == null) return;

        if (property == null)
        {
            property = new Property(kingdom);
            property.setPropertyType(terrainMap[adjustedX][adjustedY]);
        }

        property.addIncludedDomino(dominoMap[adjustedX][adjustedY].getDomino());

        // Remove this domino from the map to make sure its not added twice
        dominoMap[adjustedX][adjustedY] = null;

        // If this domino has the same type as it's neighbour, add it to the property and recurse on their neighbours.
        // right
        if (adjustedX + 1 < 9 &&
            terrainMap[adjustedX + 1][adjustedY] == terrainMap[adjustedX][adjustedY])
        {
            recursivePropertyBuild(adjustedX + 1, adjustedY, property);
        }
        // left
        if (adjustedX - 1 > 0 &&
                terrainMap[adjustedX - 1][adjustedY] == terrainMap[adjustedX][adjustedY])
        {
            recursivePropertyBuild(adjustedX - 1, adjustedY, property);
        }
        // up
        if (adjustedY + 1 < 9 &&
                terrainMap[adjustedX][adjustedY + 1] == terrainMap[adjustedX][adjustedY])
        {
            recursivePropertyBuild(adjustedX, adjustedY + 1, property);
        }
        // down
        if (adjustedY - 1 > 0 &&
                terrainMap[adjustedX][adjustedY - 1] == terrainMap[adjustedX][adjustedY])
        {
            recursivePropertyBuild(adjustedX, adjustedY - 1, property);
        }
    }
}
