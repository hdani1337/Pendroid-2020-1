package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import hu.cehessteg.remember.Hud.OptionSwitch;
import hu.cehessteg.remember.Hud.OptionSwitchType;
import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;
import hu.cehessteg.remember.Hud.Logo;
import hu.cehessteg.remember.Hud.TextBox;
import hu.cehessteg.remember.SoundManager;

import static hu.cehessteg.remember.MemoryGame.muted;
import static hu.cehessteg.remember.MemoryGame.preferences;
import static hu.cehessteg.remember.Stage.InfoStage.BACKBUTTON_TEXTURE;
import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class OptionsStage extends PrettyStage {
    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(MENU_BG_TEXTURE);
        assetList.collectAssetDescriptor(TextBox.class,assetList);
        assetList.collectAssetDescriptor(Logo.class,assetList);
    }

    public static int difficulty = preferences.getInteger("difficulty");
    public static int gamemode = preferences.getInteger("gamemode");
    public static int size = preferences.getInteger("size");

    private OneSpriteStaticActor MenuBackground;

    private Logo optionsLogo;

    private OneSpriteStaticActor backButton;
    private OptionSwitch sizeButton;
    private OptionSwitch gameModeButton;
    private OptionSwitch difficultyButton;

    private boolean setBack;

    public OptionsStage(MyGame game) {
        super(game);
    }

    @Override
    public void assignment() {
        SoundManager.assign();
        if(!muted && SoundManager.menuMusic != null)
            SoundManager.menuMusic.play();
        setBack = false;
        MenuBackground = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
        backButton = new OneSpriteStaticActor(game,BACKBUTTON_TEXTURE);
        gameModeButton = new OptionSwitch(game, OptionSwitchType.GAMEMODE);
        difficultyButton = new OptionSwitch(game, OptionSwitchType.DIFFICULTY);
        sizeButton = new OptionSwitch(game,OptionSwitchType.SIZE);
        optionsLogo = new Logo(game, Logo.LogoType.OPTIONS);
    }

    @Override
    public void setSizes() {
        if(getViewport().getWorldWidth() > MenuBackground.getWidth()) MenuBackground.setWidth(getViewport().getWorldWidth());
        if(getViewport().getWorldHeight() > MenuBackground.getHeight()) MenuBackground.setHeight(getViewport().getWorldHeight());
        backButton.setSize(160,160);
    }

    @Override
    public void setPositions() {
        backButton.setRotation(180);
        if(getViewport().getWorldWidth() < MenuBackground.getWidth()) MenuBackground.setX((getViewport().getWorldWidth()-MenuBackground.getWidth())/2);
        backButton.setPosition(getViewport().getWorldWidth() - backButton.getWidth()-16,16);
        optionsLogo.setPosition(getViewport().getWorldWidth()/2 - optionsLogo.getWidth()/2, getViewport().getWorldHeight() - optionsLogo.getHeight()*1.15f);
        gameModeButton.setPosition(getViewport().getWorldWidth()/2-gameModeButton.getWidth()/2,getViewport().getWorldHeight()-570);
        difficultyButton.setPosition(getViewport().getWorldWidth()/2-difficultyButton.getWidth()/2,getViewport().getWorldHeight()-420);
        sizeButton.setPosition(getViewport().getWorldWidth()/2-sizeButton.getWidth()/2,getViewport().getWorldHeight()-720);
    }

    @Override
    public void addListeners() {
        backButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                preferences.putInteger("difficulty",difficulty);
                preferences.putInteger("gamemode",gamemode);
                preferences.putInteger("size",size);
                preferences.flush();
                setBack = true;
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(MenuBackground);
        addActor(optionsLogo);
        addActor(difficultyButton);
        addActor(backButton);
        addActor(gameModeButton);
        addActor(sizeButton);
    }
    //endregion
    //region Act metódusai
    float alpha = 0;
    float bgAlpha = 1;
    @Override
    public void act(float delta) {
        super.act(delta);
        if(!setBack) fadeIn();
        else fadeOut();
        setBackgroundAlpha();
    }

    /**
     * Áttűnéssel jönnek be az actorok
     * **/
    private void fadeIn(){
        if (alpha < 0.95) alpha += 0.025;
        else alpha = 1;
        setAlpha();
    }

    /**
     * Áttűnéssel mennek ki az actorok
     * **/
    private void fadeOut(){
        if (alpha > 0.05) {
            setAlpha();
            alpha -= 0.05;
            if(bgAlpha<0.95) bgAlpha+= 0.05;
            MenuBackground.setAlpha(bgAlpha);
        } else {
            //Ha már nem látszanak akkor megyünk vissza a menübe
            alpha = 0;
            setAlpha();
            game.setScreenBackByStackPopWithPreloadAssets(new LoadingStage(game));
            addTimer(new TickTimer(1,false,new TickTimerListener(){
                @Override
                public void onTick(Timer sender, float correction) {
                    super.onTick(sender, correction);
                    setBack = false;
                }
            }));
        }
    }

    /**
     * Háttér átlátszóságát állítja be
     * **/
    private void setBackgroundAlpha(){
        if(bgAlpha>0.65 && !setBack){
            bgAlpha-=0.025;
            MenuBackground.setAlpha(bgAlpha);
        }
    }

    /**
     * Actorok átlátszóságának egyidejű beállítása
     * **/
    private void setAlpha(){
        optionsLogo.setAlpha(alpha);
        backButton.setAlpha(alpha);
        gameModeButton.setAlpha(alpha);
        difficultyButton.setAlpha(alpha);
        sizeButton.setAlpha(alpha);
    }
    //endregion
}
