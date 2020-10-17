package hu.cehessteg.remember.Hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import hu.cehessteg.remember.Stage.OptionsStage;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.IPrettyStage;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.MyGroup;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.OneSpriteStaticActor;
import hu.csanyzeg.master.MyBaseClasses.UI.MyLabel;

import static hu.cehessteg.remember.Hud.TextBox.VERDANA_FONT;
import static hu.cehessteg.remember.MemoryGame.preferences;

public class OptionSwitch extends MyGroup implements IPrettyStage {
    public static String DECREMENT_TEXTURE = "pic/gombok/play_kek.png";
    public static String TEXTBACKGROUND_TEXTURE = "pic/ui/szoveg.png";
    public static String INCREMENT_TEXTURE = "pic/gombok/play.png";

    private OneSpriteStaticActor decrement;
    private OneSpriteStaticActor background;
    private OneSpriteStaticActor increment;
    private MyLabel text;

    private int indexCounter;
    private int indexMax;
    private int indexMin;

    private OptionSwitchType type;

    public OptionSwitch(MyGame game, OptionSwitchType type) {
        super(game);
        this.type = type;
        assignment();
        setSizes();
        setPositions();
        addListeners();
        setZIndexes();
        addActors();
    }

    @Override
    public void assignment() {
        switch (type){
            case DIFFICULTY:
                indexMax = 4;
                indexMin = 1;
                indexCounter = OptionsStage.difficulty;
                break;
            case SIZE:
                indexMin = 0;
                indexMax = getMatrixSizes().size()-1;
                indexCounter = getMatrixSizes().indexOf(OptionsStage.size);
                break;
            case GAMEMODE:
                indexMin = 1;
                indexMax = 2;
                indexCounter = OptionsStage.gamemode;
                break;
        }

        decrement = new OneSpriteStaticActor(game,DECREMENT_TEXTURE);
        increment = new OneSpriteStaticActor(game,INCREMENT_TEXTURE);
        background = new OneSpriteStaticActor(game,TEXTBACKGROUND_TEXTURE);

        text = new MyLabel(game, "", new Label.LabelStyle(game.getMyAssetManager().getFont(VERDANA_FONT), Color.BLACK)) {
            @Override
            public void init() {
                setAlignment(0);
            }
        };

        indexCounterChanged();

        decrement.setRotation(180);
    }

    @Override
    public void setSizes() {
        decrement.setSize(120,120);
        increment.setSize(120,120);
        background.setSize((getMaxRowWidth()+1)*21,64);
        text.setFontScale(1);
        text.setAlignment(Align.center);
    }

    @Override
    public void setPositions() {
        decrement.setPosition(0,0);
        background.setPosition(decrement.getX()+decrement.getWidth()+24,decrement.getY()+decrement.getHeight()/2-background.getHeight()/2);
        text.setAlignment(Align.center);
        text.setPosition(background.getX()+background.getWidth()/2-text.getWidth()/2,background.getY()+background.getHeight()/2-text.getHeight()/2);
        increment.setPosition(background.getX()+background.getWidth()+24,0);
    }

    @Override
    public void addListeners() {
        decrement.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(decrement.isVisible()) {
                    if (indexCounter > indexMin) {
                        indexCounter--;
                        if(indexCounter == indexMin) decrement.setVisible(false);
                        if(!increment.isVisible()) increment.setVisible(true);
                    }
                    else indexCounter = indexMin;
                    indexCounterChanged();
                }
            }
        });

        increment.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(increment.isVisible()) {
                    if (indexCounter < indexMax) {
                        indexCounter++;
                        if(indexCounter == indexMax) increment.setVisible(false);
                        if(!decrement.isVisible()) decrement.setVisible(true);
                    }
                    else indexCounter = indexMax;
                    indexCounterChanged();
                }
            }
        });
    }

    @Override
    public void setZIndexes() {

    }

    @Override
    public void addActors() {
        addActor(decrement);
        addActor(increment);
        addActor(background);
        addActor(text);
    }

    private void indexCounterChanged(){
        switch (type){
            case DIFFICULTY:
                switch (indexCounter){
                    case 1:{
                        decrement.setVisible(false);
                        text.setText("Nehézség: Könnyű");
                        break;
                    }
                    case 2:{
                        text.setText("Nehézség: Normál");
                        break;
                    }
                    case 3:{
                        text.setText("Nehézség: Nehéz");
                        break;
                    }
                    case 4:{
                        increment.setVisible(false);
                        text.setText("Nehézség: Lehetetlen");
                        break;
                    }
                    default:{
                        text.setText("Nehézség: Normál");
                        preferences.putInteger("difficulty",2);
                        preferences.flush();
                        break;
                    }
                }
                OptionsStage.difficulty = indexCounter;
                break;
            case SIZE:
                if(getMatrixSizes().get(indexCounter) != 0) {
                    text.setText("Méret: " + getMatrixSizes().get(indexCounter) / 10 + "x" + getMatrixSizes().get(indexCounter) % 10);
                    if(getMatrixSizes().get(indexCounter)==86) increment.setVisible(false);
                }
                else {
                    text.setText("Méret: Folyamatos");
                    decrement.setVisible(false);
                }
                OptionsStage.size = getMatrixSizes().get(indexCounter);
                break;
            case GAMEMODE:
                switch (indexCounter){
                    case 1:{
                        decrement.setVisible(false);
                        text.setText("Játékmód: Arcade");
                        break;
                    }
                    default:{
                        increment.setVisible(false);
                        text.setText("Játékmód: Zen");
                        break;
                    }
                }
                OptionsStage.gamemode = indexCounter;
                break;
        }
        setSizes();
        setPositions();
        if(getStage()!=null&&getStage() instanceof OptionsStage){
            ((OptionsStage)getStage()).setPositions();
        }
    }

    private ArrayList<Integer> getMatrixSizes(){
        ArrayList<Integer> sizes = new ArrayList();
        sizes.add(0);
        for (int i = 3; i <= 8; i++){
            for (int j = 3; j <= 6; j++){
                sizes.add(i*10+j);
            }
        }
        return sizes;
    }

    private int getMaxRowWidth(){
        int temp = 0;
        int max = 0;
        for (int i = 0; i < this.text.getText().length(); i++){
            if(text.getText().charAt(i) != '\n') temp++;
            else temp = 0;
            if(temp > max) max = temp;
        }
        return max;
    }

    public void setAlpha(float alpha) {
        decrement.setColor(1,1,1,alpha);
        increment.setColor(1,1,1,alpha);
        background.setColor(1,1,1,alpha);
        text.setColor(1,1,1,alpha);
    }

    @Override
    public float getWidth() {
        return decrement.getWidth()+background.getWidth()+increment.getWidth()+48;
    }
}
