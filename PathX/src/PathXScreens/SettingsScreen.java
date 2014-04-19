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
public class SettingsScreen extends PathXScreen
{

    public SettingsScreen(PathXGame game)
    {
        super(game);
        screenType = SETTINGS_SCREEN_STATE;
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
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(SETTINGS_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0,SETTINGS_SCREEN_STATE);
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
        
        // SOUND BUTTON
        String soundButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION);
        sT = new SpriteType(SOUND_BUTTON_TYPE);
        img = game.loadImage(imgPath + soundButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String soundButtonSelected = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION_SELECTED);
        img = game.loadImage(imgPath + soundButtonSelected);
        sT.addState(PathXButtonState.SELECTED_STATE.toString(), img);
        String soundButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION_SELECTED);
        img = game.loadImage(imgPath + soundButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 200, 230, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(SOUND_BUTTON_TYPE, s);
        
        // MUSIC BUTTON
        String musicButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION);
        sT = new SpriteType(MUSIC_BUTTON_TYPE);
        img = game.loadImage(imgPath + musicButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String musicButtonSelected = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION_SELECTED);
        img = game.loadImage(imgPath + musicButtonSelected);
        sT.addState(PathXButtonState.SELECTED_STATE.toString(), img);
        String musicButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_OPTION_SELECTED);
        img = game.loadImage(imgPath + musicButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 200, 270, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(MUSIC_BUTTON_TYPE, s);
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
        
        buttons.get(SOUND_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   if (buttons.get(SOUND_BUTTON_TYPE).getState().toString().equals(PathXButtonState.SELECTED_STATE.toString()))
                    buttons.get(SOUND_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
                else
                    buttons.get(SOUND_BUTTON_TYPE).setState(PathXButtonState.SELECTED_STATE.toString());
                }
        });
        
        buttons.get(MUSIC_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   if (buttons.get(MUSIC_BUTTON_TYPE).getState().toString().equals(PathXButtonState.SELECTED_STATE.toString()))
                    buttons.get(MUSIC_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
                else
                    buttons.get(MUSIC_BUTTON_TYPE).setState(PathXButtonState.SELECTED_STATE.toString());
                }
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
