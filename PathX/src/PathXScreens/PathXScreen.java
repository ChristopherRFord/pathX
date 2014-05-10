/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXScreens;

import PathX.PathXGame;
import PathX.PathX.*;
import PathXData.PathXDataModel;
import java.util.Iterator;
import java.util.TreeMap;
import mini_game.Sprite;

/**
 *
 * @author Chris
 */
public abstract class PathXScreen
{
    protected PathXGame game;
    protected PathXDataModel data;
    
    protected String screenType;
    
    protected TreeMap<String, Sprite> buttons;
    protected TreeMap<String, Sprite> decors;
    protected TreeMap<String, Sprite> dialogs;
    
    public PathXScreen(PathXGame game)
    {
        this.game = game;
        
        buttons = new TreeMap<String, Sprite>();
        decors = new TreeMap<String, Sprite>();
    }
    
    public String getScreentype(){ return screenType; }
    
    public TreeMap<String, Sprite> getButtons(){ return buttons; }
    public TreeMap<String, Sprite> getDecors(){ return decors; }
    
    public abstract void initAudioContent();
    public abstract void initData(PathXDataModel data);
    public abstract void initGUIControls();
    public abstract void initGUIHandlers(); 
    public void updateGUI()
    {
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
         try
         {
                // ARE WE ENTERING A BUTTON?
                if (button.getState().equals(PathXButtonState.VISIBLE_STATE.toString()))
                {

                    if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                    {
                        button.setState(PathXButtonState.MOUSE_OVER_STATE.toString());
   
                    }

                }
                // ARE WE EXITING A BUTTON?
                else if (button.getState().equals(PathXButtonState.MOUSE_OVER_STATE.toString()))
                {
                    if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                    {
                        button.setState(PathXButtonState.VISIBLE_STATE.toString());
                    }
                }
            }    
        catch (NullPointerException e)
                {}
         
        }
    }

    
    public abstract void enter();
    public abstract void leave();
}
