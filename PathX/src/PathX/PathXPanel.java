package PathX;

import PathX.PathX.PathXSpriteState;
import static PathX.PathXConstants.*;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import PathXGraph.Intersection;
import PathXGraph.PathXBandit;
import PathXGraph.PathXPolice;
import PathXGraph.PathXZombie;
import PathXGraph.Road;
import PathXScreens.GameScreen;
import PathXScreens.LevelSelectScreen;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.Sprite;
import mini_game.SpriteType;

/**
 * Panel that renders everything for the pathX game
 * 
 * @author Christopher Ford
 */
public class PathXPanel extends JPanel
{
 // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private PathXGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;

    // WE'LL RECYCLE THESE DURING RENDERING
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     * 
     * @param initGame The pathX game that is using
     * this panel for rendering.
     * 
     * @param initData The pathX game data.
     */
    public PathXPanel(PathXGame initGame, PathXDataModel initData)
    {
        game = initGame;
        data = initData;
        
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0,0,0,0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++)
        {
            recyclableStrokes.put(i, new BasicStroke(i*2));
        }
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(   GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
    }
    
   
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            //game.beginUsingData();
        
            // CLEAR THE PANEL
            super.paintComponent(g);

            //If necessary render the level select map
            if (game.getCurrentScreen().getScreentype().equals(LEVEL_SELECT_SCREEN_STATE)) renderLevelSelectMap(g);
            
            if (game.getCurrentScreen().getScreentype().equals(GAME_SCREEN_STATE))
            {
                renderLevelBackground((Graphics2D) g);
                renderRoads((Graphics2D) g);
                renderIntersections((Graphics2D) g);
                renderSprites((Graphics2D) g);
            }
          
            
            //Renders the background
            renderBackground(g);
            
            //If necessary render the level select screen specifics
            if (game.getCurrentScreen().getScreentype().equals(LEVEL_SELECT_SCREEN_STATE)) renderLevelSelectScreen(g);
            
            //Render the GUI controls
            renderGUIControls(g);
            
            //If necessary render the dialog box for the game screen
            if (game.getCurrentScreen().getScreentype().equals(GAME_SCREEN_STATE)) renderDialogBox(g);
    }
    
    // RENDERING HELPER METHODS
        // - renderBackground
        // - renderGUIControls
        // - renderDialogs
   
    /**
     * renderLevelSelectMap
     * @param g - graphics
     * 
     * Renders the map for the Level Select screen
     */
    public void renderLevelSelectMap(Graphics g)
    {
            Sprite b = game.LevelSelectScreen.getDecors().get(LEVEL_SELECT_MAP_TYPE);
            renderSprite(g, b);
            
            Collection<Sprite> buttonSprites = ((LevelSelectScreen)game.getCurrentScreen()).getButtons().values();
            
            for (Sprite s : buttonSprites)
            {
                if (s.getSpriteType().getSpriteTypeID().contains("LEVEL_BUTTON_TYPE"))
                    renderSprite(g, s);
            }
    }
    
    /**`
     * Renders the background image, which is different depending on the screen. 
     * 
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        if (game.getCurrentScreen() == null)
            return;
        
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getCurrentScreen().getDecors().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
        
        if (game.getCurrentScreen() != game.GameScreen) return;
        
                
         g.setFont(FONT_STATS);
         g.setColor(Color.black);
         String speed = game.GameScreen.getPlayer().playerSpeed + "";
         g.drawString(speed, 100, 400);
         String balance = game.GameScreen.getLevel().recievedMoney + "";
         g.drawString(balance, 100, 370);
         balance = data.getBalance() + "";
         g.drawString(balance, 100, 340);
         String currentPowerUp = game.GameScreen.currentPowerUp;
         g.drawString(currentPowerUp, 5, 310);
    }

    /**
     * Renders all the GUI decor and buttons.
     * 
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        if (game.getCurrentScreen() == null)
            return;
        
       // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getCurrentScreen().getButtons().values();
        
        if (game.getCurrentScreen().getScreentype().equals(LEVEL_SELECT_SCREEN_STATE))
        {
            for (Sprite s : buttonSprites)
            {
                if (!s.getSpriteType().getSpriteTypeID().contains("LEVEL_BUTTON_TYPE"))
                {
                    renderSprite(g, s);
                }
            }
        }
        else if (game.getCurrentScreen().getScreentype().equals(GAME_SCREEN_STATE))
        {
             for (Sprite s : buttonSprites)
             {
                if (!s.getSpriteType().getSpriteTypeID().contains("INTERSECTION_TYPE")
                        && !s.getSpriteType().getSpriteTypeID().equals(POLICE_TYPE)
                        && !s.getSpriteType().getSpriteTypeID().equals(BANDIT_TYPE)
                        && !s.getSpriteType().getSpriteTypeID().equals(ZOMBIE_TYPE))
                {
                    renderSprite(g, s);
                }
            }
        }
        else
        {
            for (Sprite s : buttonSprites)
            {
                renderSprite(g, s);
            }
        }
    }
    
    /**
     * renderLevelSelectScreen
     * @param g - graphics
     * 
     * Renders the specifics for the Level Select screen
     */
    public void renderLevelSelectScreen(Graphics g)
    {
           g.setFont(FONT_STATS);
           String balance = "Balance: $" + data.getBalance();
           String goal = "Goal: $" + "100,000";
           g.drawString(balance, 220, 40);
           g.drawString(goal, 220, 80);
           

           GameLevel level = ((LevelSelectScreen) game.getCurrentScreen()).getSelectedLevel();
           
           if (level != null)
           {
               g.drawString(level.getName() + "-$" + level.getamount(), 100, 450);
           }
    }
    
    /**
     * renderDialogBox
     * @param g - Graphics
     * 
     * Renders the Dialog Box for the Game screen
     */
    public void renderDialogBox(Graphics g)
    {
        if (game.GameScreen.getDecors().get(DIALOG_BOX_TYPE).getState().equals(PathXSpriteState.VISIBLE_STATE.toString()))
        {
            Sprite s = game.GameScreen.getDecors().get(DIALOG_BOX_TYPE);
            renderSprite(g, s);
            g.setFont(FONT_STATS);
            g.setColor(Color.BLACK);
            String name = ((GameScreen) game.getCurrentScreen()).getLevel().getName();
            g.drawString(name, 120, 100);
            s = game.GameScreen.getButtons().get(CLOSE_BUTTON_TYPE);
            renderSprite(g, s);
            
            if (game.GameScreen.loss)
            {
                s = game.GameScreen.getButtons().get(RETRY_BUTTON_TYPE);
                renderSprite(g,s);
                
                g.setFont(FONT_STATS);
                g.setColor(Color.red);
                String message = "You got caught!";
                g.drawString(message, 120, 130);
                message = "You lost " + game.GameScreen.moneyLost + " dollars!";
                g.drawString(message, 120, 160);
                message = "Try again!";
                g.drawString(message, 120, 190);
            }
            else if (game.GameScreen.won)
            {
                g.setFont(FONT_STATS);
                g.setColor(Color.BLUE);
                String message = "You got away!";
                g.drawString(message, 120, 130);
                message = "You gained " + game.GameScreen.getLevel().recievedMoney + " dollars!";
                g.drawString(message, 120, 160);
                message = "Continue to the next level!";
                g.drawString(message, 120, 190);
            }
            

        }
    }
    
    /**
     * Renders the s Sprite into the Graphics context g. Note
     * that each Sprite knows its own x,y coordinate location.
     * 
     * @param g the Graphics context of this panel
     * 
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
    }
    
        // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics2D g2)
    {
        Sprite s = ((GameScreen) game.getCurrentScreen()).getBackgroundSprite();
        renderSprite(g2, s);
    }
    
     // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2)
    {
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        Iterator<Road> it = ((GameScreen) game.getCurrentScreen()).getLevel().getRoads().iterator();
        
        g2.setStroke(recyclableStrokes.get(INT_STROKE));

        while (it.hasNext())
        {
            Road road = it.next();
            if (!road.closed)
                renderRoad(g2, road, INT_OUTLINE_COLOR);
            else
                renderRoad(g2, road, Color.red);
        }
    }
    
    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c)
    {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit()/10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x-data.getViewport().getViewportX();
        recyclableLine.y1 = road.getNode1().y-data.getViewport().getViewportY();
        recyclableLine.x2 = road.getNode2().x-data.getViewport().getViewportX();
        recyclableLine.y2 = road.getNode2().y-data.getViewport().getViewportY();

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay())
        {
            renderOneWaySignalsOnRecyclableLine(g2);
        }
    }
    
        
    // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
    }
    
    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2)
    {
        Iterator<Sprite> sIt = ((GameScreen) game.getCurrentScreen()).getIntersections().iterator();
        
        while (sIt.hasNext())
        {
            Sprite s = sIt.next();
            renderSprite(g2, s);
        }
        
        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        
        Image startImage = ((GameScreen) game.getCurrentScreen()).getStartingLocationImage();
        Intersection startInt = ((GameScreen) game.getCurrentScreen()).getLevel().getStartingLocation();
        renderIntersectionImage(g2, startImage, startInt);

        Image destImage = ((GameScreen) game.getCurrentScreen()).getDestinationLocationImage();
        Intersection destInt = ((GameScreen) game.getCurrentScreen()).getLevel().getDestination();
        renderIntersectionImage(g2, destImage, destInt);
                
    }
    
    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i)
    {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x-(w/2);
        int y1 = i.y-(h/2);
        
        g2.drawImage(img, x1 - data.getViewport().getViewportX(), y1 - data.getViewport().getViewportY(), null);     
    }

    private void renderSprites(Graphics2D g2)
    {
        Sprite s = ((GameScreen) game.getCurrentScreen()).getPlayerSprite();
        renderSprite(g2, s);
        
        Iterator<PathXPolice> pxpIt = ((GameScreen) game.getCurrentScreen()).getPolice().iterator();
        while (pxpIt.hasNext())
        {
            s = pxpIt.next().getSprite();
            renderSprite(g2, s);
        }
        
        Iterator<PathXBandit> pxbIt = ((GameScreen) game.getCurrentScreen()).getBandits().iterator();
        while (pxbIt.hasNext())
        {
            s = pxbIt.next().getSprite();
            renderSprite(g2, s);
        }
        
        Iterator<PathXZombie> pxzIt = ((GameScreen) game.getCurrentScreen()).getZombies().iterator();
        while (pxzIt.hasNext())
        {
            s = pxzIt.next().getSprite();
            renderSprite(g2, s);
        }
    }
}