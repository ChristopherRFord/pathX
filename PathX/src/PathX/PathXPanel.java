package PathX;

import static PathX.PathXConstants.*;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import PathXScreens.GameScreen;
import PathXScreens.LevelSelectScreen;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;
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
            
            Collection<Sprite> buttonSprites = ((LevelSelectScreen)game.getCurrentScreen()).getLevelButtons().values();
            
            for (Sprite s : buttonSprites)
            {
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
        
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
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
               g.drawString(level.getName() + "-$" + level.getamount(), 400, 450);
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
        if (game.getCurrentScreen().getDecors().get(DIALOG_BOX_TYPE) != null)
        {
            Sprite s = game.getCurrentScreen().getDecors().get(DIALOG_BOX_TYPE);
            renderSprite(g, s);
            g.setFont(FONT_STATS);
            String name = ((GameScreen) game.getCurrentScreen()).getLevel().getName();
            g.drawString(name, 120, 100);
            s = game.getCurrentScreen().getButtons().get(CLOSE_BUTTON_TYPE);
            renderSprite(g, s);
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
}