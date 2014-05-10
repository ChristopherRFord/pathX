/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathXGraph;

import PathX.PathX;

import static PathX.PathXConstants.POLICE_MINDLESS_STATE;
import static PathX.PathXConstants.ZOMBIE_MINDLESS_STATE;
import static PathX.PathXConstants.ZOMBIE_SELECTED_STATE;
import static PathX.PathXConstants.ZOMBIE_STATE;
import static PathX.PathXConstants.ZOMBIE_TYPE;
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
public class PathXZombie
{

    private Sprite Sprite;

    private PathXGame game;
    private GameLevel level;
    private PathXDataModel d;
    
    private ArrayList<Road> shortestPath;
    public Intersection targetIntersection, currentIntersection;
    private int currentRoad;
    private boolean reverse = false;

    private Intersection startIntersection, finalIntersection;

    public boolean collided;

    public int moneyHeld;

    private long timeStart;
    private long timeEnd;
    private long timeDelta;

    private long mcStart;
    private long mcEnd;
    private long mcDelta;
    
    private long mtStart;
    private long mtEnd;
    private long mtDelta;

    public boolean powerUp;

    public boolean flatTire, emptyGasTank;

    public boolean mindControl, mindControlSelected;
    private boolean controlArrived, pathBack;
    private boolean rl;

    public boolean mindlessTerror, closestIntersection;

    public boolean stunned;
    public long stunStart;
    private long stunEnd;
    private long stunDelta;
    private Road currentR;

    private PathXZombie thisZ;
    public Intersection tempTarget;
    private boolean atIntersection = true;

    public PathXZombie(final PathXGame game, GameLevel level)
    {
        this.game = game;
        this.level = level;
        thisZ = this;
        d = (PathXDataModel) game.getDataModel();

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
                    startIntersection = tempIntersection;
                }
            }
        }

        while (targetIntersection == null)
        {
            //GET RANDOM INTERSECTION ATTATCHED TO THE CURRENT INTERSECTION
            r = (int) ((min + Math.random() * (max - min)));
            Intersection tempIntersection = level.getIntersections().get(r);

            //IF THE INTERSECTION ISN'T OPEN OR IS THE DESTINATION/STARTING INTERSECTION 
            //OR THE CURRENTER INTERSECTION EQUALS THE TARGET INTERSECTIONTHEN SKIP THIS WHILE LOOP ITERATION
            if (!tempIntersection.open)
            {
                continue;
            }
            if ((tempIntersection == level.getStartingLocation()) || (tempIntersection == level.getDestination()))
            {
                continue;
            }
            if (currentIntersection == tempIntersection)
            {
                continue;
            }

            targetIntersection = tempIntersection;
            finalIntersection = tempIntersection;
        }

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathX.PathXPropertyType.PATH_IMG);

        BufferedImage img;
        SpriteType sT;

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_ZOMBIE));
        sT = new SpriteType(ZOMBIE_TYPE);
        sT.addState(ZOMBIE_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_ZOMBIE_SELECTED));
        sT.addState(ZOMBIE_SELECTED_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_ZOMBIE_MINDLESS));
        sT.addState(ZOMBIE_MINDLESS_STATE, img);
        Sprite = new Sprite(sT, currentIntersection.x - 20, currentIntersection.y - 20, 0, 0, ZOMBIE_STATE);

        Sprite.setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                PathXDataModel d = (PathXDataModel) game.getDataModel();
                if (game.GameScreen.flatTire)
                {
                    game.GameScreen.flatTire = false;
                    game.GameScreen.currentPowerUp = "";
                    d.updateMoney(-20);
                    flatTire = true;
                    timeStart = System.currentTimeMillis();
                    powerUp = true;
                    return;
                }

                if (game.GameScreen.emptyGasTank)
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
                    Sprite.setState(ZOMBIE_SELECTED_STATE);
                    game.GameScreen.zombieControlled = thisZ;
                    game.GameScreen.selected = true;
                    return;
                }
                if (game.GameScreen.mindlessTerror && !powerUp && !stunned && !game.GameScreen.selected)
                {
                    ((PathXDataModel) game.getDataModel()).updateMoney(-30);
                    Sprite.setState(ZOMBIE_MINDLESS_STATE);
                    mindlessTerror = true;
                    powerUp = true;
                    game.GameScreen.currentPowerUp = "";

                    mtStart = System.currentTimeMillis();
                }

            }

        });

        moneyHeld = (int) Math.round(level.getMoney() * .5);
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
                
                shortestPath = null;
                targetIntersection = null;
                currentRoad = 0;
                return;
            }

            if (!reverse)
            {
                shortestPath = game.GameScreen.getGraphManager().shortestNoCRDijkstraPath(startIntersection, finalIntersection);
            } else
            {
                shortestPath = game.GameScreen.getGraphManager().shortestNoCRDijkstraPath(finalIntersection, startIntersection);
            }
            
            if (shortestPath == null)
                return;
            if (shortestPath.isEmpty())
                return;

            //FIND FIRST NODE
            currentR = shortestPath.get(0);
            if (currentIntersection == shortestPath.get(0).getNode1())
            {
                targetIntersection = shortestPath.get(0).getNode2();
            } else
            {
                targetIntersection = shortestPath.get(0).getNode1();
            }

            atIntersection = false;
        } 
        else
        {
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            if (shortestPath != null)
            {
                //SET VELOCITY
                Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
                Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
            } else
            {
                //SET VELOCITY
                Sprite.setVx(x * currentR.getSpeedLimit());
                Sprite.setVy(y * currentR.getSpeedLimit());
            }

            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);

            //RECOVERING FROM A RED LIGHT
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20) && rl)
            {
                Sprite.setVx(0);
                Sprite.setVy(0);

                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                //REVERSE THE
                rl = false;
                currentIntersection = targetIntersection;
                targetIntersection = null;
                shortestPath = null;
                currentRoad = 0;

                atIntersection = true;
            } //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            else if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                Sprite.setVx(0);
                Sprite.setVy(0);

                //SETTING UP RECOVERING FROM A RED LIGHT
                if (rl)
                {
                    rl = false;
                    currentIntersection = targetIntersection;
                    targetIntersection = null;
                    atIntersection = true;

                    return;
                }

                //WE HIT A RED LIGHT
                if (!targetIntersection.open && !rl)
                {
                    rl = true;

                    Intersection i = currentIntersection;
                    currentIntersection = targetIntersection;
                    targetIntersection = i;

                    return;
                }

                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                currentIntersection = targetIntersection;
                
                currentRoad++;
                
                if (mindControlSelected)
                {
                    mindControlSelected = false;
                    mindControl = true;
                
                    shortestPath = null;
                    currentRoad = 0;
                    return;
                }

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
                {
                    reverse = !reverse;
                    targetIntersection = null;
                    shortestPath = null;
                    atIntersection = true;
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    currentRoad = 0;

                } //GET NEXT TARGET NODE
                else
                {
                    currentR = shortestPath.get(currentRoad);
                    if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode2();
                    } else
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode1();
                    }
                }
            }
        }
    }

    private void mindControl()
    {      
        if (pathBack)
        {
            if (shortestPath ==  null)
            {
                shortestPath = game.GameScreen.getGraphManager().shortestNoCRDijkstraPath(currentIntersection, startIntersection);
                
                if (currentIntersection == null) System.out.println("a");
                if (shortestPath == null)   System.out.println("b");
                
                if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode2();
                } 
                else
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode1();
                }
                return;
            }
         
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
            Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
            
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
                currentRoad++;
                
                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
                {
                    mindControl = false;
                    pathBack = false;
                    shortestPath = null;
                    currentRoad = 0;
                    targetIntersection = null;
                    atIntersection = true;
                    reverse = false;

                } //GET NEXT TARGET NODE
                else
                {
                    if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode2();
                    } 
                    else
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode1();
                    }
                }
            }
        
            return;
        }
        
        if (shortestPath == null)
        {
            setShortestPath();
            return;
        }
       
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
                pathBack = true;
                atIntersection = true;
                powerUp = false;

                targetIntersection = null;
                shortestPath = null;
                currentRoad = 0;

            }
            return;
        }
        
        //STOPPED BECAUSE RODE IS CLOSED
        if (currentRoad <= shortestPath.size() - 1 && shortestPath.get(currentRoad).closed)
        {

            mcStart = System.currentTimeMillis();
            controlArrived = true;

            return;
        }
        
        
        //CREATE VELOCITY
        float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
        float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

        //SET VELOCITY
        Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
        Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
        
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
            currentRoad++;

            //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
            if (currentRoad == shortestPath.size())
            {

                mcStart = System.currentTimeMillis();
                controlArrived = true;

            } //GET NEXT TARGET NODE
            else
            {
                                    currentR = shortestPath.get(currentRoad);
                if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode2();
                } 
                else
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode1();
                }
            }
        }
    }
     /*
     *  RANDOMNESS FOR SHORTEST PATH
     */
    private void mindlessTerror()
    {
        if (pathBack)
        {
           
            if (shortestPath ==  null)
            {
                shortestPath = game.GameScreen.getGraphManager().shortestNoCRDijkstraPath(currentIntersection, startIntersection);
                

                
                if (shortestPath == null)
                {
                    mindlessTerror = false;
                    pathBack = false;
                    shortestPath = null;
                    currentRoad = 0;
                    targetIntersection = null;
                    atIntersection = true;
                    reverse = false;
                    return;
                }
                
                if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode2();
                } 
                else
                {
                    targetIntersection = shortestPath.get(currentRoad).getNode1();
                }
                return;
            }
         
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
            Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit() * d.gameSpeed);
            
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
                currentRoad++;
                
                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
                {
                    mindlessTerror = false;
                    pathBack = false;
                    shortestPath = null;
                    currentRoad = 0;
                    targetIntersection = null;
                    atIntersection = true;
                    reverse = false;

                } //GET NEXT TARGET NODE
                else
                {
                    if (currentIntersection == shortestPath.get(currentRoad).getNode1())
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode2();
                    } 
                    else
                    {
                        targetIntersection = shortestPath.get(currentRoad).getNode1();
                    }
                }
            }
        
            return;
        }
        mtEnd = System.currentTimeMillis();
        mtDelta = (mtEnd - mtStart) / 1000;

        if (mtDelta > 20 && atIntersection)
        {
            Sprite.setState(ZOMBIE_STATE);
            pathBack = true;
            mtStart = 0;
            mtEnd = 0;
            mtDelta = 0;
            powerUp = false;

            shortestPath = null;

            currentRoad = 0;

            return;
        }
        
        //CHECK COLLISION
        Iterator<PathXPolice> pIt = game.GameScreen.getPolice().iterator();
        while (pIt.hasNext())
        {
            PathXPolice p = pIt.next();

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

            if (z == this) continue;
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
                    currentR = road;
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
            Sprite.setVx(x * currentR.getSpeedLimit() * d.gameSpeed * 2);
            Sprite.setVy(y * currentR.getSpeedLimit() * d.gameSpeed * 2);

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);

            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                if (pathBack) closestIntersection = true;
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

    /*
     * SHOREST PATH FOR MIND CONTROL
     */
    private void setShortestPath()
    {
        powerUp = true;

        shortestPath = game.GameScreen.getGraphManager().shortestNoRLDijkstraPath(currentIntersection, tempTarget);
        currentRoad = 0;


        if (currentIntersection == shortestPath.get(currentRoad).getNode1())
        {
            targetIntersection = shortestPath.get(currentRoad).getNode2();
        } else
        {
            targetIntersection = shortestPath.get(currentRoad).getNode1();
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

}
