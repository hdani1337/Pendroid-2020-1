package hu.cehessteg.remember;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import hu.cehessteg.remember.Actor.Card;
import hu.cehessteg.remember.Actor.CardType;
import hu.cehessteg.remember.Stage.CardStage;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.PositionRule;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorld;
import hu.csanyzeg.master.MyBaseClasses.SimpleWorld.SimpleWorldHelper;

public class CardMethods {
    private CardStage cardStage;

    public CardMethods(CardStage senderStage){
        cardStage = senderStage;
    }

    public void generateCards(SimpleWorld world){
        cardStage.score = 0;
        cardStage.scoreTimer = 0;
        cardStage.lastFoundTime = 0;
        cardStage.isAct = true;
        cardStage.isGameOver = false;
        cardStage.isShuffling = false;
        cardStage.kartyak = new ArrayList<Card>();
        cardStage.selectedCards = new ArrayList<Card>();

        ArrayList<CardType> types = new ArrayList<>();
        for (int i = 0; i < (cardStage.matrix.x*cardStage.matrix.y)/2; i++) {
            types.add(CardType.values()[i]);
            types.add(CardType.values()[i]);
        }

        for (byte y = 0; y < cardStage.matrix.y; y++) {
            for (byte x = 0; x < cardStage.matrix.x; x++) {
                int random = (int) (Math.random()*types.size());
                cardStage.kartyak.add(new Card(cardStage.game, new Vector2(x,y), world, types.get(random),this));
                types.remove(random);
            }
        }

        for (Card c : cardStage.kartyak) {
            cardStage.addActor(c);
        }
    }

    //Összes kártyát átforgatja
    public void shuffleCards(){
        cardStage.isShuffling = true;
        ArrayList<Integer> randomIDs = new ArrayList<>();
        ArrayList<Card> shuffledCards = new ArrayList<>();
        while (randomIDs.size() < cardStage.kartyak.size()){
            int newSzam = (int) (Math.random()*cardStage.kartyak.size());
            if(!randomIDs.contains(newSzam))
                randomIDs.add(newSzam);
        }

        for (int i = 0; i < cardStage.kartyak.size(); i++){
            shuffledCards.add(cardStage.kartyak.get(randomIDs.get(i)));
        }

        for (byte y = 0; y < cardStage.matrix.y; y++) {
            for (byte x = 0; x < cardStage.matrix.x; x++) {
                try {
                    int id = y * x + x;
                    shuffledCards.get(id).koordinatak = new Vector2(x, y);
                    cardStage.kartyak.get(id).koordinatak = new Vector2(x, y);
                }catch (IndexOutOfBoundsException e){
                    System.out.println("("+cardStage.matrix.x+","+cardStage.matrix.y+") helyen nincs kártya!");
                }
            }
        }

        for (int i = 0; i < shuffledCards.size(); i++){
            ((SimpleWorldHelper)cardStage.kartyak.get(i).getActorWorldHelper()).getBody().moveToFixSpeed(shuffledCards.get(i).getX(),shuffledCards.get(i).getY(),5, PositionRule.LeftBottom);
        }
        cardStage.isShuffling = false;
    }

    public void shuffleTwoCards(){
        cardStage.isShuffling = true;
        ArrayList<Integer> randomIDs = new ArrayList<>();
        ArrayList<Vector2> newKoordinatak = new ArrayList<>();
        while (randomIDs.size() < 2){
            int newSzam = (int) (Math.random()*cardStage.kartyak.size());
            if(!randomIDs.contains(newSzam))
                randomIDs.add(newSzam);
        }

        for (int i : randomIDs)
            newKoordinatak.add(cardStage.kartyak.get(i).koordinatak);

        cardStage.kartyak.get(randomIDs.get(0)).setKoordinatak(newKoordinatak.get(1));
        cardStage.kartyak.get(randomIDs.get(1)).setKoordinatak(newKoordinatak.get(0));
        cardStage.isShuffling = false;
    }

    public void addCard(Card card) {
        cardStage.selectedCards.add(card);
        checkCards();
    }

    public void removeCard(Card card) {
        cardStage.selectedCards.remove(card);
        checkCards();
    }

    public void checkCards(){
        if(cardStage.selectedCards.size() == 2){
            if(cardStage.selectedCards.get(0).type == cardStage.selectedCards.get(1).type){
                ((SimpleWorldHelper)cardStage.selectedCards.get(0).getActorWorldHelper()).getBody().colorToFixTime(2,0,0,0,0);
                ((SimpleWorldHelper)cardStage.selectedCards.get(1).getActorWorldHelper()).getBody().colorToFixTime(2,0,0,0,0);
                cardStage.score+= 25*(cardStage.lastFoundTime/cardStage.scoreTimer);
                cardStage.kartyak.remove(cardStage.selectedCards.get(0));
                cardStage.kartyak.remove(cardStage.selectedCards.get(1));
                cardStage.selectedCards.clear();
                cardStage.lastFoundTime = cardStage.scoreTimer;
                if(cardStage.kartyak.size()<2){
                    //ELFOGYTAK A KÁRTYÁK
                    nextLevel();
                }
            }else{
                cardStage.selectedCards.get(0).deSelect();
                cardStage.selectedCards.get(1).deSelect();
                cardStage.score-= 25*(cardStage.lastFoundTime/cardStage.scoreTimer);
                cardStage.lastFoundTime = cardStage.scoreTimer;
            }
            cardStage.selectedCards.clear();
        }
    }

    public void nextLevel(){
        System.out.println("Következő szint");
        cardStage.isGameOver = true;
        cardStage.isAct = false;
    }
}
