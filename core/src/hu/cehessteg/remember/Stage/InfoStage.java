package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class InfoStage extends PrettyStage {
    public static AssetList assetList = new AssetList();
    static {
        assetList.collectAssetDescriptor(TextBox.class, assetList);
        assetList.addTexture(MENU_BG_TEXTURE);
    }

    public InfoStage(MyGame game) {
        super(game);
    }

    private OneSpriteStaticActor bg;
    private TextBox back;
    private Logo infoLogo;

    private ArrayList<TextBox> textBoxes;

    private boolean setBack;
    //endregion
    //region Absztrakt metódusok
    @Override
    public void assignment() {
        textBoxes = new ArrayList<>();
        textBoxes.add(new TextBox(game,"Hatodik infódoboz"));
        textBoxes.add(new TextBox(game,"Ötödik infódoboz"));
        textBoxes.add(new TextBox(game,"Negyedik infódoboz"));
        textBoxes.add(new TextBox(game,"Harmadik infódoboz"));
        textBoxes.add(new TextBox(game,"Második infódoboz"));
        textBoxes.add(new TextBox(game,"Elsö infódoboz"));

        //SoundManager.assign();
        //if(!muted)
        //    SoundManager.menuMusic.play();
        bg = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
        back = new TextBox(game, "Vissza a menübe");
        infoLogo = new Logo(game, Logo.LogoType.INFO);
    }

    @Override
    public void setSizes() {
        if(getViewport().getWorldWidth() > bg.getWidth()) bg.setWidth(getViewport().getWorldWidth());
        infoLogo.setSize(infoLogo.getWidth()*0.9f,infoLogo.getHeight()*0.9f);
    }

    @Override
    public void setPositions() {
        if(getViewport().getWorldWidth() < bg.getWidth()) bg.setX((getViewport().getWorldWidth()-bg.getWidth())/2);
        back.setPosition(getViewport().getWorldWidth() - (back.getWidth() + 45),50);
        infoLogo.setPosition(getViewport().getWorldWidth()/2 - infoLogo.getWidth()/2, getViewport().getWorldHeight() - infoLogo.getHeight()*1.1f);
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
        addActor(back);

        for (int i = textBoxes.size()-1; i >= 0; i--) {
            //Minden párosat a jobboldolra, páratlant a baloldalira
            if(i%2 == 0) textBoxes.get(i).setX(getViewport().getWorldWidth()+125);
            else textBoxes.get(i).setX(-textBoxes.get(i).getWidth()-125);

            //A legelsőnek adunk fix pozíciót, a többit pedig az előtte lévőhöz igazítjuk az Y tengelyen
            if(i == textBoxes.size()-1){
                textBoxes.get(textBoxes.size()-1).setY(650);
                addActor(textBoxes.get(textBoxes.size()-1));
            }else {
                textBoxes.get(i).setY(textBoxes.get(i + 1).getY() - 12 - textBoxes.get(i).getHeight());
                addActor(textBoxes.get(i));
            }
        }

        addActor(infoLogo);
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

        for (int i = textBoxes.size()-1; i >= 0; i--){
            if (i % 2 == 0) {
                if (textBoxes.get(i).getX() > getViewport().getWorldWidth() / 2 - textBoxes.get(i).getWidth() / 2)
                    textBoxes.get(i).setX(textBoxes.get(i).getX() - 15);
            }
            else {
                if (textBoxes.get(i).getX() < getViewport().getWorldWidth() / 2 - textBoxes.get(i).getWidth() / 2)
                    textBoxes.get(i).setX(textBoxes.get(i).getX() + 15);
            }
        }

        if(bgAlpha>0.25 && !setBack){
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
        for (TextBox tb : textBoxes) {
            tb.setAlpha(alpha);
        }
    }
    //endregion
}
