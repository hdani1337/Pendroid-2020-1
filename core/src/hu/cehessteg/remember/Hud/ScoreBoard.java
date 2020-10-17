package hu.cehessteg.remember.Hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import hu.cehessteg.remember.Screen.GameScreen;
import hu.cehessteg.remember.Stage.CardStage;
import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.IPrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyGroup;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.UI.MyLabel;

import static hu.cehessteg.remember.Hud.TextBox.VERDANA_FONT;
import static hu.cehessteg.remember.Stage.OptionsStage.gamemode;

public class ScoreBoard extends MyGroup implements IPrettyStage {
    //region AssetList
    public static final String SCOREBOARD_TEXTURE = "pic/szamlalo.png";
    public static final String RETRO_FONT = "font/fontstyle.ttf";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(SCOREBOARD_TEXTURE);
        assetList.addFont(RETRO_FONT, RETRO_FONT, 32, Color.WHITE, AssetList.CHARS);
    }
    //endregion
    //region Változók
    private OneSpriteStaticActor textBackground;//Szöveg háttere
    private MyLabel scoreLabel;//Szöveg label
    private MyLabel timeLabel;//Szöveg label
    private MyLabel levelLabel;//Szöveg label
    private float scale;//Méretezési skála
    //endregion
    //region Konstruktorok
    /**
     * SKÁLÁZÁS NÉLKÜLI KONSTRUKTOR
     * **/
    public ScoreBoard(MyGame game) {
        this(game,1);
    }

    /**
     * FŐ KONSTRUKTOR
     * **/
    public ScoreBoard(MyGame game, float scale){
        super(game);
        this.scale = scale;
        assignment();
        setPositions();
        addListeners();
        setZIndexes();
        addActors();
    }
    //endregion
    //region Interfész metódusai
    @Override
    public void assignment() {
        textBackground = new OneSpriteStaticActor(game, SCOREBOARD_TEXTURE);
        scoreLabel = new MyLabel(game, "Pontszám\n0", new Label.LabelStyle(game.getMyAssetManager().getFont(VERDANA_FONT), Color.WHITE)) {
            @Override
            public void init() {

            }
        };
        timeLabel = new MyLabel(game, "Idő\n00:00", new Label.LabelStyle(game.getMyAssetManager().getFont(VERDANA_FONT), Color.WHITE)) {
            @Override
            public void init() {

            }
        };
        levelLabel = new MyLabel(game, "1.szint", new Label.LabelStyle(game.getMyAssetManager().getFont(VERDANA_FONT), Color.WHITE)) {
            @Override
            public void init() {

            }
        };
    }

    @Override
    public void setSizes() {

    }

    public void setSizes(Viewport viewport) {
        float originalHeight = textBackground.getHeight();
        textBackground.setHeight(viewport.getWorldHeight());
        textBackground.setWidth((textBackground.getHeight()/originalHeight)*textBackground.getWidth()*0.725f);
    }

    @Override
    public void setPositions() {
        scoreLabel.setAlignment(Align.center);
        timeLabel.setAlignment(Align.center);
        levelLabel.setAlignment(Align.center);
        textBackground.setPosition(0,0);
        scoreLabel.setPosition(textBackground.getX()+textBackground.getWidth()/2-scoreLabel.getWidth()/2,textBackground.getY()+textBackground.getHeight()*0.5f-scoreLabel.getHeight()/2);
        timeLabel.setPosition(textBackground.getX()+textBackground.getWidth()/2-timeLabel.getWidth()/2,textBackground.getY()+textBackground.getHeight()*0.83f-timeLabel.getHeight()/2);
        levelLabel.setPosition(textBackground.getX()+textBackground.getWidth()/2-levelLabel.getWidth()/2,textBackground.getY()+textBackground.getHeight()*0.18f-levelLabel.getHeight()/2);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(textBackground);
        addActor(levelLabel);
        addActor(scoreLabel);
        addActor(timeLabel);
    }
    //endregion
    //region Override-ok
    /**
     * VISSZAADJA A HÁTTÉR SZÉLESSÉGÉT
     * **/
    @Override
    public float getWidth() {
        return textBackground.getWidth();
    }

    /**
     * VISSZAADJA A HÁTTÉR MAGASSÁGÁT
     * **/
    @Override
    public float getHeight() {
        return textBackground.getHeight();
    }

    /**
     * SZÖVEG HÁTTÉR SZÉLESSÉGÉNEK MÓDOSÍTÁSA
     * **/
    @Override
    public void setWidth(float width) {
        textBackground.setWidth(width);
        setPositions();
    }

    /**
     * SZÖVEG HÁTTÉR MÉRETÉNEK MÓDOSÍTÁSA
     * **/
    @Override
    public void setSize(float width, float height) {
        textBackground.setWidth(width);
        textBackground.setHeight(height);
        setPositions();
    }
    //endregion
    //region Egyéb metódusok
    /**
     * TEXTBOX ÁTLÁTSZÓSÁGÁNAK BEÁLLÍTÁSA
     * **/
    public void setAlpha(float alpha){
        textBackground.setAlpha(alpha);
        levelLabel.setColor(levelLabel.getColor().r,levelLabel.getColor().g,levelLabel.getColor().b,alpha);
        scoreLabel.setColor(scoreLabel.getColor().r,scoreLabel.getColor().g,scoreLabel.getColor().b,alpha);
        timeLabel.setColor(timeLabel.getColor().r,timeLabel.getColor().g,timeLabel.getColor().b,alpha);
    }

    /**
     * MÉRETEK MÓDOSÍTÁSA SKÁLA ALAPJÁN
     * A SKÁLÁT A KONSTRUKTORBAN KELL ÁTADNI, ALAPESETBEN EZ 1 MARAD
     * **/
    private void setScales(){
        textBackground.setSize(textBackground.getWidth()*scale, textBackground.getHeight()*scale);
        levelLabel.setFontScale(scale);
        scoreLabel.setFontScale(scale);
        timeLabel.setFontScale(scale);
    }

    /**
     * TEXTBOX TÖRLÉSE
     * **/
    private void removeActors(){
        textBackground.remove();
        levelLabel.remove();
        scoreLabel.remove();
        timeLabel.remove();
    }

    /**
     * LABEL SZÍNÉNEK MÓDOSÍTÁSA
     * **/
    public void setColor(Color color){
        scoreLabel.setColor(color);
        levelLabel.setColor(color);
        timeLabel.setColor(color);
    }

    @Override
    public void setPositionCenter() {
        if(getStage() != null)
            setPosition(getStage().getViewport().getWorldWidth()/2-this.getWidth()/2,getStage().getViewport().getWorldHeight()/2-this.getHeight()/2);
    }
    //endregion

    private void refreshScore(){
        if(game.getScreen() != null && game.getScreen() instanceof GameScreen){
            if(CardStage.isAct && !CardStage.isGameOver) {
                if (!scoreLabel.getText().equals("Pontszám\n" + (CardStage.score))) {
                    scoreLabel.setText("Pontszám\n" + (CardStage.score));
                    setPositions();
                    if(CardStage.score<0) scoreLabel.setColor(Color.RED);
                    else scoreLabel.setColor(Color.WHITE);
                }
                if (!timeLabel.getText().equals(getTimeText())) {
                    timeLabel.setText(getTimeText());
                    setPositions();
                    if(CardStage.time<=10 && gamemode == 1){
                        if(CardStage.time%2==1)
                            timeLabel.setColor(Color.RED);
                        else
                            timeLabel.setColor(Color.WHITE);
                    }
                }
                if (!levelLabel.getText().equals((CardStage.level+1)+".szint")) {
                    levelLabel.setText((CardStage.level+1)+".szint");
                    setPositions();
                }
                if(!scoreLabel.isVisible()) scoreLabel.setVisible(true);
                if(!timeLabel.isVisible()) timeLabel.setVisible(true);
                if(!levelLabel.isVisible()) levelLabel.setVisible(true);
            }else{
                if(!scoreLabel.isVisible()) scoreLabel.setVisible(true);
                if(!timeLabel.isVisible()) timeLabel.setVisible(true);
                if(!levelLabel.isVisible()) levelLabel.setVisible(true);
            }
        }
    }

    private String getTimeText(){
        String timeText = "";
        long time = CardStage.time;
        int minutes = 0;
        int seconds = 0;
        for (long i = 1; i < time; i++){
            if(i%60==0) {
                minutes++;
                seconds = 0;
            }else seconds++;
        }

        if (gamemode == 1) timeText = "Hátralévő idő\n";
        else timeText += "Eltelt idő\n";

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

