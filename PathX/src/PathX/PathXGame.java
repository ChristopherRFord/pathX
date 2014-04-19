package PathX;



import PathXData.PathXDataModel;
import PathXScreens.GameScreen;
import PathXScreens.HelpScreen;
import PathXScreens.LevelSelectScreen;
import PathXScreens.MainMenuScreen;
import PathXScreens.PathXScreen;
import PathXScreens.SettingsScreen;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import mini_game.MiniGame;
import mini_game.Sprite;
import properties_manager.PropertiesManager;

/**
 * Screen controller for pathX game
 * 
 * @author Christopher Ford
 */
public class PathXGame extends MiniGame
{
        
    // THE SCREEN CURRENTLY BEING PLAYED
    private PathXScreen currentScreen;
    
    //Screens
    public MainMenuScreen MainMenuScreen;
    public LevelSelectScreen LevelSelectScreen;
    public GameScreen GameScreen;
    public SettingsScreen SettingsScreen;
    public HelpScreen HelpScreen;
    
    
    /**
     * Default Constructor
     * 
     * creates the screens
     */
    public PathXGame()
    {
        super();

        MainMenuScreen = new MainMenuScreen(this);
        LevelSelectScreen = new LevelSelectScreen(this);
        GameScreen = new GameScreen(this);
        SettingsScreen = new SettingsScreen(this);
        HelpScreen = new HelpScreen(this);
    }
    
    /**
     * startGame
     * 
     * Starts the game and enters the mainMenu Screen
     */
    @Override
    public void startGame()
    {
        super.startGame();
        enter(MainMenuScreen);
    }

    /**
     * initData
     * 
     * Initializes the PathXDataModel in every screen
     */
    @Override
    public void initData()
    {
        data = new PathXDataModel(this);
        
        MainMenuScreen.initData((PathXDataModel) data);
        LevelSelectScreen.initData((PathXDataModel) data);
        GameScreen.initData((PathXDataModel) data);
        SettingsScreen.initData((PathXDataModel) data);
        HelpScreen.initData((PathXDataModel) data);
    }

    /**
     * initGUIControls
     * 
     * Creates the icon and initializes the GUI for every screen
     */
    @Override
    public void initGUIControls()
    {
         // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathX.PathXPropertyType.PATH_IMG);        
        String windowIconFile = props.getProperty(PathX.PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel)data); 
        
        // INITILAZING THE GUI FOR THE SCREENS
        MainMenuScreen.initGUIControls();
        LevelSelectScreen.initGUIControls();
        GameScreen.initGUIControls();
        SettingsScreen.initGUIControls();
        HelpScreen.initGUIControls();
    }

    /**
     * initGUIHandlers
     * 
     * Initializes the GUI handlers for every screen
     */
    @Override
    public void initGUIHandlers()
    {
        MainMenuScreen.initGUIHandlers();
        LevelSelectScreen.initGUIHandlers();
        GameScreen.initGUIHandlers();
        SettingsScreen.initGUIHandlers();
        HelpScreen.initGUIHandlers();
    }

    /**
     * reset
     * 
     * Resets the game
     */
    @Override
    public void reset()
    {
        data.reset(this);
    }

    /**
     * updateGUI
     * 
     * Calls updateGUI on the current screen 30 times a second
     */
    @Override
    public void updateGUI()
    {
         if (currentScreen == null)
             return;
         
         currentScreen.updateGUI();
    }
    
    /**
     * enter
     * @param enterScreen - screen to be entered
     * 
     * Leaves the old current screen and enters the new current screen
     */
    public void enter(PathXScreen enterScreen)
    {
         if (currentScreen != null) currentScreen.leave();

        enterScreen.enter();
        currentScreen = enterScreen;
    }
    

    public PathXScreen getCurrentScreen() { return currentScreen; }
    public void setGUIButtons(TreeMap<String, Sprite> buttons){ this.guiButtons = buttons; }
    
    
    @Override
    public void initAudioContent(){}
}