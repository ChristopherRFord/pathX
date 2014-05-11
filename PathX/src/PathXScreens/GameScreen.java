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
import PathXData.GameLevel.GameLevelState;
import PathXData.PathXDataModel;
import PathXData.PathXLevelLoader;
import PathXGraph.Intersection;
import PathXGraph.PathXBandit;
import PathXGraph.PathXGraphManager;
import PathXGraph.PathXPlayer;
import PathXGraph.PathXPolice;
import PathXGraph.PathXZombie;
import PathXGraph.Road;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
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
    private PathXGraphManager GraphManager;
    private PathXPlayer Player;

    private boolean pause = false;

    private ArrayList<Sprite> Intersections;

    private TreeMap<String, Sprite> powerUpButtons;
    private ArrayList<PathXPolice> police;
    private ArrayList<PathXZombie> zombies;
    private ArrayList<PathXBandit> bandits;

    private Sprite backgroundSprite;
    private Image startingLocationImage;
    private Image destinationLocationImage;

    //power-ups activated
    private boolean greenLightSelected;
    private boolean redLightSelected;

    private boolean decreaseSpeedLimit;
    private boolean increaseSpeedLimit;

    public boolean flatTire;
    public boolean emptyGasTank;

    public boolean closeRoad;
    public boolean closeIntersection;
    public boolean openIntersection;

    public boolean mindControl, mindlessTerror;
    public boolean selected;

    public boolean flying;

    public PathXPolice policeControlled;
    public PathXBandit banditControlled;
    public PathXZombie zombieControlled;

    public String currentPowerUp;

    private boolean gameStarted;
    public boolean loss, won;
    public int moneyLost;

    public GameScreen(PathXGame game)
    {
        super(game);
        screenType = GAME_SCREEN_STATE;
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
        sT = new SpriteType(HOME_BUTTON_TYPE);
        img = game.loadImage(imgPath + homeButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = game.loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 80, 60, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(HOME_BUTTON_TYPE, s);

        // THE START BUTTON
        String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START);
        sT = new SpriteType(START_BUTTON_TYPE);
        img = game.loadImage(imgPath + playButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String playButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START_MOUSE_OVER);
        img = game.loadImage(imgPath + playButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 22, 130, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(START_BUTTON_TYPE, s);

        powerUpButtons = new TreeMap<String, Sprite>();

        /*
         * ---POWER UPS---
         */
        //GREEN LIGHT
        String powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MAKE_LIGHT_GREEN);
        sT = new SpriteType(MAKE_LIGHT_GREEN_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MAKE_LIGHT_GREEN_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 30, 194, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(MAKE_LIGHT_GREEN_TYPE, s);
        powerUpButtons.put(MAKE_LIGHT_GREEN_TYPE, s);

        //RED LIGHT
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MAKE_LIGHT_RED);
        sT = new SpriteType(MAKE_LIGHT_RED_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MAKE_LIGHT_RED_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 60, 194, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(MAKE_LIGHT_RED_TYPE, s);
        powerUpButtons.put(MAKE_LIGHT_RED_TYPE, s);

        //DECREASE SPEED LIMIT
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DECREASE_SPEED_LIMIT);
        sT = new SpriteType(DECREASE_SPEED_LIMIT_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DECREASE_SPEED_LIMIT_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 90, 194, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(DECREASE_SPEED_LIMIT_TYPE, s);
        powerUpButtons.put(DECREASE_SPEED_LIMIT_TYPE, s);

        //INCREASE SPEED LIMIT
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INCREASE_SPEED_LIMIT);
        sT = new SpriteType(INCREASE_SPEED_LIMIT_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INCREASE_SPEED_LIMIT_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 120, 194, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(INCREASE_SPEED_LIMIT_TYPE, s);
        powerUpButtons.put(INCREASE_SPEED_LIMIT_TYPE, s);

        //INCREASE PLAYER SPEED
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INCREASE_PLAYER_SPEED);
        sT = new SpriteType(INCREASE_PLAYER_SPEED_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INCREASE_PLAYER_SPEED_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 30, 224, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(INCREASE_PLAYER_SPEED_TYPE, s);
        powerUpButtons.put(INCREASE_PLAYER_SPEED_TYPE, s);

        //FLAT TIRE
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_FLAT_TIRE);
        sT = new SpriteType(FLAT_TIRE_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_FLAT_TIRE_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 60, 224, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(FLAT_TIRE_TYPE, s);
        powerUpButtons.put(FLAT_TIRE_TYPE, s);

        //EMPTY GAS TANK
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EMPTY_GAS_TANK);
        sT = new SpriteType(EMPTY_GAS_TANK_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_EMPTY_GAS_TANK_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 90, 224, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(EMPTY_GAS_TANK_TYPE, s);
        powerUpButtons.put(EMPTY_GAS_TANK_TYPE, s);

        //CLOSE ROAD
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_ROAD);
        sT = new SpriteType(CLOSE_ROAD_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_ROAD_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 120, 224, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(CLOSE_ROAD_TYPE, s);
        powerUpButtons.put(CLOSE_ROAD_TYPE, s);

        //CLOSE INTERSECTION
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_INTERSECTION);
        sT = new SpriteType(CI_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_INTERSECTION_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 30, 254, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(CI_TYPE, s);
        powerUpButtons.put(CI_TYPE, s);

        //OPEN INTERSECTION
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_OPEN_INTERSECTION);
        sT = new SpriteType(OI_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_OPEN_INTERSECTION_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 60, 254, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(OI_TYPE, s);
        powerUpButtons.put(OI_TYPE, s);

        //STEAL
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_STEAL);
        sT = new SpriteType(STEAL_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_STEAL_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 90, 254, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(STEAL_TYPE, s);
        powerUpButtons.put(STEAL_TYPE, s);

        //MIND CONTROL
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MIND_CONTROL);
        sT = new SpriteType(MIND_CONTROL_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MIND_CONTROL_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 120, 254, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(MIND_CONTROL_TYPE, s);
        powerUpButtons.put(MIND_CONTROL_TYPE, s);

        //INTANGIBILITY
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INTANGIBILITY);
        sT = new SpriteType(INTANGIBILITY_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INTANGIBILITY_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 30, 284, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(INTANGIBILITY_TYPE, s);
        powerUpButtons.put(INTANGIBILITY_TYPE, s);

        //MINDLESS TERROR
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MINDLESS_TERROR);
        sT = new SpriteType(MINDLESS_TERROR_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MINDLESS_TERROR_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 60, 284, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(MINDLESS_TERROR_TYPE, s);
        powerUpButtons.put(MINDLESS_TERROR_TYPE, s);

        //FLYING
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_FLYING);
        sT = new SpriteType(FLYING_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_FLYING_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 90, 284, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(FLYING_TYPE, s);
        powerUpButtons.put(FLYING_TYPE, s);

        //INVINCIBILITY
        powerUpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INVINCIBILITY);
        sT = new SpriteType(INVINCIBILITY_TYPE);
        img = game.loadImage(imgPath + powerUpButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        powerUpButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INVINCIBILITY_MOUSE_OVER);
        img = game.loadImage(imgPath + powerUpButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 120, 284, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(INVINCIBILITY_TYPE, s);
        powerUpButtons.put(INVINCIBILITY_TYPE, s);

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

        // THE PAUSE BUTTON
        String pauseButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE);
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = game.loadImage(imgPath + pauseButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String pauseMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE_MOUSE_OVER);
        img = game.loadImage(imgPath + pauseMouseOverButton);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_ARROW_X, DOWN_ARROW_Y - 32, 0, 0, PathXButtonState.INVISIBLE_STATE.toString());
        buttons.put(PAUSE_BUTTON_TYPE, s);

    }

    @Override
    public void initGUIHandlers()
    {
        buttons.get(START_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                gameStarted = true;
            }
        });
        buttons.get(EXIT_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.LevelSelectScreen);
            }
        });

        buttons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.MainMenuScreen);
            }
        });

        /*
         *   POWER UPS
         */
        //GREEN LIGHT
        buttons.get(MAKE_LIGHT_GREEN_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 1)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 5 > 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    greenLightSelected = true;
                    currentPowerUp = MAKE_LIGHT_GREEN;
                }
            }
        });
        //RED LIGHT
        buttons.get(MAKE_LIGHT_RED_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 2)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 5 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    redLightSelected = true;
                    currentPowerUp = MAKE_LIGHT_RED;
                }
            }
        });

        //DECREASE SPEED LIMIT
        buttons.get(DECREASE_SPEED_LIMIT_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 3)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 15 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    decreaseSpeedLimit = true;
                    currentPowerUp = DECREASE_SPEED_LIMIT;
                }
            }
        });
        //INCREASE SPEED LIMIT
        buttons.get(INCREASE_SPEED_LIMIT_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 4)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 15 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    increaseSpeedLimit = true;
                    currentPowerUp = INCREASE_SPEED_LIMIT;
                }
            }
        });
        //INCREASE PLAYER SPEED
        buttons.get(INCREASE_PLAYER_SPEED_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 5)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 20 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    increasePlayerSpeed();
                }
            }
        });
        //FLAT TIRE
        buttons.get(FLAT_TIRE_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 6)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 20 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    flatTire = true;
                    currentPowerUp = FLAT_TIRE;
                }
            }
        });
        //EMPTY GAS TANK
        buttons.get(EMPTY_GAS_TANK_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 7)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 20 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    emptyGasTank = true;
                    currentPowerUp = EMPTY_GAS_TANK;
                }
            }
        });
        //CLOSE ROAD
        buttons.get(CLOSE_ROAD_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 8)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 25 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    closeRoad = true;
                    currentPowerUp = CLOSE_ROAD;
                }
            }
        });
        //CLOSE INTERSECTION
        buttons.get(CI_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 9)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 25 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    closeIntersection = true;
                    currentPowerUp = CLOSE_INTERSECTION;
                }
            }
        });
        //OPEN INTERSECTION
        buttons.get(OI_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 10)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 25 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    openIntersection = true;
                    currentPowerUp = OPEN_INTERSECTION;
                }
            }
        });
        //STEAL
        buttons.get(STEAL_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 11)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 30 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    playerSteal();
                }
            }
        });
        //MIND CONTROL
        buttons.get(MIND_CONTROL_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 12)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 30 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    mindControl = true;
                    currentPowerUp = MIND_CONTROL;
                }
            }
        });

        //INTANGIBILITY
        buttons.get(INTANGIBILITY_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 13)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 30 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    playerIntangibility();
                }
            }
        });

        //MINDLESS TERROR
        buttons.get(MINDLESS_TERROR_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 14)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 30 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    mindlessTerror = true;
                    currentPowerUp = MINDLESS_TERROR;
                }
            }
        });

        //FLYING
        buttons.get(FLYING_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 15)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 40 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }

                    flying = true;
                    currentPowerUp = FLYING;
                }
            }
        });

        //INVINCIBILITY
        buttons.get(INVINCIBILITY_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (level.powerUpUnlocks < 16)
                {
                    return;
                }
                if (!currentPowerUp.equals(""))
                {
                    return;
                }

                if (data.getBalance() - 40 >= 0)
                {
                    if (!data.muteSound)
                    {
                        game.getAudio().play(PathXPropertyType.SOUND_POWER_UP.toString(), false);
                    }
                    playerInvincibility();
                }
            }
        });

        // ARROW UP
        buttons.get(UP_ARROW_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                scroll(0, -VIEWPORT_INC);
            }
        });

        // ARROW RIGHT
        buttons.get(RIGHT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                scroll(VIEWPORT_INC, 0);
            }
        });

        // ARROW LEFT
        buttons.get(LEFT_ARROW_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                scroll(-VIEWPORT_INC, 0);
            }
        });

        // ARROW DOWN
        buttons.get(DOWN_ARROW_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                scroll(0, VIEWPORT_INC);
            }
        });

        // PAUSE
        buttons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (!gameStarted)
                {
                    return;
                }
                if (data.getBalance() - 10 >= 0)
                {
                    pause();
                }
            }
        });
    }

    @Override
    public void updateGUI()
    {
        super.updateGUI();
        updatePowerUps();

        if (!gameStarted || loss || won)
        {
            return;
        }

        if (Player.getCurrentIntersection() == level.getDestination())
        {
            won = true;
            respondToWin();
        } else
        {
            if (!pause)
            {
                Player.update(this);                                    //UPDATE PLAYER
                if (Player.collidedPolice || level.recievedMoney <= 0 || Player.playerSpeed <= 0)
                {
                    loss = true;
                    respondToLoss();
                    return;
                }

                Iterator<Intersection> itIt = level.getIntersections().iterator();
                while (itIt.hasNext())
                {
                    Intersection i = itIt.next();
                    i.update();
                }

                Iterator<PathXPolice> pxpIt = police.iterator();        //UPDATE POLICE
                while (pxpIt.hasNext())                                 //
                {                                                       //
                    PathXPolice p = pxpIt.next();                       //
                    p.update();                                         //
                }                                                       //

                Iterator<PathXBandit> pxbIt = bandits.iterator();       //UPDATE BANDITS
                while (pxbIt.hasNext())                                 //
                {                                                       //
                    PathXBandit b = pxbIt.next();                       //
                    b.update();                                         //
                }                                                       //

                Iterator<PathXZombie> pxzIt = zombies.iterator();       //UPDATE ZOMBIE
                while (pxzIt.hasNext())                                 //
                {                                                       //
                    PathXZombie z = pxzIt.next();                       //
                    z.update();                                         //
                }

            }
        }
    }

    private void scroll(int x, int y)
    {
        data.getViewport().scroll(x, y);
        backgroundSprite.setX(backgroundSprite.getX() - x);
        backgroundSprite.setY(backgroundSprite.getY() - y);

        Iterator<Sprite> sIt = Intersections.iterator();

        while (sIt.hasNext())
        {
            Sprite s = sIt.next();

            s.setX(s.getX() - x);
            s.setY(s.getY() - y);
        }

        Player.getSprite().setX(Player.getSprite().getX() - x);
        Player.getSprite().setY(Player.getSprite().getY() - y);

        Iterator<PathXPolice> pxpIt = police.iterator();
        while (pxpIt.hasNext())
        {
            PathXPolice p = pxpIt.next();

            p.getSprite().setX(p.getSprite().getX() - x);
            p.getSprite().setY(p.getSprite().getY() - y);
        }

        Iterator<PathXBandit> pxbIt = bandits.iterator();
        while (pxbIt.hasNext())
        {
            PathXBandit b = pxbIt.next();

            b.getSprite().setX(b.getSprite().getX() - x);
            b.getSprite().setY(b.getSprite().getY() - y);
        }

        Iterator<PathXZombie> pxzIt = zombies.iterator();
        while (pxzIt.hasNext())
        {
            PathXZombie z = pxzIt.next();

            z.getSprite().setX(z.getSprite().getX() - x);
            z.getSprite().setY(z.getSprite().getY() - y);
        }
    }

    @Override
    public void enter()
    {
        data.getViewport().scroll(-data.getViewport().getViewportX(), -data.getViewport().getViewportY());

        game.setGUIButtons(buttons);
        Iterator<Sprite> buttonsIt = buttons.values().iterator();

        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            button.setState(PathXButtonState.VISIBLE_STATE.toString());
            button.setEnabled(true);
        }
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        game.setKeyListener(new KeyAdapter()
        {
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

        /*
         * DIALOG BOX
         */
        String closeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = game.loadImage(imgPath + closeButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String closeButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = game.loadImage(imgPath + closeButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 250, 300, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(CLOSE_BUTTON_TYPE, s);

        buttons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                decors.get(DIALOG_BOX_TYPE).setState(PathXSpriteState.INVISIBLE_STATE.toString());
                buttons.get(CLOSE_BUTTON_TYPE).setState(PathXButtonState.INVISIBLE_STATE.toString());
                buttons.get(CLOSE_BUTTON_TYPE).setEnabled(false);
            }
        });

        currentPowerUp = "";
        initLevel();
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

        for (int i = 0; i < level.getNumPolice(); i++)
        {
            buttons.remove(POLICE_TYPE + (i + 1));
        }

        for (int i = 0; i < level.getNumBandits(); i++)
        {
            buttons.remove(BANDIT_TYPE + (i + 1));
        }

        for (int i = 0; i < level.getNumZombies(); i++)
        {
            buttons.remove(ZOMBIE_TYPE + (i + 1));
        }

        data.getViewport().scroll(-(data.getViewport().getViewportX()),
                -(data.getViewport().getViewportY()));

        GraphManager = null;
        Player = null;
        police = null;
        zombies = null;
        bandits = null;
        Intersections = null;
        gameStarted = false;
        pause = false;
        loss = false;
        won = false;

        greenLightSelected = false;
        redLightSelected = false;

        decreaseSpeedLimit = false;
        increaseSpeedLimit = false;

        flatTire = false;
        emptyGasTank = false;

        closeRoad = false;
        closeIntersection = false;
        openIntersection = false;

        mindControl = false;
        selected = false;

        mindlessTerror = false;
        flying = false;

        policeControlled = null;
        banditControlled = null;
        zombieControlled = null;
        currentPowerUp = "";

        if (buttons.get(RETRY_BUTTON_TYPE) != null)
        {
            buttons.remove(RETRY_BUTTON_TYPE);
        }

        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        game.setKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent ke)
            {
            }
        });

    }

    private void initLevel()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);

        BufferedImage img;
        SpriteType sT;

        /**
         * LOAD LEVEL
         */
        PathXLevelLoader levelLoader = new PathXLevelLoader(new File(PATH_LEVELS + LEVEL_SCHEMA_FILE_NAME));

        level.reset();
        levelLoader.loadLevel(level);
        GraphManager = new PathXGraphManager(level.getIntersections(), level.getRoads());
        for (int i = 0; i < level.getIntersections().size(); i++)
        {
            level.getIntersections().get(i).setConnections(level);
        }

        police = new ArrayList<PathXPolice>();
        for (int i = 0; i < level.getNumPolice(); i++)
        {
            police.add(new PathXPolice(game, level));
            buttons.put(POLICE_TYPE + (i + 1), police.get(i).getSprite());
        }

        bandits = new ArrayList<PathXBandit>();
        for (int i = 0; i < level.getNumBandits(); i++)
        {
            bandits.add(new PathXBandit(game, level));
            buttons.put(BANDIT_TYPE + (i + 1), bandits.get(i).getSprite());
        }

        zombies = new ArrayList<PathXZombie>();
        for (int i = 0; i < level.getNumZombies(); i++)
        {
            zombies.add(new PathXZombie(game, level));
            buttons.put(ZOMBIE_TYPE + (i + 1), zombies.get(i).getSprite());
        }

        // LOAD THE BACKGROUND
        img = game.loadImage(imgPath + "path_x/" + level.getBackgroundImageFileName());
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(GAME_SCREEN_STATE, img);
        backgroundSprite = new Sprite(sT, 0, 0, 0, 0, GAME_SCREEN_STATE);

        // LOAD THE STARTING IMG
        startingLocationImage = game.loadImage(imgPath + "path_x/" + level.getStartingLocationImageFileName());

        // LOAD THE DEST IMG
        destinationLocationImage = game.loadImage(imgPath + "path_x/" + level.getDestinationImageFileName());

        Player = new PathXPlayer(game, level);

        Iterator<Intersection> iT = level.getIntersections().iterator();
        Intersections = new ArrayList();

        int i = 1;
        while (iT.hasNext())
        {
            final Intersection intersection = iT.next();
            final Sprite t;

            img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GREEN_INTERSECTION));
            sT = new SpriteType(INTERSECTION_TYPE + "_" + i);
            sT.addState(GREEN_LIGHT_STATE, img);
            img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_RED_INTERSECTION));
            sT.addState(RED_LIGHT_STATE, img);
            img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSED_INTERSECTION));
            sT.addState(CLOSED_LIGHT_STATE, img);
            t = new Sprite(sT, intersection.x - 20, intersection.y - 20, 0, 0, intersection.getState());

            buttons.put(INTERSECTION_TYPE + "_" + i, t);
            Intersections.add(t);
            intersection.setSprite(t);
            i++;

            t.setActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    if (!gameStarted)
                    {
                        return;
                    }
                    if (flatTire || emptyGasTank)
                    {
                        return;
                    }

                    if (greenLightSelected && !intersection.open && (intersection != level.getStartingLocation() && intersection != level.getDestination()))
                    {
                        data.updateMoney(-5);
                        intersection.makeLightGreen();
                        greenLightSelected = false;
                        currentPowerUp = "";
                        return;
                    }
                    if (redLightSelected && (intersection != level.getStartingLocation() && intersection != level.getDestination()))
                    {
                        data.updateMoney(-5);
                        intersection.makeLightRed();
                        redLightSelected = false;
                        currentPowerUp = "";
                        return;
                    }

                    if (closeIntersection && (intersection != level.getStartingLocation() && intersection != level.getDestination()))
                    {
                        data.updateMoney(-25);
                        intersection.closeIntersection();
                        closeIntersection = false;
                        currentPowerUp = "";
                        return;
                    }

                    if (openIntersection && (intersection != level.getStartingLocation() && intersection != level.getDestination()))
                    {
                        data.updateMoney(-25);
                        intersection.openIntersection();
                        openIntersection = false;
                        currentPowerUp = "";
                        return;
                    }

                    if (mindControl && ((intersection.open && !intersection.closed) && (intersection != level.getStartingLocation() && intersection != level.getDestination())))
                    {
                        if (policeControlled != null && !policeControlled.powerUp && (intersection != policeControlled.targetIntersection))
                        {
                            data.updateMoney(-30);
                            policeControlled.mindControlSelected = true;
                            policeControlled.tempTarget = intersection;
                            mindControl = false;
                            currentPowerUp = "";
                            policeControlled.getSprite().setState(POLICE_STATE);
                            policeControlled = null;
                            selected = false;

                            return;
                        }
                        if (banditControlled != null && !banditControlled.powerUp && (intersection != banditControlled.targetIntersection))
                        {
                            data.updateMoney(-30);
                            banditControlled.mindControlSelected = true;
                            banditControlled.tempTarget = intersection;
                            mindControl = false;
                            currentPowerUp = "";
                            banditControlled.getSprite().setState(BANDIT_STATE);
                            banditControlled = null;
                            selected = false;

                            return;
                        }

                        if (zombieControlled != null && !zombieControlled.powerUp && (intersection != zombieControlled.targetIntersection))
                        {
                            data.updateMoney(-30);
                            zombieControlled.mindControlSelected = true;
                            zombieControlled.tempTarget = intersection;
                            mindControl = false;
                            currentPowerUp = "";
                            zombieControlled.getSprite().setState(ZOMBIE_STATE);
                            zombieControlled = null;
                            selected = false;

                            return;
                        }

                        return;
                    }

                    if ((!Player.moving && flying) && (Player.getCurrentIntersection() != intersection))
                    {
                        if (GraphManager.shortestNoRestrictionDijkstra(Player.currentIntersection, intersection).size() <= 5)
                        {
                            flying = false;
                            currentPowerUp = "";
                            Player.fly = true;
                            Player.targetIntersection = intersection;
                        }

                        return;
                    }

                    if ((!Player.moving && !Player.fly) && (Player.getCurrentIntersection() != intersection))
                    {
                        Player.setShortestPath(GraphManager.shortestNoCRDijkstraPath(Player.getCurrentIntersection(), intersection));
                    }

                }
            });
        }
    }

    public void setGameLevel(GameLevel level)
    {
        this.level = level;
    }

    public GameLevel getLevel()
    {
        return level;
    }

    public Sprite getBackgroundSprite()
    {
        return backgroundSprite;
    }

    public PathXPlayer getPlayer()
    {
        return Player;
    }

    public Sprite getPlayerSprite()
    {
        return Player.getSprite();
    }

    public ArrayList<PathXPolice> getPolice()
    {
        return police;
    }

    public ArrayList<PathXBandit> getBandits()
    {
        return bandits;
    }

    public ArrayList<PathXZombie> getZombies()
    {
        return zombies;
    }

    public ArrayList<Sprite> getIntersections()
    {
        return Intersections;
    }

    public Image getStartingLocationImage()
    {
        return startingLocationImage;
    }

    public Image getDestinationLocationImage()
    {
        return destinationLocationImage;
    }

    public PathXGraphManager getGraphManager()
    {
        return GraphManager;
    }

    /**
     * Called when the user presses a key on the keyboard.
     */
    public void respondToKeyPress(int keyCode)
    {
        // WASD MOVES THE VIEWPORT
        if (keyCode == KeyEvent.VK_DOWN)
        {
            if (data.getViewport().getViewportY() < 45)
            {
                scroll(0, VIEWPORT_INC);
            }
        } else if (keyCode == KeyEvent.VK_RIGHT)
        {
            if (data.getViewport().getViewportX() < 360)
            {
                scroll(VIEWPORT_INC, 0);
            }
        } else if (keyCode == KeyEvent.VK_UP)
        {
            if (data.getViewport().getViewportY() > 0)
            {
                scroll(0, -VIEWPORT_INC);
            }
        } else if (keyCode == KeyEvent.VK_LEFT)
        {
            if (data.getViewport().getViewportX() > -150)
            {
                scroll(-VIEWPORT_INC, 0);
            }
        }

        if (keyCode == KeyEvent.VK_1)
        {
            data.updateMoney(100);
        }

        if (!gameStarted)
        {
            return;
        }

        //MAKE LIGHT GREEN
        if (keyCode == KeyEvent.VK_G)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 1)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 5 >= 0)
            {
                greenLightSelected = true;
                currentPowerUp = MAKE_LIGHT_GREEN;
            }
        }
        //MAKE LIGHT RED
        if (keyCode == KeyEvent.VK_R)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 2)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 5 >= 0)
            {
                redLightSelected = true;
                currentPowerUp = MAKE_LIGHT_RED;
            }
        }
        //PAUSE
        if (keyCode == KeyEvent.VK_F)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 3)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 10 >= 0)
            {
                pause();
            }
        }
        //DECREASE SPEED LIMIT
        if (keyCode == KeyEvent.VK_Z)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 4)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 15 >= 0)
            {
                decreaseSpeedLimit = true;
                currentPowerUp = DECREASE_SPEED_LIMIT;
            }
        }
        //INCREASE SPEED LIMIT
        if (keyCode == KeyEvent.VK_X)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 5)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 15 >= 0)
            {
                increaseSpeedLimit = true;
                currentPowerUp = INCREASE_SPEED_LIMIT;
            }
        }
        //INCREASE PLAYER SPEED
        if (keyCode == KeyEvent.VK_S)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 5)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 20 >= 0)
            {
                increasePlayerSpeed();
            }
        }
        //FLAT TIRE
        if (keyCode == KeyEvent.VK_T)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 6)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 20 >= 0)
            {
                flatTire = true;
                currentPowerUp = FLAT_TIRE;
            }
        }
        //EMPTY GAS TANK
        if (keyCode == KeyEvent.VK_E)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 7)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 20 >= 0)
            {
                emptyGasTank = true;
                currentPowerUp = EMPTY_GAS_TANK;
            }
        }

        //CLOSE ROAD
        if (keyCode == KeyEvent.VK_H)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 8)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 20 >= 0)
            {
                closeRoad = true;
                currentPowerUp = CLOSE_ROAD;
            }
        }

        //OPEN INTERSECTION
        if (keyCode == KeyEvent.VK_C)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 9)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 25 >= 0)
            {
                closeIntersection = true;
                currentPowerUp = CLOSE_INTERSECTION;
            }
        }

        //CLOSED INTERSECTION
        if (keyCode == KeyEvent.VK_O)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 10)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 25 >= 0)
            {
                openIntersection = true;
                currentPowerUp = OPEN_INTERSECTION;
            }
        }

        //PLAYER STEAL
        if (keyCode == KeyEvent.VK_Q)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 11)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 30 >= 0 && !Player.steal)
            {
                playerSteal();
            }
        }
        //MIND CONTROL
        if (keyCode == KeyEvent.VK_M)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 12)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 30 >= 0)
            {
                mindControl = true;
                currentPowerUp = MIND_CONTROL;
            }
        }
        //INTANGIBILITY
        if (keyCode == KeyEvent.VK_B)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 13)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 30 >= 0)
            {
                playerIntangibility();
            }
        }
        //MINDLESSTERROR
        if (keyCode == KeyEvent.VK_L)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 14)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 30 >= 0)
            {
                mindlessTerror = true;
                currentPowerUp = MINDLESS_TERROR;
            }
        }
        //FLYING
        if (keyCode == KeyEvent.VK_Y)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 15)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 40 >= 0)
            {
                flying = true;
                currentPowerUp = FLYING;
            }
        }
        //INVINSIBILITY
        if (keyCode == KeyEvent.VK_V)
        {
            if (!gameStarted)
            {
                return;
            }
            if (level.powerUpUnlocks < 16)
            {
                return;
            }
            if (!currentPowerUp.equals(""))
            {
                return;
            }
            if (data.getBalance() - 40 >= 0)
            {
                playerInvincibility();
            }
        }

        if (keyCode == KeyEvent.VK_ESCAPE)
        {
            greenLightSelected = false;
            redLightSelected = false;

            decreaseSpeedLimit = false;
            increaseSpeedLimit = false;

            flatTire = false;
            emptyGasTank = false;

            closeRoad = false;
            closeIntersection = false;
            openIntersection = false;

            mindControl = false;
            mindlessTerror = false;
            selected = false;

            flying = false;

            policeControlled = null;
            Iterator<PathXPolice> pIt = police.iterator();
            while (pIt.hasNext())
            {
                PathXPolice p = pIt.next();
                if (!p.mindlessTerror)
                {
                    p.getSprite().setState(POLICE_STATE);
                }
            }

            banditControlled = null;
            Iterator<PathXBandit> bIt = bandits.iterator();
            while (bIt.hasNext())
            {
                PathXBandit b = bIt.next();
                if (!b.mindlessTerror)
                {
                    b.getSprite().setState(BANDIT_STATE);
                }
            }

            zombieControlled = null;
            Iterator<PathXZombie> zIt = zombies.iterator();
            while (zIt.hasNext())
            {
                PathXZombie z = zIt.next();
                if (!z.mindlessTerror)
                {
                    z.getSprite().setState(ZOMBIE_STATE);
                }
            }

            currentPowerUp = "";
        }
    }

    public void pause()
    {
        if (pause)
        {
            pause = !pause;
            return;
        }

        data.updateMoney(-10);
        pause = true;
    }

    private void increasePlayerSpeed()
    {
        data.updateMoney(-20);
        Player.playerSpeed += .2;
    }

    private void playerSteal()
    {
        if (Player.steal || Player.invincibility || Player.stunned)
        {
            return;
        }

        data.updateMoney(-30);
        Player.steal = true;
        Player.getSprite().setState(PLAYER_STEAL_STATE);
        Player.stealStart = System.currentTimeMillis();
    }

    public void playerIntangibility()
    {
        if (Player.steal || Player.invincibility || Player.stunned)
        {
            return;
        }

        data.updateMoney(-30);
        Player.intangibility = true;
        Player.getSprite().setState(PLAYER_INTANGIBILITY_STATE);
        Player.intangibilityStart = System.currentTimeMillis();
    }

    private void playerInvincibility()
    {
        if (Player.steal || Player.invincibility || Player.stunned)
        {
            return;
        }

        data.updateMoney(-40);
        Player.invincibility = true;
        Player.getSprite().setState(PLAYER_INVINCIBILITY_STATE);
        Player.intangibilityStart = System.currentTimeMillis();
    }

    public void roadClicked(Road r)
    {
        if (!gameStarted)
        {
            return;
        }

        if (decreaseSpeedLimit && r.getSpeedLimit() != 10)
        {
            r.setSpeedLimit(r.getSpeedLimit() - 10);
            decreaseSpeedLimit = false;
            currentPowerUp = "";
            data.updateMoney(-15);
            return;
        }

        if (increaseSpeedLimit && r.getSpeedLimit() != 100)
        {
            r.setSpeedLimit(r.getSpeedLimit() + 10);
            increaseSpeedLimit = false;
            currentPowerUp = "";
            data.updateMoney(-15);
        }

        if (closeRoad)
        {
            r.closed = true;
            closeRoad = false;
            currentPowerUp = "";
            data.updateMoney(-20);
        }

    }

    public void respondToLoss()
    {
        if (Player.collidedPolice)
        {
            int temp = (int) Math.round(data.getBalance() * .1);
            data.updateMoney(-temp);
            moneyLost = temp;
        }
        decors.get(DIALOG_BOX_TYPE).setState(PathXSpriteState.VISIBLE_STATE.toString());
        buttons.get(CLOSE_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
        buttons.get(CLOSE_BUTTON_TYPE).setX(190);
        buttons.get(CLOSE_BUTTON_TYPE).setEnabled(true);

        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        /*
         * DIALOG BOX
         */
        String retryButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RETRY);
        sT = new SpriteType(RETRY_BUTTON_TYPE);
        img = game.loadImage(imgPath + retryButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String retryButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RETRY_MOUSE_OVER);
        img = game.loadImage(imgPath + retryButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 310, 300, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(RETRY_BUTTON_TYPE, s);

        buttons.get(RETRY_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
        buttons.get(RETRY_BUTTON_TYPE).setEnabled(true);

        buttons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.LevelSelectScreen);
            }
        });

        buttons.get(RETRY_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                buttons.remove(RETRY_BUTTON_TYPE);

                game.GameScreen.setGameLevel(data.getLevels().get(level.type));
                game.enter(game.GameScreen);
            }
        });
    }

    public void respondToWin()
    {
        data.updateMoney(level.recievedMoney);

        decors.get(DIALOG_BOX_TYPE).setState(PathXSpriteState.VISIBLE_STATE.toString());
        buttons.get(CLOSE_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
        buttons.get(CLOSE_BUTTON_TYPE).setX(190);
        buttons.get(CLOSE_BUTTON_TYPE).setEnabled(true);

        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        /*
         * DIALOG BOX
         */
        String retryButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RETRY);
        sT = new SpriteType(RETRY_BUTTON_TYPE);
        img = game.loadImage(imgPath + retryButton);
        sT.addState(PathXButtonState.VISIBLE_STATE.toString(), img);
        String retryButtonMouseOver = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RETRY_MOUSE_OVER);
        img = game.loadImage(imgPath + retryButtonMouseOver);
        sT.addState(PathXButtonState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, 310, 300, 0, 0, PathXButtonState.VISIBLE_STATE.toString());
        buttons.put(RETRY_BUTTON_TYPE, s);

        buttons.get(RETRY_BUTTON_TYPE).setState(PathXButtonState.VISIBLE_STATE.toString());
        buttons.get(RETRY_BUTTON_TYPE).setEnabled(true);

        buttons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                game.enter(game.LevelSelectScreen);
            }
        });

        buttons.get(RETRY_BUTTON_TYPE).setActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                buttons.remove(RETRY_BUTTON_TYPE);

                game.GameScreen.setGameLevel(data.getLevels().get(level.type));
                game.enter(game.GameScreen);
            }
        });

        level.setState(GameLevelState.COMPLETED_STATE.toString());
        String temp = "LEVEL_BUTTON_TYPE" + (level.ID + 1);
        if (data.getLevels().get(temp).getState().equals(GameLevelState.LOCKED_STATE.toString()))
        {
            data.getLevels().get(temp).setState(GameLevelState.UNLOCKED_STATE.toString());
        }
    }

    private void updatePowerUps()
    {
        Iterator<Intersection> iIt = level.getIntersections().iterator();
        while (iIt.hasNext())
        {
            Intersection i = iIt.next();
            if (i.getX() - data.getViewport().getViewportX() < 150 || !gameStarted)
            {
                Intersections.get(i.ID - 1).setEnabled(false);
            } else
            {
                Intersections.get(i.ID - 1).setEnabled(true);
            }
        }

        Iterator<PathXPolice> pIt = police.iterator();
        while (pIt.hasNext())
        {
            PathXPolice p = pIt.next();
            if (!gameStarted)
            {
                p.getSprite().setEnabled(false);
            } else
            {
                p.getSprite().setEnabled(true);
            }
        }

        Iterator<PathXBandit> bIt = bandits.iterator();
        while (bIt.hasNext())
        {
            PathXBandit b = bIt.next();
            if (!gameStarted)
            {
                b.getSprite().setEnabled(false);
            } else
            {
                b.getSprite().setEnabled(true);
            }
        }

        Iterator<PathXZombie> zIt = zombies.iterator();
        while (bIt.hasNext())
        {
            PathXZombie z = zIt.next();
            if (!gameStarted)
            {
                z.getSprite().setEnabled(false);
            } else
            {
                z.getSprite().setEnabled(true);
            }
        }

        if (greenLightSelected)
        {
            powerUpButtons.get(MAKE_LIGHT_GREEN_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (redLightSelected)
        {
            powerUpButtons.get(MAKE_LIGHT_RED_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (decreaseSpeedLimit)
        {
            powerUpButtons.get(DECREASE_SPEED_LIMIT_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (increaseSpeedLimit)
        {
            powerUpButtons.get(INCREASE_SPEED_LIMIT_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }

        if (flatTire)
        {
            powerUpButtons.get(FLAT_TIRE_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (emptyGasTank)
        {
            powerUpButtons.get(EMPTY_GAS_TANK_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (closeRoad)
        {
            powerUpButtons.get(CLOSE_ROAD_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }

        if (closeIntersection)
        {
            powerUpButtons.get(CI_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (openIntersection)
        {
            powerUpButtons.get(OI_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (mindControl)
        {
            powerUpButtons.get(MIND_CONTROL_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }

        if (mindlessTerror)
        {
            powerUpButtons.get(MINDLESS_TERROR_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }
        if (flying)
        {
            powerUpButtons.get(FLYING_TYPE).setState(PathXButtonState.MOUSE_OVER_STATE.toString());
        }

    }
}
