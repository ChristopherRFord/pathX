/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathXGraph;

import PathX.PathX;
import static PathX.PathXConstants.BANDIT_MINDLESS_STATE;
import static PathX.PathXConstants.BANDIT_SELECTED_STATE;
import static PathX.PathXConstants.BANDIT_STATE;
import static PathX.PathXConstants.BANDIT_TYPE;
import PathX.PathXGame;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import PathXScreens.GameScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class PathXBandit
{

    private Sprite Sprite;
    private PathXGame game;
    private GameLevel level;

    private boolean atIntersection = true;
    public Intersection targetIntersection, currentIntersection;
    private ArrayList<Road> shortestPath;
    private int currentRoad;
    private boolean rl;

    public boolean collided;

    public int moneyHeld;

    private long mcStart;
    private long mcEnd;
    private long mcDelta;
    
    private long timeStart;
    private long timeEnd;
    private long timeDelta;

    public boolean powerUp;

    public boolean flatTire, emptyGasTank;
    public boolean mindControl, mindControlSelected;
    public boolean mindlessTerror;
    
    public boolean stunned;
    public long stunStart;
    private long stunEnd;
    private long stunDelta;

    private PathXBandit thisB;
    public Intersection tempTarget;
    private boolean controlArrived;

    public PathXBandit(final PathXGame game, GameLevel level)
    {
        this.game = game;
        this.level = level;
        thisB = this;

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
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_BANDIT));
        sT = new SpriteType(BANDIT_TYPE);
        sT.addState(BANDIT_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_BANDIT_SELECTED));
        sT.addState(BANDIT_SELECTED_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_BANDIT_MINDLESS));
        sT.addState(BANDIT_MINDLESS_STATE, img);
        Sprite = new Sprite(sT, currentIntersection.x - 20, currentIntersection.y - 20, 0, 0, BANDIT_STATE);
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
                    Sprite.setState(BANDIT_SELECTED_STATE);
                    game.GameScreen.banditControlled = thisB;
                    game.GameScreen.selected = true;
                    return;
                }
                if (game.GameScreen.mindlessTerror && !powerUp && !stunned && !game.GameScreen.selected)
                {
                    ((PathXDataModel) game.getDataModel()).updateMoney(-30);
                    Sprite.setState(BANDIT_MINDLESS_STATE);
                    mindlessTerror = true;
                    powerUp = true;
                    game.GameScreen.currentPowerUp = "";
                    timeStart = System.currentTimeMillis();
                }
            }
        });
        moneyHeld = (int) Math.round(level.getMoney() * .2);
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
        //REACHED INTERSECTION
        if (atIntersection)
        {
            if (mindControlSelected)
            {
                mindControlSelected = false;
                mindControl = true;
                return;
            }
            
            
            //RUN WHILE WE DON'T HAVE A TARGET
            int min = 0;
            int max = level.getIntersections().size();
            
            //RUN WHILE WE DON'T HAVE A TARGET
            while (targetIntersection == null)
            {
                //GENERATE RANDOM INTERSECTION
                int r = (int) ((min + Math.random() * (max - min)));
                Intersection tempIntersection = level.getIntersections().get(r);
               
                
                //IF INTERSECTION IS STARTING OR DESTINATION LOCATION
                if (tempIntersection == level.getPlayerLocation() || tempIntersection == level.getDestination()) continue;
                //IF THE INTERSECTION IS THE CURRENT INTERSECTION
                if (tempIntersection == currentIntersection) return;
                
                //IF THERE IS NO PATH
                shortestPath = ((GameScreen) game.getCurrentScreen()).getGraphManager().shortestNoCRDijkstraPath(currentIntersection, tempIntersection);

                if (shortestPath == null && currentIntersection.closed) continue;
                
                try
                {
                    //FIND FIRST NODE
                    if (currentIntersection == shortestPath.get(0).getNode1())
                    {
                        targetIntersection = shortestPath.get(0).getNode2();
                    } else
                    {
                        targetIntersection = shortestPath.get(0).getNode1();
                    }
                } catch (NullPointerException | IndexOutOfBoundsException e){continue;}
                
            }
            
            atIntersection = false;
        }
        else
        {

            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit());
            Sprite.setVy(y * shortestPath.get((currentRoad)).getSpeedLimit());
          
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
                
            }

            //IF THE PLAYER HAS REACHED TARGET INTERSECTION
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

                //INCRAMENT INTERSECTIONS
                currentIntersection = targetIntersection;
                currentRoad++;

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
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
                    currentRoad = 0;
                }
                //GET NEXT TARGET NODE
                else
                {
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
                    currentRoad = 0;
                }
                return;
            }
            
              //STOPPED BECAUSE RODE IS CLOSED
            if (currentRoad <= shortestPath.size()-1 && shortestPath.get(currentRoad).closed)
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
                    currentRoad = 0;
                    return;
            }
            
            //CREATE VELOCITY
            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            //SET VELOCITY
            Sprite.setVx(x * shortestPath.get((currentRoad)).getSpeedLimit());
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
                currentRoad++;

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
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
                    currentRoad = 0;
                }
                //GET NEXT TARGET NODE
                else
                {
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
