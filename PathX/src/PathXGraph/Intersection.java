
package PathXGraph;

import static PathX.PathXConstants.CLOSED_LIGHT_STATE;
import static PathX.PathXConstants.GREEN_LIGHT_STATE;
import static PathX.PathXConstants.RED_LIGHT_STATE;
import PathXData.GameLevel;
import java.util.ArrayList;
import java.util.Iterator;
import mini_game.Sprite;


/**
 *
 * @author Chris
 */
public class Intersection
{
    // INTERSECTION LOCATION
    public int x;
    public int y;
    public int ID;
    public double distance;
    
    public Intersection previous;
    
    private ArrayList<Intersection> intersections;
    private ArrayList<Road>         roads;
    
    // IS IT OPEN OR NOT
    public boolean open;
    public boolean closed;
    
    public boolean visited = false;
    
    private Sprite sprite;
    
    private boolean makeLightGreen;
    private boolean makeLightRed;
    
    private long timeStart;
    private long timeEnd;
    private long timeDelta;
    

    /**
     * Constructor allows for a custom location, note that all
     * intersections start as open.
     */
    public Intersection(int initID, int initX, int initY)
    {
        x = initX;
        y = initY;
        ID = initID;
        open = true;  
    }

    // ACCESSOR METHODS
    public int getX()       {   return x;       }
    public int getY()       {   return y;       }
    public int getID()      {   return ID;      }
    public boolean isOpen() {   return open;    }
    public ArrayList<Intersection>  getIntersections()      {   return intersections;   }
    public ArrayList<Road>          getRoads()              {   return roads;           }
    public String getState()
    {
        if (closed) return CLOSED_LIGHT_STATE;
        
        if (open) return GREEN_LIGHT_STATE;
        else return RED_LIGHT_STATE;
    }
    
    // MUTATOR METHODS
    public void setX(int x)
    {   this.x = x;         }
    public void setY(int y)
    {   this.y = y;         }
    public void setOpen(boolean open)
    {   this.open = open;   }
    public void setConnections(GameLevel level)
    {
        intersections = new ArrayList<Intersection>();
        roads = new ArrayList<Road>();
        
        Iterator<Road> roadIt = level.getRoads().iterator();
        while (roadIt.hasNext())
        {
            Road road = roadIt.next();
            
            if (road.node1 == this)
            {
                intersections.add(road.node2);
                roads.add(road);
            }
            else if (road.node2 == this)
            {
                intersections.add(road.node1);
                roads.add(road);
            }
        }
    }
    public void setSprite(Sprite sprite)
    {   this.sprite = sprite;   }
    
    /**
     * This toggles the intersection open/closed.
     */
    public void toggleOpen()
    {
        open = !open;
    }
    
    public void update()
    {
        if (makeLightGreen)
        {
            timeEnd = System.currentTimeMillis();
            timeDelta = (timeEnd - timeStart)/1000;
            
            if (timeDelta == 10)
            {
                timeStart = 0;
                timeEnd = 0;
                timeDelta = 0;
                makeLightGreen = false;
                open = false;
                sprite.setState(RED_LIGHT_STATE);
            }
        }
        else if (makeLightRed)
        {
            timeEnd = System.currentTimeMillis();
            timeDelta = (timeEnd - timeStart)/1000;
            
            if (timeDelta == 10)
            {
                timeStart = 0;
                timeEnd = 0;
                timeDelta = 0;
                makeLightRed = false;
                open = true;
                sprite.setState(GREEN_LIGHT_STATE);
            }
        }

    }
    
    public void makeLightGreen()
    {
        if (makeLightGreen || makeLightRed) return;
                
        timeStart = System.currentTimeMillis();
        makeLightGreen = true;
        open = true;
        sprite.setState(GREEN_LIGHT_STATE);
    }
    
    public void makeLightRed()
    {
        if (makeLightGreen || makeLightRed) return;
                
        timeStart = System.currentTimeMillis();
        makeLightRed = true;
        open = false;
        sprite.setState(RED_LIGHT_STATE);
    }
    
    public void closeIntersection()
    {
        closed = true;

        sprite.setState(CLOSED_LIGHT_STATE);
        Iterator<Road> rIt = roads.iterator();
        while (rIt.hasNext())
        {
            rIt.next().closed = true;
        }
    }
    
    public void openIntersection()
    {
        closed = false;

        if (open) sprite.setState(GREEN_LIGHT_STATE);
        else    sprite.setState(RED_LIGHT_STATE);
        
        Iterator<Road> rIt = roads.iterator();
        while (rIt.hasNext())
        {
            rIt.next().closed = false;
        }
    }
    
    
    /**
     * Returns a textual representation of this intersection.
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
