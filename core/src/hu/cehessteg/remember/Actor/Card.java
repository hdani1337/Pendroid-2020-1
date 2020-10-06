package hu.cehessteg.remember.Actor;

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

    public Card(MyGame game, Vector2 koordinatak, SimpleWorld world) {
        super(game, "pic/logos/csany.png");

        this.koordinatak = koordinatak;
        this.world = world;
        this.id = (int) (koordinatak.x+koordinatak.y*CardStage.matrix.x);
        this.isSelected = false;

        setActorWorldHelper(new SimpleWorldHelper(world, this, ShapeType.Rectangle, SimpleBodyType.Sensor));
        setSize(getWidth()*0.006f,getHeight()*0.006f);
        setPosition(koordinatak.x*1.25f,9-(koordinatak.y*1.25f)-getHeight());
        setColor(1,1,1,0.25f);

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Card.this.isSelected = !Card.this.isSelected;
                flipCard();
            }
        });
    }

    public void deSelect(){
        isSelected = false;
        flipCard();
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
            ((SimpleWorldHelper)getActorWorldHelper()).getBody().colorTo(1,1,1,1,0.25f);
            CardStage.removeCard(this);
        }
    }
}

enum CardType{
    EZEK, LESZNEK, A, KARTYA, TIPUSAI, MITTUDOMEN, JA
}
