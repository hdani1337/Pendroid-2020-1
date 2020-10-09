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
    private TextBox timeBoard;

    public HudStage(MyGame game) {
        super(game);
    }

    @Override
    public void assignment() {
        pause = new Pause(game);
        scoreBoard = new TextBox(game,"0");
        timeBoard = new TextBox(game,"00:00");
    }

    @Override
    public void setSizes() {
        pause.setSize(180,180);
    }

    @Override
    public void setPositions() {
        pause.setPosition(getViewport().getWorldWidth()-pause.getWidth()-15,getViewport().getWorldHeight()-pause.getHeight()-15);
        scoreBoard.setPosition(35,getViewport().getWorldHeight()-scoreBoard.getHeight()-95);
        timeBoard.setPosition(35,getViewport().getWorldHeight()-scoreBoard.getHeight()-15);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(pause);
        addActor(scoreBoard);
        addActor(timeBoard);
    }

    private void refreshScore(){
        if(getScreen() != null && getScreen() instanceof GameScreen){
            if(CardStage.isAct && !CardStage.isGameOver) {
                if (!scoreBoard.text.equals(((GameScreen) getScreen()).cardStage.score)) {
                    scoreBoard.setText(((GameScreen) getScreen()).cardStage.score + "");
                    scoreBoard.setX(35);
                }
                if (!timeBoard.text.equals(getTimeText())) {
                    timeBoard.setText(getTimeText());
                    timeBoard.setX(35);

                }
                if(!scoreBoard.isVisible()) scoreBoard.setVisible(true);
                if(!timeBoard.isVisible()) timeBoard.setVisible(true);
            }else{
                if(scoreBoard.isVisible()) scoreBoard.setVisible(false);
                if(timeBoard.isVisible()) timeBoard.setVisible(false);
            }
        }
    }

    private String getTimeText(){
        String timeText = "";
        long time = ((GameScreen) getScreen()).cardStage.time;
        int minutes = 0;
        int seconds = 0;
        for (long i = 1; i < time; i++){
            if(i%60==0) {
                minutes++;
                seconds = 0;
            }else seconds++;
        }

        if(minutes<10) timeText+="0"+minutes+":";
        else timeText+=minutes+":";
        if(seconds<10) timeText+="0"+seconds;
        else timeText+=seconds;

        return timeText;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        refreshScore();
    }
}
