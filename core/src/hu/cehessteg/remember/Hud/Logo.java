package hu.cehessteg.remember.Hud;

import com.badlogic.gdx.Gdx;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;

public class Logo extends OneSpriteStaticActor {
    //region AssetList
    public static final String LOGO_TEXTURE = "pic/logos/logo.png";
    public static final String MARANCSHOP_TEXTURE = "pic/logos/marancshop.png";
    public static final String OPTIONS_TEXTURE = "pic/logos/options.png";
    public static final String INFO_TEXTURE = "pic/logos/info.png";

    public static AssetList assetList = new AssetList();
    static {
        assetList.addTexture(LOGO_TEXTURE);
        assetList.addTexture(MARANCSHOP_TEXTURE);
        assetList.addTexture(OPTIONS_TEXTURE);
        assetList.addTexture(INFO_TEXTURE);
    }
    //endregion
    //region Logo típus Enum
    public enum LogoType{
        MENU, SHOP, OPTIONS, INFO
    }
    //endregion
    //region Konstruktor
    public Logo(MyGame game, LogoType logotype) {
        super(game, LOGO_TEXTURE);
        setTexture(logotype);
    }
    //endregion
    //region Textúre beállító metódus
    private void setTexture(LogoType logotype){
        switch (logotype){
            case MENU:{
                sprite.setTexture(game.getMyAssetManager().getTexture(LOGO_TEXTURE));
                break;
            }
            case SHOP:{
                sprite.setTexture(game.getMyAssetManager().getTexture(MARANCSHOP_TEXTURE));
                setSize(game.getMyAssetManager().getTexture(MARANCSHOP_TEXTURE).getWidth(),game.getMyAssetManager().getTexture(MARANCSHOP_TEXTURE).getHeight());
                break;
            }
            case OPTIONS:{
                sprite.setTexture(game.getMyAssetManager().getTexture(OPTIONS_TEXTURE));
                setSize(game.getMyAssetManager().getTexture(OPTIONS_TEXTURE).getWidth()*0.7f,game.getMyAssetManager().getTexture(OPTIONS_TEXTURE).getHeight()*0.7f);
                break;
            }
            case INFO:{
                sprite.setTexture(game.getMyAssetManager().getTexture(INFO_TEXTURE));
                setSize(game.getMyAssetManager().getTexture(INFO_TEXTURE).getWidth()*0.7f,game.getMyAssetManager().getTexture(INFO_TEXTURE).getHeight()*0.7f);
                break;
            }
            default:{
                Gdx.app.log("Logo", "Paraméterként megadott logotípus érvénytelen, alapértelmezettként a MenuLogo kerül beállításra!");
            }
        }
    }
    //endregion
    //region Act metódusai
    private int speed = 2;

    @Override
    public void act(float delta) {
        super.act(delta);
        setRotation(getRotation() + delta * speed);

        if (getRotation() >= 12 || getRotation() <= -12) {
            speed *= -1;
        }
    }
    //endregion
}
