package hu.cehessteg.remember.Actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import hu.cehessteg.remember.Stage.CardStage;
import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.ShapeType;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleBodyType;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorld;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldHelper;

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

    public Card(MyGame game, Vector2 koordinatak, SimpleWorld world, CardType cardType) {
        super(game, "pic/kartyaHatlap.png");

        this.koordinatak = koordinatak;
        this.world = world;
        this.id = (int) (koordinatak.x+koordinatak.y*CardStage.matrix.x);
        this.isSelected = false;
        this.type = cardType;

        setActorWorldHelper(new SimpleWorldHelper(world, this, ShapeType.Rectangle, SimpleBodyType.Sensor));
        setSize(getWidth()*0.0025f,getHeight()*0.0025f);
        setPosition(koordinatak.x*1.3f,9-(koordinatak.y*1.3f)-getHeight());
        setColor(getRandomColor());

        addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Card.this.isSelected = !Card.this.isSelected;
                flipCard();
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public void deSelect(){
        isSelected = false;
        ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getRandomColor());
    }

    private void flipCard(){
        //KÁRTYA MEGFORDÍTÁSA
        //LEGYEN VALAMI KÖRALAKJA ÉS HANGJA
        if(isSelected){
            //JELENJEN MEG AZ ALAKZAT
            //JELENJEN MEG A KIJELÖLÉS
            ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorTo(1,1,1,1,1);
            CardStage.addCard(this);
        }else{
            //FORDULJON VISSZA A KÁRTYA
            //TŰNJÖN EL A KIJELÖLÉS
            ((SimpleWorldHelper)getActorWorldHelper()).actor.setColor(getRandomColor());
            CardStage.removeCard(this);
        }
        System.out.println(koordinatak);
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
            default:
                return Color.GRAY;
        }
    }
}

