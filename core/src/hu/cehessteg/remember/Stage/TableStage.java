package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.utils.viewport.Viewport;

import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;

public class TableStage extends MyStage {
    /**
     * Itt lesz majd a háttér
     * **/
    public TableStage(MyGame game) {
        super(new ResponseViewport(900), game);
    }
}
