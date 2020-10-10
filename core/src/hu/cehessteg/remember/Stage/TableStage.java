package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.Timers.PermanentTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.PermanentTimerListener;

import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class TableStage extends MyStage {
    /**
     * Itt lesz majd a háttér
     * **/
    public static final String tableTexture = "pic/backgrounds/table.jpg";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(tableTexture);
        assetList.addTexture(MENU_BG_TEXTURE);
    }

    public TableStage(MyGame game) {
        super(new ResponseViewport(900), game);
        OneSpriteStaticActor tableActor = new OneSpriteStaticActor(game,tableTexture);
        OneSpriteStaticActor tableActorBlur = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
        tableActor.setSize(getViewport().getWorldWidth(),getViewport().getWorldHeight());
        tableActorBlur.setSize(getViewport().getWorldWidth(),getViewport().getWorldHeight());
        addActor(tableActor);
        addActor(tableActorBlur);

        final float[] alpha = {1};
        addTimer(new PermanentTimer(new PermanentTimerListener(){
            @Override
            public void onTick(PermanentTimer sender, float correction) {
                super.onTick(sender, correction);
                tableActorBlur.setColor(1,1,1, alpha[0]);
                alpha[0] -=0.005f;
                if(alpha[0] <= 0.005f) {
                    tableActorBlur.remove();
                    sender.stop();
                }
            }
        }));
    }
}
