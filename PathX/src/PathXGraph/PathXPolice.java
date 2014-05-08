/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathXGraph;

import PathX.PathX;
import static PathX.PathXConstants.POLICE_MINDLESS_STATE;
import static PathX.PathXConstants.POLICE_SELECTED_STATE;
import static PathX.PathXConstants.POLICE_STATE;
import static PathX.PathXConstants.POLICE_TYPE;
import PathX.PathXGame;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import PathXScreens.GameScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class PathXPolice
{

    private Sprite Sprite;

    private PathXGame game;
    private GameLevel level;
    public boolean atIntersection = true;
    public Intersection targetIntersection, currentIntersection;
    private Road currentRoad;

    public boolean collided;

    private long mcStart;
    private long mcEnd;
    private long mcDelta;
    
    private long mtStart;
    private long mtEnd;
    private long mtDelta;
    
    private long timeStart;
    private long timeEnd;
    private long timeDelta;
    
    public boolean stunned;
    public long stunStart;
    private long stunEnd;
    private long stunDelta;

    public boolean powerUp;

    public boolean flatTire, emptyGasTank;
    public boolean mindControl, mindControlSelected;
    public boolean mindlessTerror;

    private PathXPolice thisP;
    public Intersection tempTarget;
    public ArrayList<Road> shortestPath;
    private int currentRoadInt;
    private boolean controlArrived;

    public PathXPolice(final PathXGame game, GameLevel level)
    {
        this.game = game;
        this.level = level;
        thisP = this;

        int r = 0;
        int min = 0;
        int max = level.getIntersections().size();
        while (currentIntersection == null)
        {
            r = (int) ((min + Math.random() * (max - min)));
            if (r != 0 && r != 1)
            {
                Intersection tempIntersection = level.getIntersections().get(r);
                if (tempIntersection.open && (tempIntersection != level.getStartingLocation() || tempIntersection != level.getDestination()))
                {
                    currentIntersection = tempIntersection;
                }
            }
        }

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathX.PathXPropertyType.PATH_IMG);

        BufferedImage img;
        SpriteType sT;

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_POLICE));
        sT = new SpriteType(POLICE_TYPE);
        sT.addState(POLICE_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_POLICE_SELECTED));
        sT.addState(POLICE_SELECTED_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_POLICE_MINDLESS));
        sT.addState(POLICE_MINDLESS_STATE, img);
        Sprite = new Sprite(sT, currentIntersection.x - 20, currentIntersection.y - 20, 0, 0, POLICE_STATE);
        Sprite.setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                PathXDataModel d = (PathXDataModel) game.getDataModel();
                if (game.GameScreen.flatTire && !powerUp)
                {
                    game.GameScreen.flatTire = false;
                    game.GameScreen.currentPowerUp = "";
                    d.updateMoney(-20);
                    flatTire = true;
                    timeStart = System.currentTimeMillis();
                    powerUp = true;
                    return;
                }

                if (game.GameScreen.emptyGasTank && !powerUp)
                {
                    game.GameScreen.emptyGasTank = false;
                    game.GameScreen.currentPowerUp = "";
                    d.updateMoney(-20);
                    emptyGasTank = true;
                    timeStart = System.currentTimeMillis();
                    powerUp = true;
                    return;
                }

                if (game.GameScreen.mindControl && !powerUp && !game.GameScreen.selected)
                {
                    Sprite.setState(POLICE_SELECTED_STATE);
                    game.GameScreen.policeControlled = thisP;
                    game.GameScreen.selected = true;
                    return;
                }

                if (game.GameScreen.mindlessTerror && !powerUp && !stunned && !game.GameScreen.selected)
                {
                    ((PathXDataModel) game.getDataModel()).updateMoney(-30);
                    Sprite.setState(POLICE_MINDLESS_STATE);
                    mindlessTerror = true;
                    powerUp = true;
                    game.GameScreen.currentPowerUp = "";
                    mtStart = System.currentTimeMillis();
                }

            }
        });
    }

    public void update()
    {
        if (stunned)
        {
            stunEnd = System.currentTimeMillis();
            stunDelta = (stunEnd - stunStart) / 1000;
            

            if (stunDelta == 20)
            {
                stunned = false;
                return;
            }
            
            return;
        }
        
        if (flatTire)
        {
            timeEnd = System.currentTimeMillis();
            timeDelta = (timeEnd - timeStart) / 1000;

            if (timeDelta == 10)
            {
                timeStart = 0;
                timeEnd = 0;
                timeDelta = 0;

                flatTire = false;
                powerUp = false;
            }

            return;
        }

        if (emptyGasTank)
        {
            timeEnd = System.currentTimeMillis();
            timeDelta = (timeEnd - timeStart) / 1000;

            if (timeDelta == 20)
            {
                timeStart = 0;
                timeEnd = 0;
                timeDelta = 0;

                emptyGasTank = false;
                powerUp = false;
            }

            return;
        }

        Sprite.update(game);

        if (mindControl)
        {
            mindControl();
            return;
        }

        if (mindlessTerror)
        {
            mindlessTerror();
            return;
        }

        regularActivity();
    }
    
    private void regularActivity()
    {
        if (atIntersection)
        {
            if (mindControlSelected)
            {
                mindControlSelected = false;
                mindControl = true;
                return;
            }
            
            //IF THERE ARE NO POSSIBLE INTERSECTIONS AVAILABLE, DO NOTHING
            boolean open = false;   
            for (int i = 0; i < currentIntersection.getIntersections().size(); i++)
            {
                if (currentIntersection.getIntersections().get(i).open && !currentIntersection.getIntersections().get(i).closed)
                {
                    open = true;
                }
            }
            if (!open && currentIntersection.closed)
            {
                return;
            }

            //RUN WHILE WE DON'T HAVE A TARGET
            int min = 0;
            int max = currentIntersection.getRoads().size();
            
            while (targetIntersection == null)
            {
                //GET RANDOM INTERSECTION ATTATCHED TO THE CURRENT INTERSECTION
                int r = (int) ((min + Math.random() * (max - min)));
                Intersection tempIntersection = currentIntersection.getIntersections().get(r);

                //IF THE INTERSECTION ISN'T OPEN OR IS THE DESTINATION/STARTING INTERSECTION THEN SKIP THIS WHILE LOOP ITERATION
                if (!tempIntersection.open || tempIntersection.closed)
                {
                    continue;
                }
                if ((tempIntersection == level.getStartingLocation()) || (tempIntersection == level.getDestination()))
                {
                    continue;
                }

                //FIND A THE ROAD TO TRAVEL ON
                Iterator<Road> roadIt = currentIntersection.getRoads().iterator();
                Road road = null;
                while (roadIt.hasNext())
                {
                    Road tempRoad = roadIt.next();
                    if (tempRoad.closed)    continue;
                    
                    if ((tempRoad.node1 == currentIntersection) && (tempRoad.node2 == tempIntersection))
                    {
                        
                        road = tempRoad;
                        break;
                    } else if (((tempRoad.node1 == tempIntersection) && (tempRoad.node2 == currentIntersection)) && !tempRoad.oneWay)
                    {
                        road = tempRoad;
                        break;
                    }
                }

                if (road != null)
                {
                    targetIntersection = tempIntersection;
                    currentRoad = road;
                    atIntersection = false;
                }
                
                if (road == null)
                {
                    targetIntersection = null;
                    return;
                }
            }
        } //move
        else
        {
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;
            
            //SET VELOCITY
            Sprite.setVx(x * currentRoad.getSpeedLimit());
            Sprite.setVy(y * currentRoad.getSpeedLimit());

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);

            
            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                Sprite.setVx(0);
                Sprite.setVy(0);
                
                
                if (!targetIntersection.open)
                {
                    Intersection i = currentIntersection;
                    currentIntersection = targetIntersection;
                    targetIntersection = i;
                    
                    return;
                }
                
                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                //PREPARE NEXT LOOP FOR CREATING A NEW TARGET INTERSECTION
                currentIntersection = targetIntersection;
                targetIntersection = null;
                atIntersection = true;
            }
        }
    }
    
    private void mindControl()
    {
        if (shortestPath == null)
        {
            setShortestPath();
            atIntersection = false;
            return;
        }

            //WAITING UNDER MIND CONTROL
            if (controlArrived)
            {
                mcEnd = System.currentTimeMillis();
                mcDelta = (mcEnd - mcStart) / 1000;
                
                if (mcDelta == 20)
                {
                    mcStart = 0;
                    mcEnd = 0;
                    mcDelta = 0;
                    
                    controlArrived = false;
                    mindControl = false;
                    atIntersection = true;
                    powerUp = false;
                    
                    targetIntersection = null;
                    shortestPath = null;
                    currentRoadInt = 0;
                }
                return;
            }
            
              //STOPPED BECAUSE RODE IS CLOSED
            if (currentRoadInt <= shortestPath.size()-1 && shortestPath.get(currentRoadInt).closed)
            {
                   if (mindControl)
                    {
                        mcStart = System.currentTimeMillis();
                        controlArrived = true;
                        return;
                    }
                    
                    targetIntersection = null;
                    shortestPath = null;
                    atIntersection = true;
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    currentRoadInt = 0;
                    return;
            }
            
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get((currentRoadInt)).getSpeedLimit());
            Sprite.setVy(y * shortestPath.get((currentRoadInt)).getSpeedLimit());

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);


            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                Sprite.setVx(0);
                Sprite.setVy(0);
  

                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                //INCRAMENT INTERSECTIONS
                currentIntersection = targetIntersection;
                currentRoadInt++;

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoadInt == shortestPath.size())
                {
                    if (mindControl)
                    {
                        mcStart = System.currentTimeMillis();
                        controlArrived = true;
                        return;
                    }
                    
                    targetIntersection = null;
                    shortestPath = null;
                    atIntersection = true;
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    currentRoadInt = 0;
                }
                //GET NEXT TARGET NODE
                else
                {
                    if (currentIntersection == shortestPath.get(currentRoadInt).getNode1())
                    {
                        targetIntersection = shortestPath.get(currentRoadInt).getNode2();
                    } else
                    {
                        targetIntersection = shortestPath.get(currentRoadInt).getNode1();
                    }
                }
            }
    }
    
    
    /*
     * SHOREST PATH FOR MIND CONTROL
     */
    private void setShortestPath()
    {
        powerUp = true;

        shortestPath = game.GameScreen.getGraphManager().shortestNoRLDijkstraPath(currentIntersection, tempTarget);
        currentRoadInt = 0;
        if (currentIntersection == shortestPath.get(currentRoadInt).getNode1())
        {
            targetIntersection = shortestPath.get(currentRoadInt).getNode2();
        } else
        {
            targetIntersection = shortestPath.get(currentRoadInt).getNode1();
        }
    }
    
    /*
     *  RANDOMNESS FOR SHORTEST PATH
     */
    private void mindlessTerror()
    {
        mtEnd = System.currentTimeMillis();
        mtDelta = (mtEnd - mtStart) / 1000;
        
        if (mtDelta == 20)
        {
            Sprite.setState(POLICE_STATE);
            mindlessTerror = false;
            mtStart = 0;
            mtEnd = 0;
            mtDelta = 0;
            powerUp = false;
            return;
        }
        
        //CHECK COLLISION
        Iterator<PathXPolice> pIt = game.GameScreen.getPolice().iterator();
        while (pIt.hasNext())
        {
            PathXPolice p = pIt.next();
            
            if (p == this) continue;
            if (p.stunned) continue;
            if (p.mindlessTerror) continue;
            
            if (p.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                p.stunStart = System.currentTimeMillis();
                p.stunned = true;
            }
        }
        
        Iterator<PathXBandit> bIt = game.GameScreen.getBandits().iterator();
        while (bIt.hasNext())
        {
            PathXBandit b = bIt.next();
            
            if (b.stunned) continue;
            if (b.mindlessTerror) continue;
            
            if (b.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                b.stunStart = System.currentTimeMillis();
                b.stunned = true;
            }
        }
        
        Iterator<PathXZombie> zIt = game.GameScreen.getZombies().iterator();
        while (zIt.hasNext())
        {
            PathXZombie z = zIt.next();
            
            if (z.stunned) continue;
            if (z.mindlessTerror) continue;
            
            if (z.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                z.stunStart = System.currentTimeMillis();
                z.stunned = true;
            }
        }
        
        if (atIntersection)
        {
        //IF THERE ARE NO POSSIBLE INTERSECTIONS AVAILABLE, DO NOTHING
            boolean open = false;
            for (int i = 0; i < currentIntersection.getIntersections().size(); i++)
            {
                if (currentIntersection.getIntersections().get(i).open && !currentIntersection.getIntersections().get(i).closed)
                {
                    open = true;
                }
            }
            if (!open)
            {
                return;
            }

            //RUN WHILE WE DON'T HAVE A TARGET
            int min = 0;
            int max = currentIntersection.getRoads().size();
            while (targetIntersection == null)
            {
                //GET RANDOM INTERSECTION ATTATCHED TO THE CURRENT INTERSECTION
                int r = (int) ((min + Math.random() * (max - min)));
                Intersection tempIntersection = currentIntersection.getIntersections().get(r);

                if ((tempIntersection == level.getStartingLocation()) || (tempIntersection == level.getDestination()))
                {
                    continue;
                }

                //FIND A THE ROAD TO TRAVEL ON
                Iterator<Road> roadIt = currentIntersection.getRoads().iterator();
                Road road = null;
                while (roadIt.hasNext())
                {
                    Road tempRoad = roadIt.next();
                    
                    if ((tempRoad.node1 == currentIntersection) && (tempRoad.node2 == tempIntersection))
                    {
                        
                        road = tempRoad;
                        break;
                    } else if (((tempRoad.node1 == tempIntersection) && (tempRoad.node2 == currentIntersection)) && !tempRoad.oneWay)
                    {
                        road = tempRoad;
                        break;
                    }
                }

                if (road != null)
                {
                    targetIntersection = tempIntersection;
                    currentRoad = road;
                    atIntersection = false;
                }
                
                if (road == null)
                {
                    targetIntersection = null;
                    return;
                }
            }
        } //move
        else
        {
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * currentRoad.getSpeedLimit() * 2);
            Sprite.setVy(y * currentRoad.getSpeedLimit() * 2);

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);

            
            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                Sprite.setVx(0);
                Sprite.setVy(0);
               
                
                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                //PREPARE NEXT LOOP FOR CREATING A NEW TARGET INTERSECTION
                currentIntersection = targetIntersection;
                targetIntersection = null;
                atIntersection = true;
            }
        }
    }

    public void setTarget(Intersection target)
    {
        tempTarget = target;
    }

    public Sprite getSprite()
    {
        return Sprite;
    }

    public Intersection getCurrentIntersection()
    {
        return currentIntersection;
    }
}
