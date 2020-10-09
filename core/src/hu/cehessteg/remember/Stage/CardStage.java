package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import hu.cehessteg.remember.Actor.Card;
import hu.cehessteg.remember.Actor.CardType;
import hu.cehessteg.remember.CardMethods;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.PositionRule;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldHelper;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldStage;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;

import static hu.cehessteg.remember.Stage.OptionsStage.difficulty;
import static hu.cehessteg.remember.Stage.OptionsStage.gamemode;

public class CardStage extends SimpleWorldStage {
    /**
     * Ezen a stagen csak a kártyák lesznek, semmi más
     * **/
    public static long time;
    public static long score;
    public float scoreTimer;
    public float lastFoundTime;

    public static Vector2 matrix = new Vector2((int)(Math.random()*5)+3,(int)(Math.random()*3)+3);
    public static ArrayList<Card> kartyak;
    public ArrayList<Card> selectedCards;

    public static boolean isAct;
    public static boolean isGameOver;
    public static boolean isShuffling;

    private CardMethods cardMethods;

    public CardStage(MyGame game) {
        super(new ResponseViewport(9), game);
        time = 90;
        cardMethods = new CardMethods(this);
        cardMethods.generateCards(world);
        centerStage();
        if(difficulty > 1) {
            //1: KÖNNYŰ -   NEM CSERÉL KÁRTYÁT
            //2: NORMÁL -   12 MÁSODPERCENKÉNT CSERÉL 2 KÁRTYÁT
            //3: NEHÉZ  -   6 MÁSODPERCENKÉNT CSERÉL 4 KÁRTYÁT
            addTimer(new TickTimer(24/difficulty, true, new TickTimerListener() {
                @Override
                public void onTick(Timer sender, float correction) {
                    super.onTick(sender, correction);
                    if(isAct && !isGameOver)
                        for (int i = 1; i < difficulty; i++)
                            cardMethods.shuffleTwoCards();
                }
            }));
        }
        addTimer(new TickTimer(1,true,new TickTimerListener(){
            @Override
            public void onRepeat(TickTimer sender) {
                super.onRepeat(sender);
                if(isAct && !isGameOver) {
                    if(gamemode == 1) time++;
                    else time--;
                }
            }
        }));
    }

    private void centerStage(){
        float width = matrix.x*kartyak.get(0).getWidth()+(kartyak.get(1).getX()-kartyak.get(0).getX()-kartyak.get(0).getWidth())*matrix.x;
        float height = matrix.y*kartyak.get(0).getHeight()*1.05f;
        getViewport().setScreenX((int) (getViewport().getScreenWidth()/2-(getViewport().getScreenWidth()/getViewport().getWorldWidth())*(width/2)));
        getViewport().setScreenY((int) (-(getViewport().getScreenHeight()/getViewport().getWorldHeight())*(getViewport().getWorldHeight() / 2 - height/2)));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(gamemode == 1) scoreTimer+=delta;
        else scoreTimer-=delta;
    }
}
