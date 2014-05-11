/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXData;

import PathX.PathX;
import static PathX.PathXConstants.*;

import PathX.PathXGame;
import PathXGraph.Road;
import PathXScreens.LevelSelectScreen;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.TreeMap;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Viewport;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class PathXDataModel extends MiniGameDataModel
{
    private PathXGame game;
    
    private TreeMap<String, GameLevel> levels;
    
    public int balance;
    private final int goal = 100000;
    
    public int currentLevel = 1;
    
    public float gameSpeed = 1;
    public boolean muteMusic = false;
    public boolean muteSound = false;

    
    public PathXDataModel(PathXGame game)
    {
        this.game = game;
        
        levels = new TreeMap<String, GameLevel>();
        
        //FIRST SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE1, new GameLevel("Lemonade Stand Circuit 1", 20, LEVEL_BUTTON_TYPE1, GameLevel.GameLevelState.UNLOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL1), 1, 0));
        
        levels.put(LEVEL_BUTTON_TYPE2, new GameLevel("Lemonade Stand Circuit 2", 40, LEVEL_BUTTON_TYPE2, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL2), 2, 1));
        
        levels.put(LEVEL_BUTTON_TYPE3, new GameLevel("Lemonade Stand Circuit 3", 60, LEVEL_BUTTON_TYPE3, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL3), 3, 2));
        
        //SECOND SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE4, new GameLevel("Silicon Valley Circuit 1", 80, LEVEL_BUTTON_TYPE4, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL4), 4, 3));
        
        levels.put(LEVEL_BUTTON_TYPE5, new GameLevel("Silicon Valley Circuit 2", 100, LEVEL_BUTTON_TYPE5, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL5), 5, 4));
        
        levels.put(LEVEL_BUTTON_TYPE6, new GameLevel("Silicon Valley Circuit 3", 120, LEVEL_BUTTON_TYPE6, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL6), 6, 5));
        
        //THIRD SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE7, new GameLevel("Nevada Casino Circuit 1", 140, LEVEL_BUTTON_TYPE7, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL7), 7, 6));
        
        levels.put(LEVEL_BUTTON_TYPE8, new GameLevel("Nevada Casino Circuit 2", 160, LEVEL_BUTTON_TYPE8, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL8), 8, 7));
        
        levels.put(LEVEL_BUTTON_TYPE9, new GameLevel("Nevada Casino Circuit 3", 180, LEVEL_BUTTON_TYPE9, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL9), 9, 8));
        
        //FOURTH SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE10, new GameLevel("Old West Bank Circuit 1", 200, LEVEL_BUTTON_TYPE10, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL10), 10, 9));
              
        levels.put(LEVEL_BUTTON_TYPE11, new GameLevel("Old West Bank Circuit 2", 220, LEVEL_BUTTON_TYPE11, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL11), 11, 10));

        levels.put(LEVEL_BUTTON_TYPE12, new GameLevel("Old West Bank Circuit 3", 240, LEVEL_BUTTON_TYPE12, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL12), 12, 11));
        
        //FIFTH SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE13, new GameLevel("Florida Retirees Circuit 1", 260, LEVEL_BUTTON_TYPE13, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL13), 13, 12));
        
        levels.put(LEVEL_BUTTON_TYPE14, new GameLevel("Florida Retirees Circuit 2", 280, LEVEL_BUTTON_TYPE14, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL14), 14, 13));
        
        levels.put(LEVEL_BUTTON_TYPE15, new GameLevel("Florida Retirees Circuit 3", 300, LEVEL_BUTTON_TYPE15, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL15), 15, 14));
        
        //SIXTH SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE16, new GameLevel("I-95 Corridor Circuit 1", 320, LEVEL_BUTTON_TYPE16, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL16), 16, 15));
        
        levels.put(LEVEL_BUTTON_TYPE17, new GameLevel("I-95 Corridor Circuit 2", 340, LEVEL_BUTTON_TYPE17, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL17), 17, 16));
        
        levels.put(LEVEL_BUTTON_TYPE18, new GameLevel("I-95 Corridor Circuit 3", 360, LEVEL_BUTTON_TYPE18, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL18), 18, 17));
        
        //SEVENT SEQUENCE
        levels.put(LEVEL_BUTTON_TYPE19, new GameLevel("Wall Street Circuit 1", 380, LEVEL_BUTTON_TYPE19, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL19), 19, 18));
        
        levels.put(LEVEL_BUTTON_TYPE20, new GameLevel("Wall Street Circuit 2", 400, LEVEL_BUTTON_TYPE20, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL20), 20, 19));
        
        levels.put(LEVEL_BUTTON_TYPE21, new GameLevel("Wall Street Circuit 3", 420, LEVEL_BUTTON_TYPE21, GameLevel.GameLevelState.LOCKED_STATE.toString(),
                PATH_LEVELS + PropertiesManager.getPropertiesManager().getProperty(PathX.PathXPropertyType.LEVEL21), 21, 20));
    }
    
    public void unlockLevel()
    {
        if (currentLevel == 22) return;
       
        GameLevel temp = levels.get("LEVEL_BUTTON_TYPE" + currentLevel);
        temp.setState(GameLevel.GameLevelState.COMPLETED_STATE.toString());
        
        currentLevel++;
        if (currentLevel == 22) return;
        temp = levels.get("LEVEL_BUTTON_TYPE" + currentLevel);
        temp.setState(GameLevel.GameLevelState.UNLOCKED_STATE.toString());
    }
    
    public void updateLevels(LevelSelectScreen screen)
    {
        if (currentLevel == 21) return;
        
        currentLevel++;
        GameLevel temp = levels.get("LEVEL_BUTTON_TYPE" + currentLevel);
        temp.setState(GameLevel.GameLevelState.UNLOCKED_STATE.toString());
        
        screen.unloadLevels();
        screen.loadLevels();
    }
    
    public void updateMoney(int amount)
    {
        balance += amount;
    }
    
    @Override
    public void setViewport(Viewport initViewport)
    {
        viewport = initViewport;
    }
    
    @Override
    public Viewport getViewport()
    {
        return viewport;
    }
    

    @Override
    public void checkMousePressOnSprites(MiniGame g, int x, int y)
    {
        if (game.getCurrentScreen() != game.GameScreen) return;
        
        Iterator<Road> it = game.GameScreen.getLevel().getRoads().iterator();
        Line2D.Double tempLine = new Line2D.Double();
        while (it.hasNext())
        {
            Road r = it.next();
 
            tempLine.x1 = r.getNode1().x;
            tempLine.y1 = r.getNode1().y;
            tempLine.x2 = r.getNode2().x;
            tempLine.y2 = r.getNode2().y;

            double distance = tempLine.ptSegDist(x + viewport.getViewportX(), y+viewport.getViewportY());
            
            // IS IT CLOSE ENOUGH?
            if (distance <= INT_STROKE)
            {
                game.GameScreen.roadClicked(r);
            }
        }
    }

    @Override
    public void reset(MiniGame game)
    {
        balance = 0;
        Iterator<GameLevel> gIt = levels.values().iterator();
        while (gIt.hasNext())
        {
            gIt.next().setState(GameLevel.GameLevelState.LOCKED_STATE.toString());
        }
        levels.get(LEVEL_BUTTON_TYPE1).setState(GameLevel.GameLevelState.UNLOCKED_STATE.toString());
    }

    @Override
    public void updateAll(MiniGame game) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDebugText(MiniGame game) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getBalance(){ return balance; }
    public int getGoal(){ return goal; }
    public TreeMap<String, GameLevel> getLevels(){ return levels; }
    
    
    
}
