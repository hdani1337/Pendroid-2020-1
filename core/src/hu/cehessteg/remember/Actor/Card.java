package hu.cehessteg.remember.Actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import hu.cehessteg.remember.CardMethods;
import hu.cehessteg.remember.Stage.CardStage;
import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyGroup;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.PositionRule;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.ShapeType;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleBodyType;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorld;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldHelper;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;

import static hu.cehessteg.remember.Stage.CardStage.isAct;
import static hu.cehessteg.remember.Stage.CardStage.isGameOver;
import static hu.cehessteg.remember.Stage.CardStage.isShuffling;
import static hu.cehessteg.remember.Stage.OptionsStage.difficulty;

public class Card extends MyGroup {
    static ArrayList<String> kartyaTextures = new ArrayList<String>(){};
    static String kartyaHatoldal = "pic/kartyaHatlap.png";

    public static AssetList assetList = new AssetList();
    static {
        kartyaTextures.add("pic/kartyak/android.png");
        kartyaTextures.add("pic/kartyak/atari.png");
        kartyaTextures.add("pic/kartyak/billentyuzet.png");
        kartyaTextures.add("pic/kartyak/cd.png");
        kartyaTextures.add("pic/kartyak/cpu.png");
        kartyaTextures.add("pic/kartyak/eger.png");
        kartyaTextures.add("pic/kartyak/elem.png");
        kartyaTextures.add("pic/kartyak/ellenallas.png");
        kartyaTextures.add("pic/kartyak/floppy.png");
        kartyaTextures.add("pic/kartyak/fules.png");
        kartyaTextures.add("pic/kartyak/hdd.png");
        kartyaTextures.add("pic/kartyak/kazetta.png");
        kartyaTextures.add("pic/kartyak/kirby.png");
        kartyaTextures.add("pic/kartyak/kondenzator.png");
        kartyaTextures.add("pic/kartyak/maszk.png");
        kartyaTextures.add("pic/kartyak/monitor.png");
        kartyaTextures.add("pic/kartyak/nyilak.png");
        kartyaTextures.add("pic/kartyak/ram.png");
        kartyaTextures.add("pic/kartyak/ssd.png");
        kartyaTextures.add("pic/kartyak/tap.png");
        kartyaTextures.add("pic/kartyak/tux.png");
        kartyaTextures.add("pic/kartyak/ventillator.png");
        kartyaTextures.add("pic/kartyak/windows.png");
        kartyaTextures.add("pic/kartyak/windows95.png");
        for (String asset : kartyaTextures) assetList.addTexture(asset);
        assetList.addTexture(kartyaHatoldal);
    }

    public boolean isSelected;//Ki van e választva a kártya
    public Vector2 koordinatak;//A kártya koordinátái a mátrixban
    public CardType type;//A kártya típusa
    private int id;//A kártya azonosítója, egyelőre nincs használatban
    private CardMethods cardMethods;//Kártya metódusok osztály példánya
    public boolean isShowing;//Látszik e a kártya típusa
    private int cheats;//Csalások száma

    public OneSpriteStaticActor backCard;
    public OneSpriteStaticActor frontCard;

    private ArrayList<OneSpriteStaticActor> cardBackAndFront;

    public Card(MyGame game, Vector2 koordinatak, SimpleWorld world, CardType cardType, CardMethods cardMethods) {
        super(game);
        this.koordinatak = koordinatak;
        this.id = (int) (koordinatak.x+koordinatak.y*CardStage.matrix.x);
        this.isSelected = false;
        this.type = cardType;
        this.cardMethods = cardMethods;
        basicStuff(world);
        addTimers();
        addActors();
    }

    /**Alap dolgok beállítása, mint a méret, pozíció, WorldHelper, szín, átmenettel való megjelenés**/
    private void basicStuff(SimpleWorld world){
        cardBackAndFront = new ArrayList<>();
        frontCard = new OneSpriteStaticActor(game,getRandomTextureHash());
        backCard = new OneSpriteStaticActor(game,kartyaHatoldal);
        cardBackAndFront.add(frontCard);
        cardBackAndFront.add(backCard);

        for (OneSpriteStaticActor c : cardBackAndFront) {
            c.setActorWorldHelper(new SimpleWorldHelper(world, this, ShapeType.Rectangle, SimpleBodyType.Sensor));
            c.setSize(c.getWidth() * 0.0025f, c.getHeight() * 0.0025f);
            c.setPosition(koordinatak.x * 1.3f, 9 - (koordinatak.y * 1.3f) - getHeight());
            c.setColor(0, 0, 0, 0);
            ((SimpleWorldHelper) c.getActorWorldHelper()).getBody().colorToFixTime(1, 1, 1, 1, 1);
        }
    }

    /**Timerek hozzáadása**/
    private void addTimers(){
        isShowing = true;

        /**Nehézség számával fordítottan arányos ideig jelenjenek meg a kártyák**/
        addTimer(new TickTimer(6-difficulty,false,new TickTimerListener(){
            @Override
            public void onStop(Timer sender) {
                super.onStop(sender);
                ((SimpleWorldHelper)frontCard.getActorWorldHelper()).getBody().colorToFixTime(1,1,1,1,0);
                addTimer(new TickTimer(1,false,new TickTimerListener(){
                    @Override
                    public void onStop(Timer sender) {
                        super.onStop(sender);
                        isShowing = false;
                    }
                }));
            }
        }));

        /**Kártya kiválasztása**/
        addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(isAct && !isGameOver && getColor().a!=0) {
                    if (!isShuffling && !isShowing) {
                        Card.this.isSelected = !Card.this.isSelected;
                        flipCard();
                    }
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void addActors(){
        addActor(backCard);
        addActor(frontCard);
    }

    /**Új koordináta beállítása
     * Átmenettel oda is helyezi magát a kártya
     * **/
    public void setKoordinatak(Vector2 newKoordinatak){
        koordinatak = newKoordinatak;
        for (OneSpriteStaticActor c : cardBackAndFront)
            ((SimpleWorldHelper)c.getActorWorldHelper()).getBody().moveToFixSpeed(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight(),5, PositionRule.LeftBottom);
    }

    /**Kiválasztás megszüntetése egyezésvizsgálatnál**/
    public void deSelect(){
        isSelected = false;
        ((SimpleWorldHelper)frontCard.getActorWorldHelper()).getBody().colorToFixTime(0.3f,1,1,1,0);
    }

    /**A kártya forgatása**/
    private void flipCard(){
        //KÁRTYA MEGFORDÍTÁSA
        //LEGYEN VALAMI KÖRALAKJA ÉS HANGJA
        if(isSelected){
            //JELENJEN MEG AZ ALAKZAT
            //JELENJEN MEG A KIJELÖLÉS
            ((SimpleWorldHelper)frontCard.getActorWorldHelper()).getBody().colorToFixTime(0.3f,1,1,1,1);
            addTimer(new TickTimer(0.5f,false,new TickTimerListener(){
                @Override
                public void onStop(Timer sender) {
                    super.onStop(sender);
                    cardMethods.addCard(Card.this);
                }
            }));
        }else{
            //FORDULJON VISSZA A KÁRTYA
            //TŰNJÖN EL A KIJELÖLÉS
            ((SimpleWorldHelper)frontCard.getActorWorldHelper()).getBody().colorToFixTime(0.3f,1,1,1,0);
            cardMethods.removeCard(this);

            /**Ha visszavonja a kiválasztást, akkor növelünk egy számlálót
             * Ha ez a számláló páratlan, akkor levonunk valamennyi pontot a nehézség függvényében egy kis véletlenszerűséggel keverve
             * Így csalhat a játékos, mivel látja a kártya típusait
             * **/
            if(cheats++%2==1) CardStage.score-=difficulty*7*(Math.random()*1+1);
        }
    }

    /**A kártya típusa alapján ad vissza textúrát
     * **/
    private String getRandomTextureHash(){
        int id = 0;
        for (CardType c : CardType.values())
            if(c == type) break;
            else id++;
        return kartyaTextures.get(id);
    }

    @Override
    public float getX() {
        return frontCard.getX();
    }

    @Override
    public float getY() {
        return frontCard.getY();
    }

    @Override
    public float getWidth() {
        return frontCard.getWidth();
    }

    @Override
    public float getHeight() {
        return frontCard.getHeight();
    }
}

