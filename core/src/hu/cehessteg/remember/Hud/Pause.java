package hu.cehessteg.remember.Hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hu.cehessteg.remember.Screen.GameScreen;
import hu.cehessteg.remember.Stage.CardStage;
import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;

public class Pause extends OneSpriteStaticActor {
    //region AssetList
    public static final String PAUSE_TEXTURE = "pic/gombok/pause.png";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(PAUSE_TEXTURE);
    }
    //endregion
    //region Konstruktor
    public Pause(MyGame game) {
        super(game, PAUSE_TEXTURE);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Csak akkor állítjuk meg a játékmenetet, ha a Pause gomb a GameScreenen vagy BossScreenen van
                if(getStage() != null && getStage() instanceof MyStage) {
                    if (((MyStage) getStage()).getScreen() != null) {
                        if (((MyStage) getStage()).getScreen() instanceof GameScreen)
                            CardStage.isAct = false;
                    }
                }
            }
        });
    }
    //endregion
}