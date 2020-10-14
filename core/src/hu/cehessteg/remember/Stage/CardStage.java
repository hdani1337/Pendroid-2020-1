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

import static hu.cehessteg.remember.MemoryGame.preferences;
import static hu.cehessteg.remember.Stage.OptionsStage.difficulty;
import static hu.cehessteg.remember.Stage.OptionsStage.gamemode;

public class CardStage extends SimpleWorldStage {
    /**
     * Ezen a stagen csak a kártyák lesznek, semmi más
     * **/
    public static long time;//Globális időszámláló (másodpercben)
    public static long score;//Globális pontszámláló
    public float scoreTimer;//Publikus időszámláló pontszámláshoz (pontos), jelenlegi idő
    public float lastFoundTime;//Publikus időszámláló pontszámláshoz (pontos), legutóbbi pár megtalálásának ideje
    public static int level;//Globális szintszámláló

    /**
     * MARTIX
     * MAX-WIDTH: 8
     * MAX-HEIGHT: 6
     * **/
    public static Vector2 matrix;//Globális mátrix
    public static ArrayList<Card> kartyak;//Globális kártya lista
    public ArrayList<Card> selectedCards;//Publikus kiválaszott kártya lista

    public static boolean isAct;//Globális boolean, fut e a játékmenet
    public static boolean isGameOver;//Globális boolean, vége van e a játéknak
    public static boolean isShuffling;//Globális boolean, keverednek e a kártyák

    private CardMethods cardMethods;//Privát kártya metódus osztály példány

    public CardStage(MyGame game) {
        super(new ResponseViewport(9), game);
        level = 0;//Szint nullázása
        checkDifficulty();
        cardMethods = new CardMethods(this);//Kártya metódus osztály példányosítása
        cardMethods.nullEverything();//Minden érték alapértelmezettre állítása
        if(gamemode == 1) time = 151;//Ha árkád módban vagyunk, akkor 150 másodperc áll rendelkezésre a játékmenetre
        newCardset(false);
        setTimers();
    }

    /**Nehézség ellenőrzése, mivel ha még nincs mentett adat akkor ez 0**/
    private void checkDifficulty(){
        if(difficulty == 0) {
            difficulty = 2;
            preferences.putInteger("difficulty",2);
            preferences.flush();
        }
    }

    /**Középre helyezi a kártyákat a stage mozgatásával**/
    private void centerStage(){
        float width = matrix.x*kartyak.get(0).getWidth()+(kartyak.get(1).getX()-kartyak.get(0).getX()-kartyak.get(0).getWidth())*matrix.x;
        float height = matrix.y*kartyak.get(0).getHeight()*1.05f;
        getViewport().setScreenX((int) (getViewport().getScreenWidth()*0.525f-(getViewport().getScreenWidth()/getViewport().getWorldWidth())*(width/2)));
        getViewport().setScreenY((int) (-(getViewport().getScreenHeight()/getViewport().getWorldHeight())*(getViewport().getWorldHeight() / 2 - height/2)));
    }

    /**
     * Új kártyapakli generálása és középre helyezés (másfél másodperces késéssel)
     * @param needTimer Kell e késleltetés az új pakli létrehozásához
     * **/
    public void newCardset(boolean needTimer){
        generateNewMatrix();
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

    /**Fix méret: Kiválasztott méret beállítása
     * Random méret: Függvény a mátrix új méretéhez, ami egyenesen arányos a nehézséggel és a szinttel
     * **/
    private void generateNewMatrix(){
        matrix = new Vector2();

        if(OptionsStage.size == 0) {
            int width = (int) (2 + 5 / (5 - (level * difficulty) / 4.0f));
            int height = (int) (2 + 3 / (3 - (level * difficulty) / 6.0f));

            if (width > 8) width = 8;
            if (height > 6) height = 6;

            matrix.x = width;
            matrix.y = height;
        }else{
            matrix.x = OptionsStage.size/10;
            matrix.y = OptionsStage.size%10;
        }
    }

    /**Timerek beállítása**/
    public void setTimers(){
        //Kártyák cserélése
        if(difficulty > 1) {
            //1: KÖNNYŰ -   NEM CSERÉL KÁRTYÁT
            //2: NORMÁL -   9 MÁSODPERCENKÉNT CSERÉL 2 KÁRTYÁT
            //3: NEHÉZ  -   6 MÁSODPERCENKÉNT CSERÉL 4 KÁRTYÁT
            //4: LEHETETLEN - 4.5 MÁSODPERCENKÉNT AZ ÖSSZESET MEGCSERÉLI
            addTimer(new TickTimer(18/difficulty, true, new TickTimerListener() {
                @Override
                public void onTick(Timer sender, float correction) {
                    super.onTick(sender, correction);
                    if(isAct && !isGameOver && !kartyak.get(0).isShowing) {
                        if (difficulty == 4)
                            cardMethods.shuffleCards();
                        else
                            for (int i = 1; i < difficulty; i++)
                                cardMethods.shuffleTwoCards();
                    }
                }
            }));
        }

        /**Játékbeli óra léptetése játékmód alapján**/
        addTimer(new TickTimer(1,true,new TickTimerListener(){
            @Override
            public void onRepeat(TickTimer sender) {
                super.onRepeat(sender);
                try {
                    if (!kartyak.get(0).isShowing) {
                        if (isAct && !isGameOver) {
                            if (gamemode == 1) {
                                time--;
                                if (time < 0) {
                                    isAct = false;
                                    isGameOver = true;
                                }
                            } else time++;
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    System.out.println("Az asztalon nincs több lap!");
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
