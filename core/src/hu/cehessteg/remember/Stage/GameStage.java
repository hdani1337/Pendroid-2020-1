package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.cehessteg.remember.Hud.TextBox;

public class GameStage extends PrettyStage {
    public static AssetList assetList = new AssetList();
    static {
        assetList.collectAssetDescriptor(TextBox.class,assetList);
    }

    public static boolean isAct;
    public static boolean isGameOver;
    private double count;
    private TextBox counter;
    public static long score;

    public GameStage(MyGame game) {
        super(game);
    }

    @Override
    public void assignment() {
        isAct = true;
        isGameOver = false;
        count = 0;
        score = 0;
        counter = new TextBox(game,count+"");

    }

    @Override
    public void setSizes() {

    }

    @Override
    public void setPositions() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(counter);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(isAct){
            //A JÁTÉKMENET KÓDJA ITT FUT
            count+=delta;
            counter.setText(count+"");
        }
    }
}
