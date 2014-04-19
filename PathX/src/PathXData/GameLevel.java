package PathXData;

import static PathXData.GameLevel.GameLevelState.*;

/**
 * Contains all the information that defines a Game Level 
 * @author Christopher Ford
 */
public class GameLevel
{
    private String name;
    private int amount;
    private String state;
    
    public GameLevel(String name, int amount, String state)
    {
        this.name = name;
        this.amount = amount;
        this.state = state;
    }
    
    public String getName(){ return name; }
    public int getamount(){ return amount; }
    public String getState(){ return state; }
    public void setState(String state){ this.state = state; }
    
    public String getProperty()
    {
        if (state.equals(UNLOCKED_STATE.toString()))
            return "./path_x/ButtonLevelUnlocked.png";
        if (state.equals(LOCKED_STATE.toString()))
            return "./path_x/ButtonLevelLocked.png";
        if (state.equals(COMPLETED_STATE.toString()))
            return "./path_x/ButtonLevelCompleted.png";
        else
            return "";
    }
    
    public static enum GameLevelState
    {
        UNLOCKED_STATE,
        LOCKED_STATE,
        COMPLETED_STATE
    }
}
