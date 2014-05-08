package PathX;

import static PathX.PathXConstants.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * Main class of the game pathX. Inits the Properties manager and starts the game.
 * 
 * @author Christopher Ford
 */
public class PathX
{
    static PathXGame miniGame = new PathXGame();
    
    public static void main(String[] args)
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
        
        try 
        {
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);

            String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);
            String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_GAME);
        
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
            miniGame.startGame();
        
        } catch (InvalidXMLFileFormatException ex)
        {
            Logger.getLogger(PathX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public enum PathXButtonState
    {
        INVISIBLE_STATE,
        VISIBLE_STATE,
        SELECTED_STATE,
        MOUSE_OVER_STATE
    }
    
    public enum PathXSpriteState
    {
        INVISIBLE_STATE,
        VISIBLE_STATE
    }

    public enum PathXPropertyType
    {
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
       
        /* IMAGE FILE NAMES */
        IMAGE_BUTTON_EXIT,
        IMAGE_BUTTON_EXIT_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_HOME_MOUSE_OVER,
        
        //SCREEN BACKGROUNDS
        IMAGE_BACKGROUND_MENU,
        IMAGE_BACKGROUND_LEVEL_SELECT,        
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BACKGROUND_HELP,
        IMAGE_BACKGROUND_GAME,
        
        //MAIN MENU IMAGES
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        
        //SETTINGS IMAGES
        IMAGE_BUTTON_SETTINGS_OPTION,
        IMAGE_BUTTON_SETTINGS_OPTION_SELECTED,
        
        //LEVEL SELECT IMAGES
        IMAGE_LEVEL_SELECT_MAP,
        IMAGE_BUTTON_LEVEL_UNLOCKED,
        IMAGE_BUTTON_LEVEL_LOCKED,
        IMAGE_BUTTON_LEVEL_COMPLETED,
        IMAGE_BUTTON_LEVEL_MOUSE_OVER,
        
        IMAGE_BUTTON_UP_ARROW,
        IMAGE_BUTTON_UP_ARROW_MOUSE_OVER,
        IMAGE_BUTTON_RIGHT_ARROW,
        IMAGE_BUTTON_RIGHT_ARROW_MOUSE_OVER,
        IMAGE_BUTTON_LEFT_ARROW,
        IMAGE_BUTTON_LEFT_ARROW_MOUSE_OVER,
        IMAGE_BUTTON_DOWN_ARROW,
        IMAGE_BUTTON_DOWN_ARROW_MOUSE_OVER,
        IMAGE_BUTTON_PAUSE,
        IMAGE_BUTTON_PAUSE_MOUSE_OVER,
        
        IMAGE_DIALOG_BOX,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_CLOSE_MOUSE_OVER,
        IMAGE_BUTTON_RETRY,
        IMAGE_BUTTON_RETRY_MOUSE_OVER,
        IMAGE_BUTTON_START,
        IMAGE_BUTTON_START_MOUSE_OVER,
        
        IMAGE_PLAYER,
        IMAGE_PLAYER_STEAL,
        IMAGE_PLAYER_INTANGIBILITY,
        
        IMAGE_POLICE,
        IMAGE_POLICE_SELECTED,
        IMAGE_POLICE_MINDLESS,
        
        IMAGE_BANDIT,
        IMAGE_BANDIT_SELECTED,
        IMAGE_BANDIT_MINDLESS,
        
        IMAGE_ZOMBIE,
        IMAGE_ZOMBIE_SELECTED,
        IMAGE_ZOMBIE_MINDLESS,
        
        IMAGE_GREEN_INTERSECTION,
        IMAGE_RED_INTERSECTION,
        IMAGE_CLOSED_INTERSECTION,

        //GAME IMAGES - LEVEL1
        LEVEL1,
        LEVEL2,
        LEVEL3,
        
        LEVEL4,
        LEVEL5,
        LEVEL6,
        
        LEVEL7,
        LEVEL8,
        LEVEL9,
        
        LEVEL10,
        LEVEL11,
        LEVEL12,
        
        LEVEL13,
        LEVEL14,
        LEVEL15,
        
        LEVEL16,
        LEVEL17,
        LEVEL18,
        
        LEVEL19,
        LEVEL20,
        LEVEL21,  
        
        IMAGE_WINDOW_ICON,
        
        /* GAME TEXT */
        TEXT_TITLE_BAR_GAME,
     
    }
}
