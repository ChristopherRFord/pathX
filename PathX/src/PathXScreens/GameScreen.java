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
import PathXData.PathXLevelLoader;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class GameScreen extends PathXScreen
{
    private GameLevel level;
    
    private Sprite backgroundSprite;
    private Image startingLocationImage;
    private Image destinationLocationImage;
    
    public GameScreen(PathXGame game)
    {
        super(game);
        screenType = GAME_SCREEN_STATE;
    }

    @Override
    public void initAudioContent() {}

    @Override
    public void initData(PathXDataModel data) { this.data = data; }

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
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, GAME_SCREEN_STATE);
        decors.put(BACKGROUND_TYPE, s);
        
        
        // THE EXIT BUTTON
        String exitButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EXIT);
        sT = new SpriteType(EXIT_BUTTON_TYPE);
	img = game.loadImage(imgPath + exitButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String exitMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EXIT_MOUSE_OVER);
        img = game.loadImage(imgPath + exitMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 0, 60, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(EXIT_BUTTON_TYPE, s);
        
        // THE HOME BUTTON
        String homeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(EXIT_BUTTON_TYPE);
	img = game.loadImage(imgPath + homeButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = game.loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 80, 60, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(HOME_BUTTON_TYPE, s);
        
         // THE HOME BUTTON
        String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START);
        sT = new SpriteType(START_BUTTON_TYPE);
	img = game.loadImage(imgPath + playButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String playButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START_MOUSE_OVER);
        img = game.loadImage(imgPath + playButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 22, 130, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(START_BUTTON_TYPE, s);
        
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
        buttons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.LevelSelectScreen);
            }
        });
        
        buttons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.MainMenuScreen);
            }
        });
        
                // ARROW UP
        buttons.get(UP_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                scroll(0, VIEWPORT_INC);
            }
        });
        
        // ARROW RIGHT
        buttons.get(RIGHT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   
                scroll(VIEWPORT_INC, 0);
            }
        });
        
        // ARROW LEFT
        buttons.get(LEFT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {   
                scroll(-VIEWPORT_INC, 0);
            }
        });   
        
        // ARROW DOWN
        buttons.get(DOWN_ARROW_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {    
                scroll(0, -VIEWPORT_INC);
            }
        }); 
    }
    
    private void scroll(int x, int y)
    {
        data.getViewport().scroll(x, y);
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
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        game.setKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke)
            {   
                respondToKeyPress(ke.getKeyCode());    
            }
        });
        
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG); 
        
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_DIALOG_BOX));
        sT = new SpriteType(DIALOG_BOX_TYPE);
        sT.addState(PathXSpriteState.VISIBLE_STATE.toString(), img);
        s = new Sprite(sT, 100, 40, 0, 0, PathXSpriteState.VISIBLE_STATE.toString());
        decors.put(DIALOG_BOX_TYPE, s);
        
        // THE HOME BUTTON
        String closeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
	img = game.loadImage(imgPath + closeButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String closeButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = game.loadImage(imgPath + closeButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 250, 300, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(CLOSE_BUTTON_TYPE, s);
        
        buttons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                decors.remove(DIALOG_BOX_TYPE);
                buttons.remove(CLOSE_BUTTON_TYPE);
            }
        });
        
        PathXLevelLoader levelLoader = new PathXLevelLoader(new File(PATH_LEVELS + LEVEL_SCHEMA_FILE_NAME));
        levelLoader.loadLevel(level);
        
         // LOAD THE BACKGROUND
        img = game.loadImage(imgPath + "path_x/" + level.getBackgroundImageFileName());
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(GAME_SCREEN_STATE, img);
        backgroundSprite = new Sprite(sT, 0, 0, 0, 0, GAME_SCREEN_STATE);
        
        // LOAD THE STARTING IMG
        startingLocationImage = game.loadImage(imgPath + "path_x/" + level.getStartingLocationImageFileName());

  
        // LOAD THE DEST IMG
        destinationLocationImage = game.loadImage(imgPath + "path_x/" + level.getDestinationImageFileName());

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
        
        data.getViewport().scroll(-(data.getViewport().getViewportX()),
                                    -(data.getViewport().getViewportY()));
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        game.setKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){}});
    }
    
    public void setGameLevel(GameLevel level) { this.level = level; }
    public GameLevel getLevel() { return level; }
    
    public Sprite getBackgroundSprite()             { return backgroundSprite;          }
    public Image getStartingLocationImage()       { return startingLocationImage;    }
    public Image getDestinationLocationImage()    { return destinationLocationImage; }
    
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    {
        // WASD MOVES THE VIEWPORT
        if (keyCode == KeyEvent.VK_DOWN)
        {
                scroll(0, -VIEWPORT_INC);
        }
        else if (keyCode == KeyEvent.VK_RIGHT)
        {
                scroll(-VIEWPORT_INC, 0);
        }
        else if (keyCode == KeyEvent.VK_UP)
        {
                scroll(0, VIEWPORT_INC);
        }
        else if (keyCode == KeyEvent.VK_LEFT)
        {
                scroll(VIEWPORT_INC, 0);
        }
    }
}
