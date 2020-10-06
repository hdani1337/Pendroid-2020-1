package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.cehessteg.remember.Hud.Pause;
import hu.cehessteg.remember.Hud.TextBox;
import hu.cehessteg.remember.Screen.GameScreen;

public class HudStage extends PrettyStage {
    public static AssetList assetList = new AssetList();
    static {
        //add assets here
    }

    public static CardStage stage;//Hátha kell a GameStageből valami
    private Pause pause;
    private TextBox scoreBoard;
    private TextBox end;

    public HudStage(MyGame game) {
        super(game);
    }

    @Override
    public void assignment() {
        pause = new Pause(game);
        scoreBoard = new TextBox(game,"0");
        end = new TextBox(game,"vesztés");
    }

    @Override
    public void setSizes() {
        pause.setSize(180,180);
    }

    @Override
    public void setPositions() {
        pause.setPosition(getViewport().getWorldWidth()-pause.getWidth()-15,getViewport().getWorldHeight()-pause.getHeight()-15);
        scoreBoard.setPosition(getViewport().getWorldWidth()/2-scoreBoard.getWidth()/2,getViewport().getWorldHeight()-scoreBoard.getHeight()-15);
        end.setX(500);
    }

    @Override
    public void addListeners() {
        end.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //GameStage.isGameOver = true;
                //GameStage.isAct = false;
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(pause);
        addActor(scoreBoard);
        addActor(end);
    }

    private void refreshScore(){
        if(getScreen() != null && getScreen() instanceof GameScreen){
            /*if(GameStage.isAct && !GameStage.isGameOver) {
                if (!scoreBoard.text.equals(((GameScreen) getScreen()).cardStage.score)) {
                    scoreBoard.setText(((GameScreen) getScreen()).cardStage.score + "");
                    scoreBoard.setX(getViewport().getWorldWidth() / 2 - scoreBoard.getWidth() / 2);
                    if(!scoreBoard.isVisible()) scoreBoard.setVisible(true);
                }
            }else{
                if(scoreBoard.isVisible()) scoreBoard.setVisible(false);
            }*/
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        refreshScore();
    }
}
