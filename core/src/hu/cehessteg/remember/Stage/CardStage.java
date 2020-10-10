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
import hu.csanyzeg.master.MyBaseClasses.Timers.PermanentTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.PermanentTimerListener;
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
    public int level;

    public static Vector2 matrix = new Vector2((int)(Math.random()*3)+3,(int)(Math.random()*3)+3);
    public static ArrayList<Card> kartyak;
    public ArrayList<Card> selectedCards;

    public static boolean isAct;
    public static boolean isGameOver;
    public static boolean isShuffling;

    private CardMethods cardMethods;

    public CardStage(MyGame game) {
        super(new ResponseViewport(9), game);
        level = 0;
        matrix = new Vector2((int)(Math.random()*3)+3,(int)(Math.random()*3)+3);
        if(gamemode == 1) time = 90-(level*difficulty*3);
        cardMethods = new CardMethods(this);
        cardMethods.nullEverything();
        newCardset(false);
        setTimers();
    }

    /**Középre helyezi a kártyákat a stage mozgatásával**/
    private void centerStage(){
        float width = matrix.x*kartyak.get(0).getWidth()+(kartyak.get(1).getX()-kartyak.get(0).getX()-kartyak.get(0).getWidth())*matrix.x;
        float height = matrix.y*kartyak.get(0).getHeight()*1.05f;
        getViewport().setScreenX((int) (getViewport().getScreenWidth()/2-(getViewport().getScreenWidth()/getViewport().getWorldWidth())*(width/2)));
        getViewport().setScreenY((int) (-(getViewport().getScreenHeight()/getViewport().getWorldHeight())*(getViewport().getWorldHeight() / 2 - height/2)));
    }

    /**
     * Új kártyapakli generálása és középre helyezés (másfél másodperces késéssel)
     * @param needTimer Kell e késleltetés az új pakli létrehozásához
     * **/
    public void newCardset(boolean needTimer){
        matrix = new Vector2((int)(Math.random()*3)+3,(int)(Math.random()*3)+3);
        if(needTimer) {
            addTimer(new TickTimer(1.5f, false, new TickTimerListener() {
                @Override
                public void onStop(Timer sender) {
                    super.onStop(sender);
                    cardMethods.generateCards(world);
                    centerStage();
                }
            }));
        }else{
            cardMethods.generateCards(world);
            centerStage();
        }
    }

    /**Timerek beállítása**/
    public void setTimers(){
        //Kártyák cserélése
        if(difficulty > 1) {
            //1: KÖNNYŰ -   NEM CSERÉL KÁRTYÁT
            //2: NORMÁL -   9 MÁSODPERCENKÉNT CSERÉL 2 KÁRTYÁT
            //3: NEHÉZ  -   6 MÁSODPERCENKÉNT CSERÉL 4 KÁRTYÁT
            addTimer(new TickTimer(18/difficulty, true, new TickTimerListener() {
                @Override
                public void onTick(Timer sender, float correction) {
                    super.onTick(sender, correction);
                    if(isAct && !isGameOver)
                        for (int i = 1; i < difficulty; i++)
                            cardMethods.shuffleTwoCards();
                }
            }));
        }

        /**Játékbeli óra léptetése játékmód alapján**/
        addTimer(new TickTimer(1,true,new TickTimerListener(){
            @Override
            public void onRepeat(TickTimer sender) {
                super.onRepeat(sender);
                if(isAct && !isGameOver) {
                    if(gamemode == 1) {
                        time--;
                        if(time<0){
                            isAct = false;
                            isGameOver = true;
                        }
                    }
                    else time++;
                }
            }
        }));

        /**Pontszámláláshoz létrehozott időzítő léptetése képkockánként**/
        addTimer(new PermanentTimer(new PermanentTimerListener(){
            @Override
            public void onTick(PermanentTimer sender, float correction) {
                super.onTick(sender, correction);
                if(gamemode == 1) scoreTimer-=correction;
                else scoreTimer+=correction;
            }
        }));
    }
}
