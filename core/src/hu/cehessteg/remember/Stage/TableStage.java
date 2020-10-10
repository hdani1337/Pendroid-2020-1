package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;

public class TableStage extends MyStage {
    /**
     * Itt lesz majd a háttér
     * **/
    public static final String tableTexture = "pic/backgrounds/table.jpg";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(tableTexture);
    }

    public TableStage(MyGame game) {
        super(new ResponseViewport(900), game);
        OneSpriteStaticActor tableActor = new OneSpriteStaticActor(game,tableTexture);
        tableActor.setSize(getViewport().getWorldWidth(),getViewport().getWorldHeight());
        addActor(tableActor);
    }
}
