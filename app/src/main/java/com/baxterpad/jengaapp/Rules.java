package com.baxterpad.jengaapp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.baxterpad.jengaapp.Network;


import android.content.Context;



/**
 * Created by Sean on 3/5/2015.
 */
public class Rules {


    static String[] rules = {};
    static String FILENAME = "rules";

   public static String[] getRulesFromSite() throws Exception {
        ArrayList<String> ruleNames = new  ArrayList<String>();
        ArrayList<String> ruleDescriptions = new  ArrayList<String>();

        rules = combineRules(ruleNames, ruleDescriptions);
        return rules;
    }

    public static String[] getDefaultRules() throws Exception {
        rules[0] = "1:Drink one for every letter in your last name";
        rules[1] = "2\tpick someone to take a shot";
        rules[2] = "3\tsocial drink";
        rules[3] = "4\twhenever you speak you must talk into a pretend microphone";
        rules[4] = "5\t\"drink, then pull another piece\"";
        rules[5] = "6\tgroup chooses shot for you to take from anywhere (ex. Pickle juice)";
        rules[6] = "7\tyou take a shot";
        rules[7] = "8\tGive 1";
        rules[8] = "9\tGive 2";
        rules[9] = "10\tGive 3";
        rules[10] = "11\tGive 4";
        rules[11] = "12\tTake 1";
        rules[12] = "13\tTake 2";
        rules[13] = "14\tTake 3";
        rules[14] = "15\tTake 4";
        rules[15] = "16\tMake a Rule";
        rules[16] = "17\tEveryone drinks until the person next to them stops.";
        rules[17] = "18\tDrink 1 drink for each person playing the game.";
        rules[18] = "19\tThe next person must take their turn sitting on your lap.";
        rules[19] = "20\t\"Every time you give a drink, you take a drink.\"";
        rules[20] = "21\tThe tallest person at the table must take 4 drinks then give 4 drinks.";
        rules[21] = "22\tThe shortest person at the table must take 4 drinks then give 4 drinks.";
        rules[22] = "23\tEveryone must remove the first piece they touch";
        rules[23] = "24\tYou must keep physical contact with the person to your right for the remainder of the game.";
        rules[24] = "25\t\"Choose someone to drink with you for the rest of the game. [They drink when you do, and vice versa.";
        rules[25] = "26\tEveryone whose gender is opposite yours must take 1 drink.";
        rules[26] = "27\tEveryone whose gender is the same as yours must take 1 drink.";
        rules[27] = "28\tTake 2 then pull again.";
        rules[28] = "29\tYou are the Beer Wench. [You must fetch new drinks for all players the rest of the game.";
        rules[29] = "30\t\"You must insult the person to your right, then compliment the person to your left.";
        rules[30] = "31\tAny time you sing the Jeopardy theme song, the person taking their turn must complete their turn before you finish the song.";
        rules[31] = "32\tYou must refer to yourself in the third person for the rest of the game";
        rules[32] = "33\tPick a color.  Everyone must drink 1 for every article of clothing they have on that contains that color.";
        rules[33] = "34\tTell a story that led to a scar on your body - or drink 3!";
        rules[34] = "35\tPerform I'm A Little Teapot and give 2 drinks, or drink 5.";
        rules[35] = "36\tChoose someone to make an animal sound every time another person drinks for the rest of the game.";
        rules[36] = "37\tGive 1 for every cat you own.  Drink 3 if you have no cats.";
        rules[37] = "38\tThe oldest player drinks 4, then gives 4.";
        rules[38] = "39\tPick someone.  You must actively disagree with everything they say for the rest of the game.";
        rules[39] = "40\tEveryone who has never grown a full beard nor French-kissed a man with one must drink 3.";
        rules[40] = "41\t\"From now on, anyone who talks or makes noise during your turn must drink 2\"";
        rules[41] = "42\tGive 2 to the player whom you think has the best behind.";
        rules[42] = "43\tGive 2 to every player you've seen naked.";
        rules[43] = "44\tThe two players next to you each drink 4.";
        rules[44] = "45\tIf you're single, give 5 each to the smuggest pair playing. If you're taken, you and your honey drink 5 each.";
        rules[45] = "46\tSwitch hands you pull with";
        rules[46] = "47\tDrink 3 and skip your next turn.";
        rules[47] = "48\tYou can not speak for the rest of the game";
        rules[48] = "49\tYou must sing whenever you talk for the rest game";
        rules[49] = "50\tGive 1. That player may give you 1 back. You may then give them 2; they may give 2 back. Continue until one of you opts not to give drinks";
        rules[50] = "51\tThink of a block idea. If majority approves, this becomes that block. If not, you drink 5 whenever this is pulled again.";
        rules[51] = "52\tIf you're the first person to pull this, drink 2. Every time this is pulled after that, you drink 6.";
        rules[52] = "53\tDo your best impression of someone at the table.";
        rules[53] = "54\tTake 6 drinks. Jenga got you good!";
        return rules;
    }


    public static String[] shuffleRules() {
        String[] temp_rules = new String[rules.length];
        for (int i = 0; i < rules.length; i++) {
            temp_rules[i] = rules[i];
        }

        int newI;
        String temp_rule;
        Random randIndex = new Random();

        for (int i = 0; i < rules.length; i++) {

            // pick a random index between 0 and cardsInDeck - 1
            newI = randIndex.nextInt(rules.length);

            // swap cards[i] and cards[newI]
            temp_rule = temp_rules[i];
            temp_rules[i] = temp_rules[newI];
            temp_rules[newI] = temp_rule;
        }

        return temp_rules;
    }

    private static String[] combineRules(ArrayList<String> ruleNames, ArrayList<String> ruleDescriptions) throws Exception {
        String[] rules = {};
        if(ruleNames.size() != ruleDescriptions.size()){
            throw new Exception("Rules file malformed");
        }
        for(int i = 0; i < ruleNames.size(); i++){
            rules[i] = ruleNames.get(i) + ":" + ruleDescriptions.get(i);
        }
        return rules;
    }

    public static void getRulesFromPhone() throws Exception {

        FileInputStream fis = openFileInput(FILENAME, Context.MODE_PRIVATE);
        for(String rule : rules){
            fis.read(rule.getBytes());
        }
        fis.close();
    }

    public void setRulesToPhone() throws Exception {


        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        for(String rule : rules){
            fos.write(rule.getBytes());
        }
        fos.close();
    }

    public String[] initializeRules() throws Exception{

        String[] listOfFiles = fileList();

        if(Network.isNetworkAvailable()){
            getRulesFromSite();
        }else {
            if (Arrays.asList(listOfFiles).contains(FILENAME)) {
                getRulesFromPhone();
            } else {
                getDefaultRules();
            }
        }
        return rules;
    }
}
