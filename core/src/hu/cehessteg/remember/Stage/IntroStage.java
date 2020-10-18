package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.UI.MyLabel;
import hu.cehessteg.remember.Screen.MenuScreen;

import static hu.cehessteg.remember.Hud.TextBox.RETRO_FONT;
import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class IntroStage extends PrettyStage {
    public static final String GDX_TEXTURE = "pic/logos/gdx.png";
    public static final String CSANY_TEXTURE = "pic/logos/csany.png";
    public static final String CSAPAT_TEXTURE = "pic/logos/csapat.png";
    public static final String PENDROID_TEXTURE = "pic/logos/pendroid.png";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(MENU_BG_TEXTURE);
        assetList.addTexture(GDX_TEXTURE);
        assetList.addTexture(CSANY_TEXTURE);
        assetList.addTexture(CSAPAT_TEXTURE);
        assetList.addTexture(PENDROID_TEXTURE);
        assetList.addFont(RETRO_FONT, RETRO_FONT, 32, Color.WHITE, AssetList.CHARS);
    }
    private OneSpriteStaticActor bg;
    private OneSpriteStaticActor gdxLogo;
    private OneSpriteStaticActor csanyLogo;
    private OneSpriteStaticActor csapatLogo;
    private OneSpriteStaticActor pendroidLogo;
    private MyLabel copyright;
    //endregion
    //region Konstruktor
    public IntroStage(MyGame game) {
        super(new ResponseViewport(900), game);
    }
    //endregion
    //region Absztrakt metódusok
    @Override
    public void assignment() {
        bg = new OneSpriteStaticActor(game, MENU_BG_TEXTURE);
        gdxLogo = new OneSpriteStaticActor(game, GDX_TEXTURE);
        csanyLogo = new OneSpriteStaticActor(game, CSANY_TEXTURE);
        csapatLogo = new OneSpriteStaticActor(game, CSAPAT_TEXTURE);
        pendroidLogo = new OneSpriteStaticActor(game, PENDROID_TEXTURE);

        copyright = new MyLabel(game,"Copyright 2020 Céhessteg. Minden jog fenntartva.", new Label.LabelStyle(game.getMyAssetManager().getFont(RETRO_FONT), Color.WHITE)) {
            @Override
            public void init() {
                setFontScale(1);
                setAlignment(0);
            }
        };
    }

    @Override
    public void setSizes() {
        if(getViewport().getWorldWidth() > bg.getWidth()) bg.setWidth(getViewport().getWorldWidth());
        if(getViewport().getWorldHeight() > bg.getHeight()) bg.setHeight(getViewport().getWorldHeight());
    }

    @Override
    public void setPositions() {
        if(getViewport().getWorldWidth() < bg.getWidth()) bg.setX((getViewport().getWorldWidth()-bg.getWidth())/2);

        gdxLogo.setPosition(getViewport().getWorldWidth()/2-gdxLogo.getWidth()/2,getViewport().getWorldHeight()/2-gdxLogo.getHeight()/2);
        pendroidLogo.setPosition(getViewport().getWorldWidth()/2-pendroidLogo.getWidth()-50, getViewport().getWorldHeight()/2-pendroidLogo.getHeight()/2);
        csanyLogo.setPosition(getViewport().getWorldWidth()/2+50, getViewport().getWorldHeight()/2-csanyLogo.getHeight()/2);
        copyright.setPosition(getViewport().getWorldWidth()/2-copyright.getWidth()/2, 20);
        csapatLogo.setPosition(getViewport().getWorldWidth()/2-csapatLogo.getWidth()/2, getViewport().getWorldHeight()/2-csapatLogo.getHeight()/2);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(bg);
        addActor(gdxLogo);
        addActor(csanyLogo);
        addActor(copyright);
        addActor(pendroidLogo);
        addActor(csapatLogo);

        for (Actor actor : getActors()) actor.setColor(1,1,1,0);
    }
    //endregion
    //region Áttűnés metódusai
    private float pElapsed;
    private byte index = 0;
    private float alpha = 0;

    @Deprecated
    private void fadeIn(OneSpriteStaticActor... actor) {
        if (alpha < 0.95) alpha += 0.05;
        else alpha = 1;

        for (OneSpriteStaticActor actor1 : actor)
        {
            actor1.setAlpha(alpha);
        }
    }

    @Deprecated
    private void fadeOut(OneSpriteStaticActor... actor) {
        if (alpha > 0.05) alpha -= 0.05;
        else {
            alpha = 0;
            pElapsed = 0;
            index++;
        }

        for (OneSpriteStaticActor actor1 : actor)
        {
            actor1.setAlpha(alpha);
        }
    }
    //endregion
    //region Act metódusai
    @Override
    public void act(float delta) {
        super.act(delta);
        if((1/6.0f) * elapsedTime < 1) bg.setAlpha((1/6.0f) * elapsedTime);
        else bg.setAlpha(1);

        switch (index) {
            case 0: {
                pElapsed += delta;
                if (pElapsed < 0.75) fadeIn(gdxLogo);
                else if (pElapsed > 1.5) fadeOut(gdxLogo);
                break;
            }

            case 1: {
                pElapsed += delta;
                if (pElapsed < 0.75) fadeIn(csanyLogo,pendroidLogo);
                else if (pElapsed > 1.5) fadeOut(csanyLogo,pendroidLogo);
                break;
            }

            case 2:{
                pElapsed += delta;
                if (pElapsed < 0.75) {
                    fadeIn(csapatLogo);
                } else if (pElapsed > 1){
                    copyright.setColor(1,1,1,elapsedTime-4.8f);
                    if (pElapsed > 2.5) {
                        fadeOut(csapatLogo);
                        copyright.setColor(1,1,1, alpha);
                    }
                }
                break;
            }
        }

        if(elapsedTime > 6) {
            csapatLogo.remove();
            game.setScreenWithPreloadAssets(MenuScreen.class, true, new LoadingStage(game));
        }
    }
}
