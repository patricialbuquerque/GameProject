package this_shit_is_real.gameobjects;

import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import this_shit_is_real.GamePlay;
import this_shit_is_real.field.FieldDirection;
import this_shit_is_real.field.FieldPosition;
import this_shit_is_real.sounds.GameSounds;

public class Player extends GameObjects implements KeyboardHandler {
    private int score;
    private final Keyboard keyboard;
    private GameObjectsFactory factory;
    private GamePlay gamePlay;
    private int lifes;
    private int originalHealth;
    private KeyboardEvent left;
    private KeyboardEvent right;
    private KeyboardEvent shoot;
    private KeyboardEvent mute;
    private KeyboardEvent quit;
    private Boolean musicOn;

    public Player(GameObjectsType type, FieldPosition[] pos, GamePlay game){
        super(type, pos);
        keyboard = new Keyboard(this);
        gamePlay = game;
        factory = gamePlay.getFactory();
        init();
        score = 0;
        lifes = 3;
        originalHealth = getHealth();
        musicOn = true;
    }


    // KEYBOARD  -------------------------------------------------------------

    public void init() {
        left = new KeyboardEvent();
        left.setKey(KeyboardEvent.KEY_LEFT);
        left.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        right = new KeyboardEvent();
        right.setKey(KeyboardEvent.KEY_RIGHT);
        right.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        shoot = new KeyboardEvent();
        shoot.setKey(KeyboardEvent.KEY_SPACE);
        shoot.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        quit = new KeyboardEvent();
        quit.setKey(KeyboardEvent.KEY_Q);
        quit.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        mute = new KeyboardEvent();
        mute.setKey(KeyboardEvent.KEY_M);
        mute.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        keyboard.addEventListener(left);
        keyboard.addEventListener(right);
        keyboard.addEventListener(shoot);
        keyboard.addEventListener(quit);
        keyboard.addEventListener(mute);
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        FieldPosition pos = getPos();
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_LEFT:
                setCurrentDirection(FieldDirection.LEFT);
                move(getCurrentDirection(),1);
                break;
            case KeyboardEvent.KEY_RIGHT:
                setCurrentDirection(FieldDirection.RIGHT);
                move(getCurrentDirection(),1);
                break;
            case KeyboardEvent.KEY_SPACE:
                shoot();
                break;
            case KeyboardEvent.KEY_Q:
                System.exit(0);
                break;
            case KeyboardEvent.KEY_M:
                if(musicOn) {
                    GameSounds.gameMusic.stop();
                    musicOn = false;
                } else {
                    GameSounds.gameMusic.play(true);
                    musicOn = true;
                }
                break;
        }
    }

    public void keyReleased(KeyboardEvent keyboardEvent) {}

    public void killListeners () {
        keyboard.removeEventListener(left);
        keyboard.removeEventListener(right);
        keyboard.removeEventListener(shoot);
        keyboard.removeEventListener(quit);
        keyboard.removeEventListener(mute);
    }


    // OTHER ---------------------------------------------------------------

    public void shoot(){
        int row = getPos().getRow() - 1;
        int col = getPos().getCol();
        Bullets bullet = factory.generateBullets(col, row);
        bullet.setCurrentDirection(FieldDirection.UP);
        bullet.setDamage(getDamage());
        gamePlay.addBullet(bullet);
        GameSounds.throwSeringe.play(true);
    }


    // GETTERS ---------------------------------------------------------------

    public int getOriginalHealth() {
        return originalHealth;
    }
    public int getScore() {
        return score;
    }
    public int getLifes() {
        return lifes;
    }


    // SETTERS -----------------------------------------------------------------

    public void addScore(int add) {
        score += add;
    }
    public void reduceLifes() {
        if (lifes > 0) {
            lifes = lifes - 1;
            gamePlay.minusLife();
        }
    }
}