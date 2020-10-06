package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.cehessteg.remember.Hud.TextBox;
import hu.cehessteg.remember.Screen.GameScreen;

import static hu.cehessteg.remember.MemoryGame.muted;
import static hu.cehessteg.remember.MemoryGame.preferences;
import static hu.cehessteg.remember.SoundManager.gameMusic;

public class PauseStage extends PrettyStage {
    //region AssetList
    public static final String BLACK_TEXTURE = "pic/fekete.png";

    public static AssetList assetList = new AssetList();
    static {
        assetList.collectAssetDescriptor(TextBox.class,assetList);
        assetList.addTexture(BLACK_TEXTURE);
    }
    //endregion
    //region Változók
    private TextBox info;
    private TextBox pontok;
    private TextBox again;
    private TextBox menu;

    private OneSpriteStaticActor black;
    //endregion
    //region Konstruktor
    public PauseStage(MyGame game) {
        super(new ResponseViewport(900), game);
    }
    //endregion
    //region Absztrakt metódusok
    @Override
    public void assignment() {
        info = new TextBox(game, "Megállítva",2f);
        pontok = new TextBox(game, "Pontszámok\nHamarosan!");
        again = new TextBox(game, "Folytatás",1.5f);
        menu = new TextBox(game, "Menü",1.5f);

        black = new OneSpriteStaticActor(game, BLACK_TEXTURE);

        addedActors = false;
        alpha = 0;
    }

    @Override
    public void setSizes() {
        black.setSize(getViewport().getWorldWidth(),getViewport().getWorldHeight());
        menu.setWidth(menu.getWidth()*1.1f);
        pontok.setWidth(pontok.getWidth()*0.7f);
    }

    @Override
    public void setPositions() {
        info.setPosition(getViewport().getWorldWidth()/2-info.getWidth()/2,getViewport().getWorldHeight()*0.75f);
        pontok.setPosition(getViewport().getWorldWidth()/2-pontok.getWidth()/2,getViewport().getWorldHeight()*0.52f);
        again.setPosition(getViewport().getWorldWidth()/2-again.getWidth()/2,getViewport().getWorldHeight()*0.37f);
        menu.setPosition(getViewport().getWorldWidth()/2-menu.getWidth()/2,getViewport().getWorldHeight()*0.25f);
    }

    @Override
    public void addListeners() {
        again.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(getScreen() != null) {
                    if (getScreen() instanceof GameScreen)
                        CardStage.isAct = true;
                }
            }
        });

        menu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(!muted && gameMusic != null) {
                    gameMusic.stop();
                }
                game.setScreenBackByStackPopWithPreloadAssets(new LoadingStage(game));
                //preferences.putLong("coin", Coin.coin);
                preferences.flush();
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        /**
         * MINDEN ACTOR LÁTHATATLAN LESZ
         * EZUTÁN HOZZÁADJUK AZ ACTOROKAT A STAGEHEZ
         * ÁTVÁLTJUK AZ {@link addedActors} VÁLTOZÓT IGAZRA, ÍGY CSAK EGYSZER KERÜLNEK FEL A STAGERE AZ ACTOROK, MERT TÖBBSZÖR NEM HÍVÓDIK MEG EZ A METÓDUS
         * **/
        black.setAlpha(0);
        info.setAlpha(0);
        pontok.setAlpha(0);
        again.setAlpha(0);
        menu.setAlpha(0);

        addActor(black);
        addActor(info);
        addActor(pontok);
        addActor(again);
        addActor(menu);

        addedActors = true;
    }

    @Override
    public void afterInit() {
        /**
         * MIVEL AZ addActors() METÓDUS AUTOMATIKUSAN LEFUT, EZÉRT EGYSZER HOZZÁADJA A GOMBOKAT A STAGEHEZ 0-ÁS ALPHÁVAL
         * EZÉRT A STAGE LÉTREHOZÁSA UTÁN EL KELL ŐKET TÁVOLÍTANI, NEHOGY VÉLETLENÜL KATTINTSUNK A GOMBOKRA
         * **/
        black.remove();
        info.remove();
        pontok.remove();
        again.remove();
        menu.remove();
        addedActors = false;
    }
    //endregion
    //region Act metódusai
    private boolean addedActors;
    private float alpha;

    @Override
    public void act(float delta) {
        super.act(delta);
        if(getScreen() != null) {
            if (getScreen() instanceof GameScreen) {
                if (!CardStage.isAct && !CardStage.isGameOver) pause(null);
                else if (CardStage.isAct && addedActors) resume(null);
            }
        }
    }

    private void pause(Music music){
        if(getScreen() != null && (getScreen() instanceof GameScreen)){
            if(!pontok.text.equals("Jelenlegi pontszámod\n"+CardStage.score)) {
                pontok.setText("Jelenlegi pontszámod\n"+CardStage.score);
                pontok.setX(getViewport().getWorldWidth()/2-pontok.getWidth()/2);
            }
        }
        //Adjuk hozzá a gombokat a stagehez ha még nincsenek rajta
        if(!addedActors) {
            addActors();
            if(music != null) music.pause();
        }

        //Áttűnés
        if(alpha < 0.95f)
            alpha += 0.05f;
        else
            alpha = 1;

        black.setAlpha(alpha*0.6f);
        info.setAlpha(alpha);
        pontok.setAlpha(alpha);
        again.setAlpha(alpha);
        menu.setAlpha(alpha);
        //Áttűnés vége
    }

    private void resume(Music music){
        //Áttűnéssel tűnnek el a stageről
        if(alpha > 0.05f) {
            //if(!music.isPlaying()) music.play();
            alpha -= 0.05f;
            black.setAlpha(alpha*0.6f);
            info.setAlpha(alpha);
            pontok.setAlpha(alpha);
            again.setAlpha(alpha);
            menu.setAlpha(alpha);
        }
        //Ha már láthatatlanok, akkor eltávolítjuk őket a stageről
        else {
            alpha = 0;
            black.remove();
            info.remove();
            pontok.remove();
            again.remove();
            menu.remove();
            addedActors = false;
        }
    }
    //endregion
}

