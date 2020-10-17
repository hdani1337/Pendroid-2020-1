package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;
import hu.cehessteg.remember.Hud.Logo;
import hu.cehessteg.remember.Hud.TextBox;
import hu.csanyzeg.master.MyBaseClasses.UI.MyLabel;

import static hu.cehessteg.remember.Hud.TextBox.RETRO_FONT;
import static hu.cehessteg.remember.Hud.TextBox.VERDANA_FONT;
import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class InfoStage extends PrettyStage {
    public static String BACKBUTTON_TEXTURE = "pic/gombok/play_kek.png";
    public static String TEXTBACKGROUND_TEXTURE = "pic/ui/szoveg.png";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(MENU_BG_TEXTURE);
        assetList.addTexture(BACKBUTTON_TEXTURE);
    }

    public InfoStage(MyGame game) {
        super(game);
    }

    private OneSpriteStaticActor bg;
    private OneSpriteStaticActor textBg;
    private MyLabel text;
    private OneSpriteStaticActor back;
    private Logo infoLogo;

    private boolean setBack;
    //endregion
    //region Absztrakt metódusok
    @Override
    public void assignment() {
        //SoundManager.assign();
        //if(!muted)
        //    SoundManager.menuMusic.play();
        bg = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
        back = new OneSpriteStaticActor(game, BACKBUTTON_TEXTURE);
        back.setRotation(180);
        textBg = new OneSpriteStaticActor(game,TEXTBACKGROUND_TEXTURE);
        infoLogo = new Logo(game, Logo.LogoType.INFO);

        String infoText = "Ez a játék a 2020-ban megrendezett Pendroid verseny 1. fordulójára készült.\n" +
                "A játék célja az, hogy minél hamarabb megtaláld a kártyák párjait.\n" +
                "Vigyázz, mert ha tévedsz vagy csalni próbálsz, pontokat fogsz veszteni!\n" +
                "Ha kihívásra vágysz, az Arcade játékmód lesz a kedvenced, ugyanis ekkor\n" +
                "mindössze két és fél perc áll rendelkezésedre a játékra, ha pedig csak\n" +
                "kikapcsolódnál, a Zen módban korlátok nélkül keresgélhetsz.\n" +
                "Továbbá még eldöntheted, hogy fix paklimérettel szeretnél játszani,\n" +
                "vagy a nehézség és szint alapján folyamatosan növekedjen.\n" +
                "Ezeket a paramétereket az Opciók menüpontban adhatod meg.\n" +
                "\nJó játékot kíván a Céhessteg csapata!";

        text = new MyLabel(game, infoText, new Label.LabelStyle(game.getMyAssetManager().getFont(VERDANA_FONT), Color.BLACK)) {
            @Override
            public void init() {

            }
        };
    }

    @Override
    public void setSizes() {
        if(getViewport().getWorldWidth() > bg.getWidth()) bg.setWidth(getViewport().getWorldWidth());
        if(getViewport().getWorldHeight() > bg.getHeight()) bg.setHeight(getViewport().getWorldHeight());
        infoLogo.setSize(infoLogo.getWidth()*0.9f,infoLogo.getHeight()*0.9f);
        textBg.setSize(text.getWidth()+60,text.getHeight()+30);
        back.setSize(160,160);
    }

    @Override
    public void setPositions() {
        if(getViewport().getWorldWidth() < bg.getWidth()) bg.setX((getViewport().getWorldWidth()-bg.getWidth())/2);
        back.setPosition(getViewport().getWorldWidth() - back.getWidth()-16,16);
        infoLogo.setPosition(getViewport().getWorldWidth()/2 - infoLogo.getWidth()/2, getViewport().getWorldHeight() - infoLogo.getHeight()*1.1f);
        text.setAlignment(Align.center);
        text.setPosition(getViewport().getWorldWidth()/2-text.getWidth()/2,getViewport().getWorldHeight()/2-text.getHeight()/2-20);
        textBg.setPosition(text.getX()-30,text.getY()-15);
    }

    @Override
    public void addListeners() {
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setBack = true;
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(bg);
        addActor(textBg);
        addActor(text);
        addActor(infoLogo);
        addActor(back);
    }
    //endregion
    //region Act metódusai
    float alpha = 0;
    float bgAlpha = 1;

    @Override
    public void act(float delta) {
        super.act(delta);
        if(!setBack) {
            //Áttűnéssel jönnek be az actorok
            if (alpha < 0.95) alpha += 0.025;
            else alpha = 1;
            setAlpha();
        }
        else
        {
            //Áttűnéssel mennek ki az actorok
            if (alpha > 0.05) {
                setAlpha();
                alpha -= 0.05;
                if(bgAlpha<0.95) bgAlpha+= 0.05;
                bg.setAlpha(bgAlpha);
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

        if(bgAlpha>0.65 && !setBack){
            bgAlpha-=0.025;
            bg.setAlpha(bgAlpha);
        }
    }

    /**
     * Actorok átlátszóságának egyidejű beállítása
     * **/
    private void setAlpha(){
        infoLogo.setAlpha(alpha);
        back.setAlpha(alpha);
        textBg.setAlpha(alpha);
        text.setColor(0,0,0,alpha);
    }
    //endregion
}
