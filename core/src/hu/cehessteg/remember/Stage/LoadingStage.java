package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.cehessteg.remember.Hud.TextBox;

import static hu.cehessteg.remember.Stage.IntroStage.CSAPAT_TEXTURE;

public class LoadingStage extends hu.csanyzeg.master.MyBaseClasses.Assets.LoadingStage {

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(CSAPAT_TEXTURE).protect = true;
    }

    public LoadingStage(MyGame game) {
        super(new ResponseViewport(900), game);
        addActor(new OneSpriteStaticActor(game, CSAPAT_TEXTURE) {
            @Override
            public void init() {
                super.init();
                setPosition(getViewport().getWorldWidth() / 2 - this.getWidth() / 2, getViewport().getWorldHeight() / 2 - this.getHeight() / 2);
            }

            @Override
            public void act(float delta) {
                super.act(delta);
                setRotation(getRotation() - 10);
            }
        });
    }

    @Override
    public AssetList getAssetList() {
        return assetList;
    }
}
