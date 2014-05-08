/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathXGraph;

import PathX.PathX;

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

    public boolean powerUp;

    public boolean flatTire, emptyGasTank;

    public boolean mindControl, selected;
    private ArrayList<Road> tempShortestPath;
    private Intersection tempTargetIntersection;
    private int tempCurrentRoad;

    public boolean mindlessTerror;

    public boolean stunned;
    public long stunStart;
    private long stunEnd;
    private long stunDelta;
    private Road currentRoadTemp;

    private PathXZombie thisZ;
    public Intersection tempTarget;
    private boolean controlArrived;
    private boolean atIntersection;
    private boolean pathBack;

    public PathXZombie(final PathXGame game, GameLevel level)
    {
        this.game = game;
        this.level = level;
        thisZ = this;

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

        shortestPath = ((GameScreen) game.getCurrentScreen()).getGraphManager().shortestNoRLDijkstraPath(currentIntersection, targetIntersection);

        if (currentIntersection == shortestPath.get(0).getNode1())
        {
            targetIntersection = shortestPath.get(0).getNode2();
        } else
        {
            targetIntersection = shortestPath.get(0).getNode1();
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
                    timeStart = System.currentTimeMillis();
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

        /**
         * MIND CONTROL
         */
        if (mindControl)
        {
            if (controlArrived)
            {
                timeEnd = System.currentTimeMillis();
                timeDelta = (timeEnd - timeStart) / 1000;

                if (timeDelta == 5)
                {
                    timeStart = 0;
                    timeEnd = 0;
                    timeDelta = 0;
                    tempTarget = null;
                    tempShortestPath = null;
                    controlArrived = false;
                    tempCurrentRoad = 0;
                    selected = false;
                    powerUp = false;
                    pathBack = true;

                    tempShortestPath = game.GameScreen.getGraphManager().shortestDijkstraPath(currentIntersection, startIntersection);
                    if (currentIntersection == tempShortestPath.get(tempCurrentRoad).getNode1())
                    {
                        tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode2();
                    } else
                    {
                        tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode1();
                    }
                }
            } else if (pathBack)
            {
                boolean finished = mindControl();
                if (finished)
                {
                    atIntersection = false;
                    mindControl = false;
                    selected = false;
                    currentRoad = 0;
                    reverse = false;

                    if (currentIntersection == shortestPath.get(0).getNode1())
                    {
                        targetIntersection = shortestPath.get(0).getNode2();
                    } else
                    {
                        targetIntersection = shortestPath.get(0).getNode1();
                    }

                }
            } else
            {
                controlArrived = mindControl();
            }
            return;
        }


        regularActivity();
    }

    private void regularActivity()
    {

        /**
         * SET UP SHORTEST PATH FOR MIND CONTROL
         */
        if (atIntersection && selected)
        {
            mindControl = true;
            selected = false;
            setShortestPath();
            return;
        }

        /**
         * IF THERE IS A REASON TO MOVE
         */
        if (targetIntersection != null)
        {
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get(currentRoad).getSpeedLimit());
            Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit());

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

                if (!reverse)
                {
                    currentRoad++;
                } else
                {
                    currentRoad--;
                }

                //RETURN FOR MIND CONTROL
                if (selected)
                {
                    atIntersection = true;
                    return;
                }

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size() || currentRoad == -1)
                {
                    reverse = !reverse;
                    if (!reverse)
                    {
                        currentRoad++;
                    } else
                    {
                        currentRoad--;
                    }
                }

                //GET NEXT TARGET NODE
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

    /**
     * DIJKSTRA'S SHORTEST PATH FOR MIND CONTROL
     */
    public boolean mindControl()
    {
        /**
         * IF THERE IS A REASON TO MOVE
         */
        if (tempTargetIntersection != null)
        {
            //CREATE VELOCITY
            float x = (tempTargetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (tempTargetIntersection.getY() - currentIntersection.getY()) * .0005f;
            //SET VELOCITY
            Sprite.setVx(x * tempShortestPath.get((tempCurrentRoad)).getSpeedLimit());
            Sprite.setVy(y * tempShortestPath.get((tempCurrentRoad)).getSpeedLimit());

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(tempTargetIntersection.ID - 1);

            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());

                //INCRAMENT INTERSECTIONS
                currentIntersection = tempTargetIntersection;
                tempCurrentRoad++;

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (tempCurrentRoad == tempShortestPath.size())
                {
                    tempTargetIntersection = null;
                    tempShortestPath = null;
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    tempCurrentRoad = 0;

                    timeStart = System.currentTimeMillis();

                    return true;
                } //GET NEXT TARGET NODE
                else
                {
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    if (currentIntersection == tempShortestPath.get(tempCurrentRoad).getNode1())
                    {
                        tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode2();
                    } else
                    {
                        tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode1();
                    }
                }
            }
        }
        return false;
    }

    private void mindlessTerror()
    {
        if (atIntersection)
        {
            //IF THERE ARE NO POSSIBLE INTERSECTIONS AVAILABLE, DO NOTHING
            boolean open = false;
            for (int i = 0; i < currentIntersection.getIntersections().size(); i++)
            {
                if (currentIntersection.getIntersections().get(i).open)
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

                //IF THE ROAD IS ONE WAY AND CAN'T BE TRAVELED ON, THEN DO NOTHING
                if (road != null)
                {
                    targetIntersection = tempIntersection;
                    currentRoadTemp = road;
                    atIntersection = false;
                }
            }
        } //move
        else
        {
            if (currentRoadTemp == null)
            {
                currentRoadTemp = shortestPath.get(currentRoad);
            }

            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * currentRoadTemp.getSpeedLimit() * 2);
            Sprite.setVy(y * currentRoadTemp.getSpeedLimit() * 2);

            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);

            //POLICE COLLISION
            Iterator<PathXPolice> pIt = game.GameScreen.getPolice().iterator();
            while (pIt.hasNext())
            {
                PathXPolice p = pIt.next();

                if (p.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20) && !p.stunned && (!p.mindlessTerror && !p.mindControl))
                {
                    p.stunned = true;
                    p.stunStart = System.currentTimeMillis();
                }
            }

            Iterator<PathXBandit> bIt = game.GameScreen.getBandits().iterator();
            while (bIt.hasNext())
            {
                PathXBandit b = bIt.next();

                if (b.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
                {
                    b.stunned = true;
                    b.stunStart = System.currentTimeMillis();
                }
            }

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
                currentRoadTemp = null;
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

        tempShortestPath = game.GameScreen.getGraphManager().shortestDijkstraPath(currentIntersection, tempTarget);
        tempCurrentRoad = 0;

        if (currentIntersection == tempShortestPath.get(tempCurrentRoad).getNode1())
        {
            tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode2();
        } else
        {
            tempTargetIntersection = tempShortestPath.get(tempCurrentRoad).getNode1();
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
