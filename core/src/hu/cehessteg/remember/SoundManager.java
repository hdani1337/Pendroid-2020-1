package hu.cehessteg.remember;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import hu.csanyzeg.master.MyBaseClasses.Assets.AssetList;

/**
 * Ezt az osztályt azért hoztam létre, hogy könnyebben hozzáférhessek a hanganyagokhoz, illetve egységesen tölthessem be őket
 * Minden hang és zene 1-1 statikus példány, így nem kell őket stagenként létrehozni
 * **/
public class SoundManager {
    /**Game egy példánya, hogy később könnyen hozzáférhessek**/
    public static MemoryGame game;

    /**
     * Hangok elérési útjai
     * **/
    public static final String KICK_SOUND = "sound/kick.wav";
    public static final String URAIM_SOUND = "sound/uraim.wav";
    public static final String HEE_SOUND = "sound/héé.wav";
    public static final String CRASH_SOUND = "sound/crash.wav";
    public static final String COIN_SOUND = "sound/coin.mp3";
    public static final String ERROR_SOUND = "sound/error.mp3";
    public static final String GLASS_SOUND = "sound/glass.wav";
    public static final String PAY_SOUND = "sound/pay.mp3";
    public static final String POWERUP_SOUND = "sound/powerup.mp3";

    /**
     * Zenék elérési útjai
     * **/
    public static final String MENUMUSIC = "music/menuMusic.mp3";
    public static final String BOSSMUSIC = "music/bossMusic.mp3";
    public static final String GAMEMUSIC = "music/gameMusic.mp3";

    /**
     * Hangok
     * **/
    public static Sound kickSound;
    public static Sound kezdesHang;
    public static Sound kilepesHang;
    public static Sound coinSound;
    public static Sound crashSound;
    public static Sound errorSound;
    public static Sound glassSound;
    public static Sound paySound;
    public static Sound powerUpSound;

    /**
     * Zenék
     * **/
    public static Music menuMusic;
    public static Music bossMusic;
    public static Music gameMusic;

    /**
     * Objektumok létrehozása
     * **/
    public static void assign(){
        if(game != null) {
            /**
             * Hang objektumok létrehozása
             * **/
            kickSound = game.getMyAssetManager().getSound(KICK_SOUND);
            kezdesHang = game.getMyAssetManager().getSound(URAIM_SOUND);
            kilepesHang = game.getMyAssetManager().getSound(HEE_SOUND);
            coinSound = game.getMyAssetManager().getSound(COIN_SOUND);
            crashSound = game.getMyAssetManager().getSound(CRASH_SOUND);
            errorSound = game.getMyAssetManager().getSound(ERROR_SOUND);
            glassSound = game.getMyAssetManager().getSound(GLASS_SOUND);
            paySound = game.getMyAssetManager().getSound(PAY_SOUND);
            powerUpSound = game.getMyAssetManager().getSound(POWERUP_SOUND);

            /**
             * Zene objektumok létrehozása
             * **/
            menuMusic = game.getMyAssetManager().getMusic(MENUMUSIC);
            bossMusic = game.getMyAssetManager().getMusic(BOSSMUSIC);
            gameMusic = game.getMyAssetManager().getMusic(GAMEMUSIC);
        }
    }

    /**
     * Betöltés AssetList átadásával
     * **/
    public static void load(AssetList assetList) {
        if(assetList != null) {
            /**
             * Hangok betöltése
             * **/
            assetList.addSound(URAIM_SOUND);
            assetList.addSound(KICK_SOUND);
            assetList.addSound(HEE_SOUND);
            assetList.addSound(COIN_SOUND);
            assetList.addSound(CRASH_SOUND);
            assetList.addSound(ERROR_SOUND);
            assetList.addSound(GLASS_SOUND);
            assetList.addSound(PAY_SOUND);
            assetList.addSound(POWERUP_SOUND);

            /**
             * Zenék betöltése
             * **/
            assetList.addMusic(MENUMUSIC);
            assetList.addMusic(BOSSMUSIC);
            assetList.addMusic(GAMEMUSIC);
            Gdx.app.log("SoundManager","!!! WARNING !!! CALL assign() WHEN THE LOADING HAS BEEN FINISHED");
        }
    }

    /**
     * Objektumok kiürítése
     * Ennek meghívása után lehetőleg ne hivatkozzunk rájuk mert NullPointert dobnak :D
     * **/
    public static void dispose(){
        /**
         * Hang objektumok nullásáza
         * **/
        kickSound = null;
        kezdesHang = null;
        kilepesHang = null;
        coinSound = null;
        crashSound = null;
        errorSound = null;
        glassSound = null;
        paySound = null;
        powerUpSound = null;

        /**
         * Zene objektumok nullázása
         * **/
        menuMusic = null;
        bossMusic = null;
        gameMusic = null;
    }
}
