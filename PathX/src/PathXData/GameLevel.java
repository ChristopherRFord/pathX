package PathXData;

import PathX.PathXGame;
import static PathXData.GameLevel.GameLevelState.*;
import PathXGraph.Intersection;
import PathXGraph.Road;
import java.util.ArrayList;
import mini_game.Sprite;

/**
 * Contains all the information that defines a Game Level 
 * @author Christopher Ford
 */
public class GameLevel
{
    
    PathXGame game;
    
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
    Sprite backgroundImage;
    Intersection destination;
    String destinationImageFileName;
    
    Intersection playerLocation;

    // THE AMOUNT OF MONEY TO BE EARNED BY THE LEVEL
    public int money;
    public int recievedMoney;

    // THE NUMBER OF POLICE, BANDITS, AND ZOMBIES
    int numPolice;
    int numBandits;
    int numZombies;
    
    private String state;
    public String type;
    public int ID;
    
    public GameLevel(String name, int amount, String type, String state, String loc, int ID)
    {
        levelName = name;
        money = amount;
        this.type = type;
        recievedMoney = amount;
        this.state = state;
        levelLocation = loc;
        this.ID = ID;
        
        // INIT THE GRAPH DATA STRUCTURES
        intersections = new ArrayList();
        roads = new ArrayList();    
    }

    
    // ACCESSOR METHODS
    public String                   getLevelName()                      {   return levelName;                       }
    public String                   getLevelLocation()                  {   return levelLocation;                   }
    public String                   getStartingLocationImageFileName()  {   return startingLocationImageFileName;   }
    public String                   getBackgroundImageFileName()        {   return backgroundImageFileName;         }
    public String                   getDestinationImageFileName()       {   return destinationImageFileName;        }
    public ArrayList<Intersection>  getIntersections()                  {   return intersections;                   }
    public ArrayList<Road>          getRoads()                          {   return roads;                           }
    public Intersection             getStartingLocation()
    {
        playerLocation = startingLocation;
        return startingLocation;
    }
    public Intersection             getPlayerLocation()                 {   return playerLocation;                  }
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
    {   
        this.money = money; 
        recievedMoney = money;
    }
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
        recievedMoney = 0;
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
            return "./path_x/buttons/ButtonLevelUnlocked.png";
        if (state.equals(LOCKED_STATE.toString()))
            return "./path_x/buttons/ButtonLevelLocked.png";
        if (state.equals(COMPLETED_STATE.toString()))
            return "./path_x/buttons/ButtonLevelCompleted.png";
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
