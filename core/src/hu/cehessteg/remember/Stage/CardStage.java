package hu.cehessteg.remember.Stage;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import hu.cehessteg.remember.Actor.Card;
import hu.csanyzeg.master.MyBaseClasses.Game.MyGame;
import hu.csanyzeg.master.MyBaseClasses.Scene2D.ResponseViewport;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.PositionRule;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldHelper;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldStage;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimer;
import hu.csanyzeg.master.MyBaseClasses.Timers.TickTimerListener;
import hu.csanyzeg.master.MyBaseClasses.Timers.Timer;

public class CardStage extends SimpleWorldStage {
    public static long score;

    public static Vector2 matrix = new Vector2((int)(Math.random()*5)+3,(int)(Math.random()*3)+3);
    public ArrayList<Card> kartyak;
    static ArrayList<Card> selectedCards;

    public static boolean isAct;
    public static boolean isGameOver;

    public CardStage(MyGame game) {
        super(new ResponseViewport(9), game);
        kartyak = new ArrayList<Card>();
        selectedCards = new ArrayList<Card>();

        for (byte y = 0; y < matrix.y; y++) {
            for (byte x = 0; x < matrix.x; x++) {
                kartyak.add(new Card(game, new Vector2(x,y), world));
            }
        }

        for (Card c : kartyak) {
            addActor(c);
        }
        System.out.println(matrix);

        addTimer(new TickTimer(3,true,new TickTimerListener(){
            @Override
            public void onTick(Timer sender, float correction) {
                super.onTick(sender, correction);
                shuffleCards();
            }
        }));
    }

    private void shuffleCards(){
        ArrayList<Integer> randomIDs = new ArrayList<>();
        ArrayList<Card> shuffledCards = new ArrayList<>();
        while (randomIDs.size() < kartyak.size()){
            int newSzam = (int) (Math.random()*kartyak.size());
            if(!randomIDs.contains(newSzam))
                randomIDs.add(newSzam);
        }

        for (int i = 0; i < kartyak.size(); i++){
            shuffledCards.add(kartyak.get(randomIDs.get(i)));
        }

        for (byte y = 0; y < matrix.y; y++) {
            for (byte x = 0; x < matrix.x; x++) {
                int id = y*x+x;
                shuffledCards.get(id).koordinatak = new Vector2(x,y);
            }
        }

        for (int i = 0; i < shuffledCards.size(); i++){
            ((SimpleWorldHelper)kartyak.get(i).getActorWorldHelper()).getBody().moveToFixSpeed(shuffledCards.get(i).getX(),shuffledCards.get(i).getY(),5,PositionRule.LeftBottom);
        }
    }

    public static void addCard(Card card) {
        selectedCards.add(card);
        checkCards();
    }

    public static void removeCard(Card card) {
        selectedCards.remove(card);
        checkCards();
    }

    private static void checkCards(){
        if(selectedCards.size() == 2){
            if(selectedCards.get(0).type == selectedCards.get(1).type){
                ((SimpleWorldHelper)selectedCards.get(0).getActorWorldHelper()).getBody().moveToFixSpeed(selectedCards.get(0).getX(),20,5, PositionRule.Center);
                ((SimpleWorldHelper)selectedCards.get(1).getActorWorldHelper()).getBody().moveToFixSpeed(selectedCards.get(1).getX(),20,5, PositionRule.Center);
            }else{
                selectedCards.get(0).deSelect();
                selectedCards.get(1).deSelect();
            }
            selectedCards.clear();
        }
    }
}
