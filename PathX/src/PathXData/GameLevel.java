package PathXData;

import static PathXData.GameLevel.GameLevelState.*;
import PathXGraph.Intersection;
import PathXGraph.Road;
import java.util.ArrayList;

/**
 * Contains all the information that defines a Game Level 
 * @author Christopher Ford
 */
public class GameLevel
{
    // EVERY LEVEL HAS A NAME
    String levelName;
    
    String levelLocation;

    // THE LEVEL BACKGROUND
    String startingLocationImageFileName;

    // COMPLETE LIST OF INTERSECTIONS SORTED LEFT TO RIGHT
    ArrayList<Intersection> intersections;

    // COMPLETE LIST OF ROADS SORTED BY STARTING INTERSECTION LOCATION LEFT TO RIGHT
    ArrayList<Road> roads;

    // THE STARTING LOCATION AND DESTINATION
    Intersection startingLocation;
    String backgroundImageFileName;
    Intersection destination;
    String destinationImageFileName;

    // THE AMOUNT OF MONEY TO BE EARNED BY THE LEVEL
    int money;

    // THE NUMBER OF POLICE, BANDITS, AND ZOMBIES
    int numPolice;
    int numBandits;
    int numZombies;
    
    private String state;
    
    public GameLevel(String name, int amount, String state, String loc)
    {
        levelName = name;
        money = amount;
        this.state = state;
        levelLocation = loc;
        
        // INIT THE GRAPH DATA STRUCTURES
        intersections = new ArrayList();
        roads = new ArrayList();    
    }

    /**
     * Initializes this level to get it up and running.
     */
    public void init (  String initLevelName,
                        String initBackgroundImageFileName,
                        String initStartingLocationImageFileName,
                        int startingLocationX, 
                        int startingLocationY,
                        String initDestinationImageFileName,
                        int destinationX, 
                        int destinationY)
    {
        // THESE THINGS ARE KNOWN
        levelName = initLevelName;
        backgroundImageFileName = initBackgroundImageFileName;
        startingLocationImageFileName = initStartingLocationImageFileName;
        destinationImageFileName = initDestinationImageFileName;
        
        // AND THE STARTING LOCATION AND DESTINATION
        startingLocation = new Intersection(startingLocationX, startingLocationY);
        intersections.add(startingLocation);
        destination = new Intersection(destinationX, destinationY);
        intersections.add(destination);
        
        // THESE THINGS WILL BE PROVIDED DURING LEVEL EDITING
        money = 0;
        numPolice = 0;
        numBandits = 0;
        numZombies = 0;
    }
    
    // ACCESSOR METHODS
    public String                   getLevelName()                      {   return levelName;                       }
    public String                   getLevelLocation()                  {   return levelLocation;                   }
    public String                   getStartingLocationImageFileName()  {   return startingLocationImageFileName;   }
    public String                   getBackgroundImageFileName()        {   return backgroundImageFileName;         }
    public String                   getDestinationImageFileName()       {   return destinationImageFileName;        }
    public ArrayList<Intersection>  getIntersections()                  {   return intersections;                   }
    public ArrayList<Road>          getRoads()                          {   return roads;                           }
    public Intersection             getStartingLocation()               {   return startingLocation;                }
    public Intersection             getDestination()                    {   return destination;                     }
    public int                      getMoney()                          {   return money;                           }
    public int                      getNumPolice()                      {   return numPolice;                       }
    public int                      getNumBandits()                     {   return numBandits;                      }
    public int                      getNumZombies()                     {   return numZombies;                      }
    
    // MUTATOR METHODS
    public void setLevelName(String levelName)    
    {   this.levelName = levelName;                                             }
    public void setNumBandits(int numBandits)
    {   this.numBandits = numBandits;                                           }
    public void setBackgroundImageFileName(String backgroundImageFileName)    
    {   this.backgroundImageFileName = backgroundImageFileName;                 }
    public void setStartingLocationImageFileName(String startingLocationImageFileName)    
    {   this.startingLocationImageFileName = startingLocationImageFileName;     }
    public void setDestinationImageFileName(String destinationImageFileName)    
    {   this.destinationImageFileName = destinationImageFileName;               }
    public void setMoney(int money)    
    {   this.money = money;                                                     }
    public void setNumPolice(int numPolice)    
    {   this.numPolice = numPolice;                                             }
    public void setNumZombies(int numZombies)
    {   this.numZombies = numZombies;                                           }
    public void setStartingLocation(Intersection startingLocation)
    {   this.startingLocation = startingLocation;                               }
    public void setDestination(Intersection destination)
    {   this.destination = destination;                                         }
    
    /**
     * Clears the level graph and resets all level data.
     */
    public void reset()
    {
        levelName = "";
        startingLocationImageFileName = "";
        intersections.clear();
        roads.clear();
        startingLocation = null;
        backgroundImageFileName = "";
        destination = null;
        destinationImageFileName = "";
        money = 0;
        numPolice = 0;
        numBandits = 0;
        numZombies = 0;
    }
    
    public String getName(){ return levelName; }
    public int getamount(){ return money; }
    public String getState(){ return state; }
    public void setState(String state){ this.state = state; }
    
    public String getProperty()
    {
        if (state.equals(UNLOCKED_STATE.toString()))
            return "./path_x/ButtonLevelUnlocked.png";
        if (state.equals(LOCKED_STATE.toString()))
            return "./path_x/ButtonLevelLocked.png";
        if (state.equals(COMPLETED_STATE.toString()))
            return "./path_x/ButtonLevelCompleted.png";
        else
            return "";
    }
    
    public static enum GameLevelState
    {
        UNLOCKED_STATE,
        LOCKED_STATE,
        COMPLETED_STATE
    }
}
