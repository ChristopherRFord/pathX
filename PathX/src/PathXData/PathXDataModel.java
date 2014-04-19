/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXData;

import static PathX.PathXConstants.*;

import PathX.PathXGame;
import java.util.ArrayList;
import java.util.TreeMap;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Viewport;

/**
 *
 * @author Chris
 */
public class PathXDataModel extends MiniGameDataModel
{
    private PathXGame game;
    
    private TreeMap<String, GameLevel> levels;
    
    private int balance;
    private final int goal = 100000;
    
    public PathXDataModel(PathXGame game)
    {
        this.game = game;
        
        levels = new TreeMap<String, GameLevel>();
        levels.put(LEVEL_BUTTON_TYPE1, new GameLevel("South1,CA", 20, GameLevel.GameLevelState.UNLOCKED_STATE.toString()));
        levels.put(LEVEL_BUTTON_TYPE2, new GameLevel("South2,CA", 40, GameLevel.GameLevelState.LOCKED_STATE.toString()));
        levels.put(LEVEL_BUTTON_TYPE3, new GameLevel("South3,CA", 60, GameLevel.GameLevelState.LOCKED_STATE.toString()));
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
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset(MiniGame game) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void addBalance(int money){ balance += balance; }
    public void subtractBalance(int money){ balance -= balance; }
    public int getGoal(){ return goal; }
    public TreeMap<String, GameLevel> getLevels(){ return levels; }
    
}
