/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXScreens;

import static PathX.PathX.*;
import static PathX.PathXConstants.*;
import PathX.PathXGame;
import PathXData.PathXDataModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class HelpScreen extends PathXScreen
{  
    public HelpScreen(PathXGame game)
    {
        super(game);
        screenType = HELP_SCREEN_STATE;
    }

    @Override
    public void initAudioContent()
    {
       
    }

    @Override
    public void initData(PathXDataModel data)
    {
        this.data = data;
    }

    @Override
    public void initGUIControls()
    {
         // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);   
        
         // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(HELP_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, HELP_SCREEN_STATE);
        decors.put(BACKGROUND_TYPE, s);
        
        // THE EXIT BUTTON
        String exitButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EXIT);
        sT = new SpriteType(EXIT_BUTTON_TYPE);
	img = game.loadImage(imgPath + exitButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String exitMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EXIT_MOUSE_OVER);
        img = game.loadImage(imgPath + exitMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EXIT_BUTTON_X, EXIT_BUTTON_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(EXIT_BUTTON_TYPE, s);
        
        // THE HOME BUTTON
        String homeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(EXIT_BUTTON_TYPE);
	img = game.loadImage(imgPath + homeButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = game.loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(HOME_BUTTON_TYPE, s);
    }

    @Override
    public void initGUIHandlers()
    {
        buttons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   System.exit(0);     }
        });
        
        buttons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   game.enter(game.MainMenuScreen);     }
        });
    }

    @Override
    public void enter()
    {
        game.setGUIButtons(buttons);
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            button.setState(PathXButtonState.VISIBLE_STATE.toString());
            button.setEnabled(true);
        }
    }

    @Override
    public void leave()
    {
        game.setGUIButtons(null);
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            button.setState(PathXButtonState.INVISIBLE_STATE.toString());
            button.setEnabled(false);
        }
    }
    
}
