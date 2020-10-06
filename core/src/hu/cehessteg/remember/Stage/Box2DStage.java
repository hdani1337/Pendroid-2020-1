package hu.cehessteg.remember.Stage;

import hu.csanyzeg.master.MyBaseClasses.Box2dWorld.Box2dStage;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;

public class Box2DStage extends Box2dStage {
    public Box2DStage(MyGame game) {
        super(new ResponseViewport(9), game);
    }
}
