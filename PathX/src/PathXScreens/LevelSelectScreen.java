/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXScreens;

import static PathX.PathX.*;
import static PathX.PathXConstants.*;
import PathX.PathXGame;
import PathXData.GameLevel;
import PathXData.PathXDataModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class LevelSelectScreen extends PathXScreen
{
    private GameLevel selectedLevel;

    
    public LevelSelectScreen(PathXGame game)
    {
        super(game);
        
        screenType = LEVEL_SELECT_SCREEN_STATE;
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
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, LEVEL_SELECT_SCREEN_STATE);
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
        
        // THE MAP
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_SELECT_MAP));
        sT = new SpriteType(LEVEL_SELECT_MAP_TYPE);
        sT.addState(PathXSpriteState.INVISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 0, -180, 0, 0, PathXSpriteState.INVISIBLE_STATE.toString());
        decors.put(LEVEL_SELECT_MAP_TYPE, s);
        
        // THE UP ARROW BUTTON
        String upButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UP_ARROW);
        sT = new SpriteType(UP_ARROW_BUTTON_TYPE);
	img = game.loadImage(imgPath + upButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String upMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UP_ARROW_MOUSE_OVER);
        img = game.loadImage(imgPath + upMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_ARROW_X, UP_ARROW_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(UP_ARROW_BUTTON_TYPE, s);
        
        // THE LEFT ARROW BUTTON
        String rightButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RIGHT_ARROW);
        sT = new SpriteType(RIGHT_ARROW_BUTTON_TYPE);
	img = game.loadImage(imgPath + rightButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String rightMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RIGHT_ARROW_MOUSE_OVER);
        img = game.loadImage(imgPath + rightMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_ARROW_X, RIGHT_ARROW_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(RIGHT_ARROW_BUTTON_TYPE, s);
        
        // THE LEFT ARROW BUTTON
        String leftButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEFT_ARROW);
        sT = new SpriteType(LEFT_ARROW_BUTTON_TYPE);
	img = game.loadImage(imgPath + leftButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String leftMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEFT_ARROW_MOUSE_OVER);
        img = game.loadImage(imgPath + leftMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_ARROW_X, LEFT_ARROW_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(LEFT_ARROW_BUTTON_TYPE, s);
        
        // THE DOWN ARROW BUTTON
        String downButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DOWN_ARROW);
        sT = new SpriteType(DOWN_ARROW_BUTTON_TYPE);
	img = game.loadImage(imgPath + downButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String downMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DOWN_ARROW_MOUSE_OVER);
        img = game.loadImage(imgPath + downMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_ARROW_X, DOWN_ARROW_Y, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(DOWN_ARROW_BUTTON_TYPE, s);
    }

    @Override
    public void initGUIHandlers()
    {
        // EXIT BUTTON
        buttons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   System.exit(0);    }
        });
        
        // HOME BUTTON
        buttons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   game.enter(game.MainMenuScreen);     }
        });
        
        // ARROW UP
        buttons.get(UP_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                if (data.getViewport().getViewportY() < MAP_MAX_Y) scroll(0, VIEWPORT_INC);
            }
        });
        
        // ARROW RIGHT
        buttons.get(RIGHT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   
                if (data.getViewport().getViewportX() > MAP_MIN_X) scroll(-VIEWPORT_INC, 0);
            }
        });
        
        // ARROW LEFT
        buttons.get(LEFT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   
                if (data.getViewport().getViewportX() < MAP_MAX_X) scroll(VIEWPORT_INC, 0);
            }
        });   
        
        // ARROW DOWN
        buttons.get(DOWN_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                if (data.getViewport().getViewportY() > MAP_MIN_Y) scroll(0, -VIEWPORT_INC);
            }
        }); 


        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        game.setKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke)
            {   
                respondToKeyPress(ke.getKeyCode());    
            }
        });
    }
    
    private void scroll(int x, int y)
    {
        data.getViewport().scroll(x, y);
        decors.get(LEVEL_SELECT_MAP_TYPE).setX(decors.get(LEVEL_SELECT_MAP_TYPE).getX() + x);
        decors.get(LEVEL_SELECT_MAP_TYPE).setY(decors.get(LEVEL_SELECT_MAP_TYPE).getY() + y);
        
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            if(button.getSpriteType().getSpriteTypeID().contains("LEVEL_BUTTON_TYPE"))
            {
                button.setX(button.getX() + x);
                button.setY(button.getY() + y);
            }

        }
 
    }
    
    @Override
    public void updateGUI()
    {
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
        
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXButtonState.VISIBLE_STATE.toString()))
            {

                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXButtonState.MOUSE_OVER_STATE.toString());
                    
                    if (button.getSpriteType().getSpriteTypeID().contains("LEVEL_BUTTON_TYPE"))
                        selectedLevel = data.getLevels().get(button.getSpriteType().getSpriteTypeID());
                }

            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXButtonState.MOUSE_OVER_STATE.toString()))
            {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXButtonState.VISIBLE_STATE.toString());
                    
                    selectedLevel = null;
                }
            }
        }
    }

    @Override
    public void enter()
    {
        game.setGUIButtons(buttons);
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        decors.get(LEVEL_SELECT_MAP_TYPE).setX(0);
        decors.get(LEVEL_SELECT_MAP_TYPE).setY(-180);
        
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            button.setState(PathXButtonState.VISIBLE_STATE.toString());
            button.setEnabled(true);
        }
        
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);

        String levelButton = data.getLevels().get(LEVEL_BUTTON_TYPE1).getProperty();
        sT = new SpriteType(LEVEL_BUTTON_TYPE1);
        img = game.loadImage(imgPath + levelButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String levelButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEVEL_MOUSE_OVER);
        img = game.loadImage(imgPath + levelButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 5, 150, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(LEVEL_BUTTON_TYPE1, s);
        
        buttons.get(LEVEL_BUTTON_TYPE1).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (data.getLevels().get(LEVEL_BUTTON_TYPE1).getState().equals(GameLevel.GameLevelState.UNLOCKED_STATE.toString()))
                {
                    game.GameScreen.setGameLevel(data.getLevels().get(LEVEL_BUTTON_TYPE1));
                    game.enter(game.GameScreen);
                }
            }
        });
        
        levelButton = data.getLevels().get(LEVEL_BUTTON_TYPE2).getProperty();
        sT = new SpriteType(LEVEL_BUTTON_TYPE2);
        img = game.loadImage(imgPath + levelButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        levelButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEVEL_MOUSE_OVER);
        img = game.loadImage(imgPath + levelButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 50, 265, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(LEVEL_BUTTON_TYPE2, s);
        
        buttons.get(LEVEL_BUTTON_TYPE2).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                if (data.getLevels().get(LEVEL_BUTTON_TYPE2).getState().equals(GameLevel.GameLevelState.UNLOCKED_STATE.toString()))
                {
                    game.GameScreen.setGameLevel(data.getLevels().get(LEVEL_BUTTON_TYPE2));
                    game.enter(game.GameScreen);
                }
            }
        }); 
        /*
        levelButton = data.getLevels().get(LEVEL_BUTTON_TYPE3).getProperty();
        sT = new SpriteType(LEVEL_BUTTON_TYPE3);
        img = game.loadImage(imgPath + levelButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        levelButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEVEL_MOUSE_OVER);
        img = game.loadImage(imgPath + levelButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 100, 320, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(LEVEL_BUTTON_TYPE3, s);
        
        buttons.get(LEVEL_BUTTON_TYPE3).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                if (data.getLevels().get(LEVEL_BUTTON_TYPE3).getState().equals(GameLevel.GameLevelState.UNLOCKED_STATE.toString()))
                {
                    game.GameScreen.setGameLevel(data.getLevels().get(LEVEL_BUTTON_TYPE3));
                    game.enter(game.GameScreen);
                }
            }
        }); 
        */
    }

    @Override
    public void leave()
    {
        game.setGUIButtons(null);

        
        buttons.remove(buttons.get(LEVEL_BUTTON_TYPE1).getSpriteType().getSpriteTypeID().toString());
        buttons.remove(buttons.get(LEVEL_BUTTON_TYPE2).getSpriteType().getSpriteTypeID().toString());
//        buttons.remove(buttons.get(LEVEL_BUTTON_TYPE3).getSpriteType().getSpriteTypeID().toString());
        
        Iterator<Sprite> buttonsIt = buttons.values().iterator();
         
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            button.setState(PathXButtonState.INVISIBLE_STATE.toString());
            button.setEnabled(false);
        }
    }
    
     /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    {
        // WASD MOVES THE VIEWPORT
        if (keyCode == KeyEvent.VK_DOWN)
        {
                if (data.getViewport().getViewportY() > MAP_MIN_Y) scroll(0, -VIEWPORT_INC);
        }
        else if (keyCode == KeyEvent.VK_RIGHT)
        {
                if (data.getViewport().getViewportX() > MAP_MIN_X) scroll(-VIEWPORT_INC, 0);
        }
        else if (keyCode == KeyEvent.VK_UP)
        {
                if (data.getViewport().getViewportY() < MAP_MAX_Y) scroll(0, VIEWPORT_INC);
        }
        else if (keyCode == KeyEvent.VK_LEFT)
        {
                if (data.getViewport().getViewportX() < MAP_MAX_X) scroll(VIEWPORT_INC, 0);
        }
    }
    
    public GameLevel getSelectedLevel() { return selectedLevel; }
}
