package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.cehessteg.remember.Hud.TextBox;

public class LoadingStage extends hu.csanyzeg.master.MyBaseClasses.Assets.LoadingStage {

    public static AssetList assetList = new AssetList();
    static {
        assetList.collectAssetDescriptor(TextBox.class,assetList);
    }

    public LoadingStage(MyGame game) {
        super(new ResponseViewport(900), game);
        TextBox load = new TextBox(game,"Betöltés...");
        load.setPosition(getViewport().getWorldWidth()/2-load.getWidth()/2,getViewport().getWorldHeight()/2-load.getHeight()/2);
        addActor(load);
    }

    @Override
    public AssetList getAssetList() {
        return assetList;
    }
}
