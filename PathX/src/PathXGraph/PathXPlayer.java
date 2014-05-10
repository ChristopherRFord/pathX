/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathXGraph;

import PathX.PathX;
import static PathX.PathXConstants.PLAYER_INTANGIBILITY_STATE;
import static PathX.PathXConstants.PLAYER_INVINCIBILITY_STATE;
import static PathX.PathXConstants.PLAYER_STATE;
import static PathX.PathXConstants.PLAYER_STEAL_STATE;
import static PathX.PathXConstants.PLAYER_TYPE;
import PathX.PathXGame;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import PathXScreens.GameScreen;
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
public class PathXPlayer
{

    private Sprite Sprite;
    private PathXGame game;
    private GameLevel level;
    private PathXDataModel d;
    
    public boolean moving = false;

    private int currentRoad;
    private ArrayList<Road> shortestPath;
    public Intersection targetIntersection, currentIntersection;

    public boolean collidedPolice;
    public float playerSpeed = 1;
    private int banditsCollision;
    private int zombiesCollision;

    public long stunStart;
    private long stunEnd;
    private long stunDelta;
    
    public long stealStart;
    private long stealEnd;
    private long stealDelta;
    
    public long intangibilityStart;
    private long intangibilityEnd;
    private long intangibilityDelta;

    public long invincibilityStart;
    private long invincibilityEnd;
    private long invincibilityDelta;
    
    public boolean steal;
    public boolean intangibility;
    public boolean invincibility;
    public boolean stunned;

    private float tempVx, tempVy;
    
    public boolean fly;

    public PathXPlayer(PathXGame game, GameLevel level)
    {
        this.game = game;
        this.level = level;
        
        d = (PathXDataModel) game.getDataModel();

        currentIntersection = level.getStartingLocation();

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathX.PathXPropertyType.PATH_IMG);

        BufferedImage img;
        SpriteType sT;

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_PLAYER));
        sT = new SpriteType(PLAYER_TYPE);
        sT.addState(PLAYER_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_PLAYER_STEAL));
        sT.addState(PLAYER_STEAL_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_PLAYER_INTANGIBILITY));
        sT.addState(PLAYER_INTANGIBILITY_STATE, img);
        img = game.loadImage(imgPath + props.getProperty(PathX.PathXPropertyType.IMAGE_PLAYER_INVINCIBILITY));
        sT.addState(PLAYER_INVINCIBILITY_STATE, img);
        Sprite = new Sprite(sT, level.getStartingLocation().x - 20, level.getStartingLocation().y - 20, 0, 0, PLAYER_STATE);
    }

    public void update(GameScreen screen)
    {
        Sprite.update(game);

        if (stunned)
        {
            stunEnd = System.currentTimeMillis();
            stunDelta = (stunEnd - stunStart) / 1000;
            

            if (stunDelta == 20)
            {
                Sprite.setVx(tempVx);
                Sprite.setVy(tempVy);
                stunned = false;
                return;
            }
            
            return;
        }
        
        if (steal)
        {
            stealEnd = System.currentTimeMillis();
            stealDelta = (stealEnd - stealStart) / 1000;
            
            if (stealDelta == 10)
            {
                stealStart = 0;
                stealEnd = 0;
                stealDelta = 0;
                
                Sprite.setState(PLAYER_STATE);
                steal = false;
            }
        }
        
        if (intangibility)
        {
            intangibilityEnd = System.currentTimeMillis();
            intangibilityDelta = System.currentTimeMillis();
            
            if (intangibilityDelta == 10)
            {
                intangibilityStart = 0;
                intangibilityEnd = 0;
                intangibilityDelta = 0;
                
                Sprite.setState(PLAYER_STATE);
                intangibility = false;
            }
        }
        
        if (invincibility)
        {
            invincibilityEnd = System.currentTimeMillis();
            invincibilityDelta = System.currentTimeMillis();
            
            if (invincibilityDelta == 10)
            {
                invincibilityStart = 0;
                invincibilityEnd = 0;
                invincibilityDelta = 0;
                
                Sprite.setState(PLAYER_STATE);
                invincibility = false;
            }
        }

        

        Iterator<PathXPolice> plIt = screen.getPolice().iterator();
        while (plIt.hasNext())
        {
            PathXPolice p = plIt.next();

            if (p.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20) && !intangibility)
            {
                
                if (invincibility)
                {
                    game.GameScreen.getPolice().remove(p);
                    return;
                }
                if (p.mindlessTerror && !intangibility && !stunned)
                {

                    stunned = true;
                    stunStart = System.currentTimeMillis();
                    tempVx = Sprite.getVx();
                    tempVy = Sprite.getVy();
                    Sprite.setVx(0);
                    Sprite.setVy(0);

                    return;
                }
                collidedPolice = true;
                return;
            }
        }

        Iterator<PathXBandit> baIt = screen.getBandits().iterator();
        while (baIt.hasNext())
        {
            PathXBandit b = baIt.next();

            if (b.collided && b.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                break;
            }
            if (b.collided && !b.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                b.collided = false;
                break;
            }
            if (!b.collided && b.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                if (invincibility)
                {
                    game.GameScreen.getBandits().remove(b);
                    return;
                }
                respondToBanditCollision(b);
                b.collided = true;
                break;
            }
        }

        Iterator<PathXZombie> zbIt = screen.getZombies().iterator();
        while (zbIt.hasNext())
        {
            PathXZombie z = zbIt.next();

            if (z.collided && z.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                break;
            }
            if (z.collided && !z.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                z.collided = false;
                break;
            }
            if (!z.collided && z.getSprite().containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                if (invincibility)
                {
                    game.GameScreen.getZombies().remove(z);
                    return;
                }
                respondToZombieCollision(z);
                z.collided = true;
                break;
            }
        }
        
        if (targetIntersection != null && fly)
        {

            float x = (targetIntersection.getX() - currentIntersection.getX()) * .0005f;
            float y = (targetIntersection.getY() - currentIntersection.getY()) * .0005f;

            x = x * playerSpeed;
            y = y * playerSpeed;

            //SET VELOCITY
            Sprite.setVx(x * d.gameSpeed * 10);
            Sprite.setVy(y * d.gameSpeed * 10);
            
            //GET SPRITE OF TARGET INTERSECTION
            Sprite tempIntersection = ((GameScreen) game.getCurrentScreen()).getIntersections().get(targetIntersection.ID - 1);
            if (tempIntersection.containsPoint(Sprite.getX() + 20, Sprite.getY() + 20))
            {
                Sprite.setVx(0);
                Sprite.setVy(0);

                if (targetIntersection.open)
                {
                    Sprite.setX(tempIntersection.getX());
                    Sprite.setY(tempIntersection.getY());
                }
                
                 currentIntersection = targetIntersection;
                targetIntersection = null;
                fly = false;
            }
            
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

            x = x * playerSpeed;
            y = y * playerSpeed;

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
                if (!targetIntersection.open) return;
                
                //SET THE X AND Y COORDS OF THE PLAYER ON TO THE SPRITE
                Sprite.setX(tempIntersection.getX());
                Sprite.setY(tempIntersection.getY());
                
                //STOPPED BECAUSE RODE IS CLOSED
                if (currentRoad <= shortestPath.size()-1 && shortestPath.get(currentRoad).closed)
                {
                    targetIntersection = null;
                    shortestPath = null;
                    Sprite.setVx(0);
                    Sprite.setVy(0);
                    moving = false;
                    currentRoad = 0;
                    return;
                }

                //INCRAMENT INTERSECTIONS
                currentIntersection = targetIntersection;
                currentRoad++;

                //THE DESTINATION HAS BEEN REACHED, STOPPED EVERYTHING
                if (currentRoad == shortestPath.size())
                {
                    targetIntersection = null;
                    shortestPath = null;
                    moving = false;

                    currentRoad = 0;
                } //GET NEXT TARGET NODE
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

    public Intersection getCurrentIntersection()
    {
        return currentIntersection;
    }

    public Sprite getSprite()
    {
        return Sprite;
    }

    public void setCurrentRoad(int cr)
    {
        currentRoad = cr;
    }

    public void setShortestPath(ArrayList<Road> shortestPath)
    {
        if (shortestPath.isEmpty())
        {
            return;
        }

        this.shortestPath = shortestPath;
        moving = true;
        currentRoad = 0;
        if (currentIntersection == shortestPath.get(currentRoad).getNode1())
        {
            targetIntersection = shortestPath.get(currentRoad).getNode2();
        } else
        {
            targetIntersection = shortestPath.get(currentRoad).getNode1();
        }
    }

    public void respondToZombieCollision(PathXZombie z)
    {
        if (intangibility)
        {
            return;
        }

        if (z.mindlessTerror && !stunned)
        {
            stunned = true;
            stunStart = System.currentTimeMillis();
            return;
        }
        
        if (steal)
        {
            PathXDataModel d = (PathXDataModel) game.getDataModel();
            d.updateMoney(z.moneyHeld);
            z.moneyHeld = 0;
            return;
        }
        zombiesCollision++;

        playerSpeed = 1;
        playerSpeed -= (zombiesCollision * .1f);
    }

    public void respondToBanditCollision(PathXBandit b)
    {
        if (intangibility)
        {
            return;
        }
        
        if (b.mindlessTerror && !stunned)
        {
            stunned = true;
            stunStart = System.currentTimeMillis();
            return;
        }
        
        if (steal)
        {
            PathXDataModel d = (PathXDataModel) game.getDataModel();
            d.updateMoney(b.moneyHeld);
            b.moneyHeld = 0;
            return;
        }
        banditsCollision++;

        level.recievedMoney = level.money;
        float subtraction = Math.round(level.money * (banditsCollision * .1f));
        level.recievedMoney -= subtraction;
    }
}
