package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;
import hu.csanyzeg.master.MyBaseClasses.UI.MyLabel;
import hu.cehessteg.remember.Hud.Logo;
import hu.cehessteg.remember.Hud.TextBox;
import hu.cehessteg.remember.SoundManager;

import static hu.cehessteg.remember.MemoryGame.muted;
import static hu.cehessteg.remember.MemoryGame.preferences;
import static hu.cehessteg.remember.Hud.TextBox.RETRO_FONT;
import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class ShopStage extends PrettyStage {
    //region AssetList
    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(MENU_BG_TEXTURE);
    }
    //endregion
    //region Konstruktor
    public ShopStage(MyGame game) {
        super(new ResponseViewport(900), game);
    }
    //endregion
    //region Változók
    /**
     * Actorok
     * **/
    private OneSpriteStaticActor MenuBackground;
    private TextBox back;//Vissza a menübe gomb
    //private Coin coin;//Pénz ikon
    public MyLabel coinText;//Pénz mennyiség label
    private Logo shopLogo;//Bolt logo

    //endregion
    //region Absztrakt metódusok
    @Override
    public void assignment() {
        SoundManager.assign();
        if(!muted && SoundManager.menuMusic != null) SoundManager.menuMusic.play();
        MenuBackground = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
        back = new TextBox(game, "Vissza a menübe");
        //coin = new Coin(game, true);
        coinText = new MyLabel(game, "10000", new Label.LabelStyle(game.getMyAssetManager().getFont(RETRO_FONT), Color.WHITE)) {
            @Override
            public void init() {

            }
        };
        shopLogo = new Logo(game, Logo.LogoType.SHOP);
        //coinText.setText(Coin.coin+"");
    }

    @Override
    public void setSizes() {
        if(getViewport().getWorldWidth() > MenuBackground.getWidth()) MenuBackground.setWidth(getViewport().getWorldWidth());
        //coin.setSize(coin.getWidth()*0.7f,coin.getHeight()*0.7f);
        back.setPosition(getViewport().getWorldWidth() - (back.getWidth() + 45),50);
        //coin.setPosition(15, getViewport().getWorldHeight()-15-coin.getHeight());
        //coinText.setPosition(coin.getX() + coin.getWidth() + 10, coin.getY() + coin.getHeight()/4);
        shopLogo.setPosition(getViewport().getWorldWidth()/2 - shopLogo.getWidth()/2, getViewport().getWorldHeight() - shopLogo.getHeight()*1.8f);
    }

    @Override
    public void setPositions() {
        if(getViewport().getWorldWidth() < MenuBackground.getWidth()) MenuBackground.setX((getViewport().getWorldWidth()-MenuBackground.getWidth())/2);
    }

    @Override
    public void addListeners() {
        /**
         * Vissza a menübe
         * **/
        back.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                /**
                 * Változók mentése
                 * **/
                //preferences.putLong("coin", Coin.coin);
                preferences.flush();
                setBack = true;//Kifele áttűnés
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    /**
     * Minden actort hozzáadunk a stagehez, csak a Visible tulajdonságaikat állítjuk át
     * **/
    @Override
    public void addActors() {
        addActor(MenuBackground);
        addActor(shopLogo);
        //addActor(coin);
        addActor(coinText);
        addActor(back);
        MenuBackground.setAlpha(0.25f);
    }
    //endregion
    //region Act metódusai
    float alpha = 0;
    float bgAlpha = 1;
    boolean setBack = false;

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!setBack) fadeIn();
        else fadeOut();
        setBackgroundFade();
    }
    //endregion
    //region Áttűnések metódusai
    /**
     * Actorok átlátszóságának egyidejű beállítása
     * **/
    private void setAlpha(){
        //coin.setColor(1, 1, 1, alpha);
        coinText.setColor(1, 1, 1, alpha);
        shopLogo.setColor(1, 1, 1, alpha);
        back.setAlpha(alpha);
    }

    /**
     * Áttűnéssel jönnek be az actorok
     * **/
    private void fadeIn(){
        if (alpha < 0.95) {
            alpha += 0.05;
            setAlpha();
        }
        else alpha = 1;
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
     * Háttér átlátszóságának beállítása
     * **/
    private void setBackgroundFade(){
        if(bgAlpha>0.25 && !setBack){
            bgAlpha-=0.025;
            MenuBackground.setAlpha(bgAlpha);
        }
    }
    //endregion
}
