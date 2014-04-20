/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXData;

import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import xml_utilities.XMLUtilities;
import static PathX.PathXConstants.*;
import PathXGraph.Intersection;
import PathXGraph.Road;


/**
 *
 * @author Chris
 */
public class PathXLevelLoader
{
    // THIS WILL HELP US PARSE THE XML FILES
    private XMLUtilities xmlUtil;
    
    // THIS IS THE SCHEMA WE'LL USE
    private File levelSchema;

    /**
     * Constructor for making our importer/exporter. Note that it
     * initializes the XML utility for processing XML files and it
     * sets up the schema for use.
     */
    public PathXLevelLoader(File initLevelSchema)
    {
        // THIS KNOWS HOW TO READ AND ACCESS XML FILES
        xmlUtil = new XMLUtilities();
        
        // WE'LL USE THE SCHEMA FILE TO VALIDATE THE XML FILES
        levelSchema = initLevelSchema;
    }
    
     /**
     * Reads the level data found in levelFile into levelToLoad.
     */
    public boolean loadLevel(GameLevel level)
    {
        try
        {

            level.reset();
            
            File levelFile = new File(level.getLevelLocation());

            // FIRST LOAD ALL THE XML INTO A TREE
            Document doc = xmlUtil.loadXMLDocument( levelFile.getAbsolutePath(), 
                                                    levelSchema.getAbsolutePath());
            
            // FIRST LOAD THE LEVEL INFO
            Node levelNode = doc.getElementsByTagName(LEVEL_NODE).item(0);
            NamedNodeMap attributes = levelNode.getAttributes();
            String levelName = attributes.getNamedItem(NAME_ATT).getNodeValue();
            level.setLevelName(levelName);
            String bgImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            level.setBackgroundImageFileName(bgImageName);
            
            //STARTING IMG LOC
            Node startNode = doc.getElementsByTagName(START_NODE).item(0);
            attributes = startNode.getAttributes();
            String stImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            level.setStartingLocationImageFileName(stImageName);
            
            //DEST IMG LOC
            Node destNode = doc.getElementsByTagName(DEST_NODE).item(0);
            attributes = destNode.getAttributes();
            String dtImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            level.setDestinationImageFileName(dtImageName);
            

            // THEN LET'S LOAD THE LIST OF ALL THE REGIONS
            loadIntersectionsList(doc, level);
            ArrayList<Intersection> intersections = level.getIntersections();
            
            // AND NOW CONNECT ALL THE REGIONS TO EACH OTHER
            loadRoadsList(doc, level);
            
            // LOAD THE START INTERSECTION
            Node startIntNode = doc.getElementsByTagName(START_INTERSECTION_NODE).item(0);
            attributes = startIntNode.getAttributes();
            String startIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int startId = Integer.parseInt(startIdText);
            Intersection startingIntersection = intersections.get(startId);
            level.setStartingLocation(startingIntersection);
            
            // LOAD THE DESTINATION
            Node destIntNode = doc.getElementsByTagName(DESTINATION_INTERSECTION_NODE).item(0);
            attributes = destIntNode.getAttributes();
            String destIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int destId = Integer.parseInt(destIdText);
            level.setDestination(intersections.get(destId));
            
            // LOAD THE MONEY
            Node moneyNode = doc.getElementsByTagName(MONEY_NODE).item(0);
            attributes = moneyNode.getAttributes();
            String moneyText = attributes.getNamedItem(AMOUNT_ATT).getNodeValue();
            int money = Integer.parseInt(moneyText);
            level.setMoney(money);
            
            // LOAD THE NUMBER OF POLICE
            Node policeNode = doc.getElementsByTagName(POLICE_NODE).item(0);
            attributes = policeNode.getAttributes();
            String policeText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numPolice = Integer.parseInt(policeText);
            level.setNumPolice(numPolice);
            
            // LOAD THE NUMBER OF BANDITS
            Node banditsNode = doc.getElementsByTagName(BANDITS_NODE).item(0);
            attributes = banditsNode.getAttributes();
            String banditsText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numBandits = Integer.parseInt(banditsText);
            level.setNumBandits(numBandits);
            
            // LOAD THE NUMBER OF ZOMBIES
            Node zombiesNode = doc.getElementsByTagName(ZOMBIES_NODE).item(0);
            attributes = zombiesNode.getAttributes();
            String zombiesText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numZombies = Integer.parseInt(zombiesText);
            level.setNumZombies(numZombies);            
        }
        catch(Exception e)
        {
            // LEVEL DIDN'T LOAD PROPERLY
            return false;
        }
        // LEVEL LOADED PROPERLY
        return true;
    }
    
     // PRIVATE HELPER METHOD FOR LOADING INTERSECTIONS INTO OUR LEVEL
    private void loadIntersectionsList( Document doc, GameLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node intersectionsListNode = doc.getElementsByTagName(INTERSECTIONS_NODE).item(0);
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(intersectionsListNode, INTERSECTION_NODE);
        for (int i = 0; i < intersectionsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node intersectionNode = intersectionsList.get(i);
            NamedNodeMap intersectionAttributes = intersectionNode.getAttributes();
            String idText = intersectionAttributes.getNamedItem(ID_ATT).getNodeValue();
            String openText = intersectionAttributes.getNamedItem(OPEN_ATT).getNodeValue();
            String xText = intersectionAttributes.getNamedItem(X_ATT).getNodeValue();
            int x = Integer.parseInt(xText);
            String yText = intersectionAttributes.getNamedItem(Y_ATT).getNodeValue();
            int y = Integer.parseInt(yText);
            
            // NOW MAKE AND ADD THE INTERSECTION
            Intersection newIntersection = new Intersection(x, y);
            newIntersection.open = Boolean.parseBoolean(openText);
            intersections.add(newIntersection);
        }
    }
    
    // PRIVATE HELPER METHOD FOR LOADING ROADS INTO OUR LEVEL
    private void loadRoadsList( Document doc, GameLevel levelToLoad)
    {
        // FIRST GET THE REGIONS LIST
        Node roadsListNode = doc.getElementsByTagName(ROADS_NODE).item(0);
        ArrayList<Road> roads = levelToLoad.getRoads();
        ArrayList<Intersection> intersections = levelToLoad.getIntersections();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_NODE);
        for (int i = 0; i < roadsList.size(); i++)
        {
            // GET THEIR DATA FROM THE DOC
            Node roadNode = roadsList.get(i);
            NamedNodeMap roadAttributes = roadNode.getAttributes();
            String id1Text = roadAttributes.getNamedItem(INT_ID1_ATT).getNodeValue();
            int int_id1 = Integer.parseInt(id1Text);
            String id2Text = roadAttributes.getNamedItem(INT_ID2_ATT).getNodeValue();
            int int_id2 = Integer.parseInt(id2Text);
            String oneWayText = roadAttributes.getNamedItem(ONE_WAY_ATT).getNodeValue();
            boolean oneWay = Boolean.parseBoolean(oneWayText);
            String speedLimitText = roadAttributes.getNamedItem(SPEED_LIMIT_ATT).getNodeValue();
            int speedLimit = Integer.parseInt(speedLimitText);
            
            // NOW MAKE AND ADD THE ROAD
            Road newRoad = new Road();
            newRoad.setNode1(intersections.get(int_id1));
            newRoad.setNode2(intersections.get(int_id2));
            newRoad.setOneWay(oneWay);
            newRoad.setSpeedLimit(speedLimit);
            roads.add(newRoad);
        }
    }
}
