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

public class Card extends OneSpriteStaticActor {
    static ArrayList<String> kartyaTextures = new ArrayList<String>(){};
    static String kartyaHatoldal = "...";

    public static AssetList assetList = new AssetList();
    static {
        for (String asset : kartyaTextures) assetList.addTexture(asset);
        assetList.addTexture(kartyaHatoldal);
    }

    public boolean isSelected;//Ki van e választva a kártya
    public Vector2 koordinatak;//A kártya koordinátái a mátrixban
    public CardType type;//A kártya típusa
    private int id;//A kártya azonosítója, egyelőre nincs használatban
    private CardMethods cardMethods;//Kártya metódusok osztály példánya
    public boolean isShowing;//Látszik e a kártya típusa
    private Color randomColor;//A kártya random színe, deprecated lesz
    private int cheats;//Csalások száma

    public Card(MyGame game, Vector2 koordinatak, SimpleWorld world, CardType cardType, CardMethods cardMethods) {
        super(game, "pic/kartyaHatlap.png");
        this.koordinatak = koordinatak;
        this.id = (int) (koordinatak.x+koordinatak.y*CardStage.matrix.x);
        this.isSelected = false;
        this.type = cardType;
        this.cardMethods = cardMethods;
        this.randomColor = getRandomColor();
        basicStuff(world);
        addTimers();
    }

    /**Alap dolgok beállítása, mint a méret, pozíció, WorldHelper, szín, átmenettel való megjelenés**/
    private void basicStuff(SimpleWorld world){
        setActorWorldHelper(new SimpleWorldHelper(world, this, ShapeType.Rectangle, SimpleBodyType.Sensor));
        setSize(getWidth()*0.0025f,getHeight()*0.0025f);
        setPosition(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight());
        setColor(0,0,0,0);
        ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(1,randomColor.r,randomColor.g,randomColor.b,1);
    }

    /**Timerek hozzáadása**/
    private void addTimers(){
        isShowing = true;

        /**Nehézség számával fordítottan arányos ideig jelenjenek meg a kártyák**/
        addTimer(new TickTimer(6-difficulty,false,new TickTimerListener(){
            @Override
            public void onStop(Timer sender) {
                super.onStop(sender);
                ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(1,1,1,1,1);
                isShowing = false;
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

    /**Új koordináta beállítása
     * Átmenettel oda is helyezi magát a kártya
     * **/
    public void setKoordinatak(Vector2 newKoordinatak){
        koordinatak = newKoordinatak;
        ((SimpleWorldHelper)getActorWorldHelper()).getBody().moveToFixSpeed(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight(),5, PositionRule.LeftBottom);
    }

    /**Kiválasztás megszüntetése egyezésvizsgálatnál**/
    public void deSelect(){
        isSelected = false;
        ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(0.3f,1,1,1,1);
    }

    /**A kártya forgatása**/
    private void flipCard(){
        //KÁRTYA MEGFORDÍTÁSA
        //LEGYEN VALAMI KÖRALAKJA ÉS HANGJA
        if(isSelected){
            //JELENJEN MEG AZ ALAKZAT
            //JELENJEN MEG A KIJELÖLÉS
            ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(0.3f,randomColor.r,randomColor.g,randomColor.b,1);
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
            ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(0.3f,1,1,1,1);
            cardMethods.removeCard(this);

            /**Ha visszavonja a kiválasztást, akkor növelünk egy számlálót
             * Ha ez a számláló páratlan, akkor levonunk valamennyi pontot a nehézség függvényében egy kis véletlenszerűséggel keverve
             * Így csalhat a játékos, mivel látja a kártya típusait
             * **/
            if(cheats++%2==1) CardStage.score-=difficulty*7*(Math.random()*1+1);
        }
    }

    /**A kártya típusa alapján ad vissza színt
     * Ezt majd a textúra fogja helyettesíteni
     * **/
    private Color getRandomColor(){
        switch (type){
            case EGY:
                return Color.BLUE;
            case KETTO:
                return Color.BROWN;
            case HAROM:
                return Color.GOLD;
            case NEGY:
                return Color.RED;
            case OT:
                return Color.CYAN;
            case HAT:
                return Color.PINK;
            case HET:
                return Color.PURPLE;
            case NYOLC:
                return Color.LIME;
            case KILENC:
                return Color.MAGENTA;
            case TIZ:
                return Color.SKY;
            case TIZENEGY:
                return Color.SALMON;
            case TIZENKETTO:
                return Color.GOLDENROD;
            case TIZENHAROM:
                return Color.TAN;
            case TIZENNEGY:
                return Color.YELLOW;
            case TIZENOT:
                return Color.CORAL;
            case TIZENHAT:
                return Color.FIREBRICK;
            case TIZENHET:
                return Color.MAROON;
            case TIZENNYOLC:
                return Color.OLIVE;
            default:
                return Color.GRAY;
        }
    }
}

