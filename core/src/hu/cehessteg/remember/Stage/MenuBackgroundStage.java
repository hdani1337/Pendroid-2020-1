package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.PrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;

import static hu.cehessteg.remember.Stage.MenuStage.MENU_BG_TEXTURE;

public class MenuBackgroundStage extends PrettyStage {
    public MenuBackgroundStage(MyGame game) {
        super(new ResponseViewport(900), game);
    }

    OneSpriteStaticActor bg;

    @Override
    public void assignment() {
        bg = new OneSpriteStaticActor(game,MENU_BG_TEXTURE);
    }

    @Override
    public void setSizes() {
        bg.setSize(getViewport().getWorldWidth(),getViewport().getWorldHeight());
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
        addActor(bg);
    }
}
