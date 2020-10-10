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

import static hu.cehessteg.remember.Stage.CardStage.isAct;
import static hu.cehessteg.remember.Stage.CardStage.isGameOver;
import static hu.cehessteg.remember.Stage.CardStage.isShuffling;

public class Card extends OneSpriteStaticActor {
    static ArrayList<String> kartyaTextures = new ArrayList<String>(){};
    static String kartyaHatoldal = "...";

    public static AssetList assetList = new AssetList();
    static {
        for (String asset : kartyaTextures) assetList.addTexture(asset);
        assetList.addTexture(kartyaHatoldal);
    }

    private SimpleWorld world;
    public boolean isSelected;
    public Vector2 koordinatak;
    public CardType type;
    private int id;
    private CardMethods cardMethods;

    public Card(MyGame game, Vector2 koordinatak, SimpleWorld world, CardType cardType, CardMethods cardMethods) {
        super(game, "pic/kartyaHatlap.png");

        this.koordinatak = koordinatak;
        this.world = world;
        this.id = (int) (koordinatak.x+koordinatak.y*CardStage.matrix.x);
        this.isSelected = false;
        this.type = cardType;
        this.cardMethods = cardMethods;

        setActorWorldHelper(new SimpleWorldHelper(world, this, ShapeType.Rectangle, SimpleBodyType.Sensor));
        setSize(getWidth()*0.0025f,getHeight()*0.0025f);
        setPosition(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight());
        setColor(0,0,0,0);
        Color randomColor = getRandomColor();
        ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorToFixTime(1,randomColor.r,randomColor.g,randomColor.b,0.5f);

        addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(isAct && !isGameOver && getColor().a!=0) {
                    if (!isShuffling) {
                        Card.this.isSelected = !Card.this.isSelected;
                        flipCard();
                    }
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public void setKoordinatak(Vector2 newKoordinatak){
        koordinatak = newKoordinatak;
        ((SimpleWorldHelper)getActorWorldHelper()).getBody().moveToFixSpeed(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight(),5, PositionRule.LeftBottom);
    }

    public void deSelect(){
        isSelected = false;
        ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getRandomColor());
        ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getColor().r,getColor().g,getColor().b,0.5f);
    }

    private void flipCard(){
        //KÁRTYA MEGFORDÍTÁSA
        //LEGYEN VALAMI KÖRALAKJA ÉS HANGJA
        if(isSelected){
            //JELENJEN MEG AZ ALAKZAT
            //JELENJEN MEG A KIJELÖLÉS
            ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getColor().r,getColor().g,getColor().b,1);
            cardMethods.addCard(this);
        }else{
            //FORDULJON VISSZA A KÁRTYA
            //TŰNJÖN EL A KIJELÖLÉS
            ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getRandomColor());
            ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getColor().r,getColor().g,getColor().b,0.5f);
            cardMethods.removeCard(this);
        }
    }

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

