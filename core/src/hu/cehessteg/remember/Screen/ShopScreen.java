package hu.cehessteg.remember.Screen;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyScreen;
import hu.cehessteg.remember.Stage.ShopStage;

public class ShopScreen extends MyScreen {
    //region AssetList
    public static AssetList assetList = new AssetList();
    static {
        assetList.collectAssetDescriptor(ShopStage.class, assetList);
    }
    //endregion
    //region Konstruktor
    public ShopScreen(MyGame game) {
        super(game);
    }
    //endregion
    //region Absztrakt metódusok
    @Override
    protected void afterAssetsLoaded() {
        addStage(new ShopStage(game),1,true);
    }

    @Override
    public AssetList getAssetList() {
        return assetList;
    }

    @Override
    public void init() {

    }
    //endregion
}