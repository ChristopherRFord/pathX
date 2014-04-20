package PathX;

import java.awt.Color;
import java.awt.Font;

/**
 * Contains constants for the UI positions and types.
 * 
 * @author Christopher Ford
 */
public class PathXConstants
{
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    public static String PATH_LEVELS = "./levels/";
    public static String LEVEL_SCHEMA_FILE_NAME = "PathXLevelSchema.xsd";
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    //MAIN MENU SCREEN BUTTONS
    public static final String EXIT_BUTTON_TYPE = "EXIT_BUTTON_TYPE";
    public static final String HOME_BUTTON_TYPE = "HOME_BUTTON_TYPE";
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_BUTTON_TYPE";
    public static final String SETTINGS_BUTTON_TYPE = "SETTINGS_BUTTON_TYPE"; 
    
    //SETTINGS SCREEN BUTTONS
    public static final String MUSIC_BUTTON_TYPE = "MUSIC_BUTTON_TYPE";
    public static final String SOUND_BUTTON_TYPE = "SOUND_BUTTON_TYPE";
    
    //LEVEL SELECT SCREEN BUTTONS
    public static final String LEVEL_SELECT_MAP_TYPE = "LEVEL_SELECT_MAP_TYPE";
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    public static final String UP_ARROW_BUTTON_TYPE = "UP_ARROW_BUTTON_TYPE";
    public static final String RIGHT_ARROW_BUTTON_TYPE = "RIGHT_ARROW_BUTTON_TYPE";
    public static final String LEFT_ARROW_BUTTON_TYPE = "LEFT_ARROW_BUTTON_TYPE";
    public static final String DOWN_ARROW_BUTTON_TYPE = "DOWN_ARROW_BUTTON_TYPE";
    
    //LEVEL SCREEN BUTTONS
    public static final String LEVEL_BUTTON_TYPE1 = "LEVEL_BUTTON_TYPE1";
    public static final String LEVEL_BUTTON_TYPE2 = "LEVEL_BUTTON_TYPE2";
    public static final String LEVEL_BUTTON_TYPE3 = "LEVEL_BUTTON_TYPE3";
    
    //GAME SCREEN BUTTONS
    public static final String DIALOG_BOX_TYPE = "DIALOG_BOX_TYPE";
    public static final String CLOSE_BUTTON_TYPE = "CLOSE_BUTTON_TYPE";
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    
    public static final String STARTING_LOCATION_TYPE = "STARTING_LOCATION_TYPE";
    public static final String DESTINATION_LOCATION_TYPE = "DESTINATION_LOCATION_TYPE";
    
     // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";
    public static final String START_NODE = "start_intersection";
    public static final String DEST_NODE = "destination_intersection";

    // AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";

    
    // RENDERING SETTINGS
    public static final int INTERSECTION_RADIUS = 20;
    public static final int INT_STROKE = 3;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 40;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 60;
    
    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;
    
    
    // ANIMATION SPEED
    public static final int FPS = 30;

    
    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;
    public static final int VIEWPORT_INC = 5;
    
    // UI CONTROLS POSITIONS IN THE MAIN MENU SCREEN
    public static final int CONTROLS_MARGIN = 0;
    public static final int EXIT_BUTTON_X = 576;
    public static final int EXIT_BUTTON_Y = 0;
    public static final int HOME_BUTTON_X = EXIT_BUTTON_X-70;
    public static final int HOME_BUTTON_Y = 0;
    public static final int PLAY_BUTTON_X = 50;
    public static final int PLAY_BUTTON_Y = 360;
    public static final int RESET_BUTTON_X = 10 + PLAY_BUTTON_X + 128;
    public static final int RESET_BUTTON_Y = 360;
    public static final int SETTINGS_BUTTON_X = 10 + RESET_BUTTON_X + 128;
    public static final int SETTINGS_BUTTON_Y = 360;
    public static final int HELP_BUTTON_X = 10 + SETTINGS_BUTTON_X + 128;
    public static final int HELP_BUTTON_Y = 360;
    
    //MIN AND MAX POSITION OF THE MAP
    public static final int MAP_MIN_X = -680;
    public static final int MAP_MAX_X = 0;
    public static final int MAP_MIN_Y = -210;
    public static final int MAP_MAX_Y = 290;
    
    //ARROW LOCATION ON LEVEL SELECTION SCREEN
    public static final int UP_ARROW_X = 32;
    public static final int UP_ARROW_Y = 360;
    public static final int RIGHT_ARROW_X = 64;
    public static final int RIGHT_ARROW_Y = 392;
    public static final int LEFT_ARROW_X = 0;
    public static final int LEFT_ARROW_Y = 392;
    public static final int DOWN_ARROW_X = 32;
    public static final int DOWN_ARROW_Y = 424;
    
    public static final Font FONT_STATS = new Font(Font.SANS_SERIF, Font.BOLD, 30);
}
