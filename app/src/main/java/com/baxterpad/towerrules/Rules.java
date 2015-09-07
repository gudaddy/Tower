package com.baxterpad.towerrules;

import android.util.Log;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Sean on 3/5/2015.
 */
public class Rules {

    private static String[] rules;
    private static LinkedList<String> rulesHistory;
    private static int rule_index = 0;
    private static final int RULE_HISTORY_SIZE = 9;

    public static String[] setDeveloperRules() {
        rules = new String[20];
        rules[0] = "Rule 0";
        rules[1] = "Rule 1";
        rules[2] = "Rule 2";
        rules[3] = "Rule 3";
        rules[4] = "Rule 4";
        rules[5] = "Rule 5";
        rules[6] = "Rule 6";
        rules[7] = "Rule 7";
        rules[8] = "Rule 8";
        rules[9] = "Rule 9";
        rules[10] = "Rule 10";
        rules[11] = "Rule 11";
        rules[12] = "Rule 12";
        rules[13] = "Rule 13";
        rules[14] = "Rule 14";
        rules[15] = "Rule 15";
        rules[16] = "Rule 16";
        rules[17] = "Rule 17";
        rules[18] = "Rule 18";
        rules[19] = "Rule 19";
        //rules[53] = "abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef"; // 139
        //rules[53] = "abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef abcdef";
        return rules;
    }

    public static String[] setDefaultRules() {
        rules = new String[54];
        rules[0] = "Drink one for every letter in your last name.";
        rules[1] = "Pick someone to take a shot.";
        rules[2] = "Social drink!";
        rules[3] = "Whenever you speak you must talk into a pretend microphone.";
        rules[4] = "Drink, then pull another piece.";
        rules[5] = "Group chooses shot for you to take (ex. Pickle juice).";
        rules[6] = "You take a shot. Jenga got you good!";
        rules[7] = "Give 1!";
        rules[8] = "Give 2!";
        rules[9] = "Give 3!";
        rules[10] = "Give 4!";
        rules[11] = "Take 1!";
        rules[12] = "Take 2!";
        rules[13] = "Take 3!";
        rules[14] = "Take 4!";
        rules[15] = "Make a Rule.";
        rules[16] = "Everyone drinks until the person next to them stops.";
        rules[17] = "Drink 1 drink for each person playing the game.";
        rules[18] = "The next person must take their turn sitting on your lap.";
        rules[19] = "Every time you give a drink, you take a drink.";
        rules[20] = "The tallest person at the table must take 4 drinks then give 4 drinks.";
        rules[21] = "The shortest person at the table must take 4 drinks then give 4 drinks.";
        rules[22] = "Everyone must remove the first piece they touch.";
        rules[23] = "You must keep physical contact with the person to your right for the remainder of the game.";
        rules[24] = "Choose someone to drink with you for the rest of the game. They drink when you do, and vice versa.";
        rules[25] = "Everyone whose gender is opposite yours must take 1 drink.";
        rules[26] = "Everyone whose gender is the same as yours must take 1 drink.";
        rules[27] = "Take 2 then pull again.";
        rules[28] = "You are the Beer Wench. You must fetch new drinks for all players the rest of the game.";
        rules[29] = "You must insult the person to your right, then compliment the person to your left.";
        rules[30] = "Any time you sing the Jeopardy theme song, the person taking their turn must complete their turn before you finish the song.";
        rules[31] = "You must refer to yourself in the third person for the rest of the game.";
        rules[32] = "Pick a color. Everyone must drink 1 for every article of clothing they have on that contains that color.";
        rules[33] = "Tell a story that led to a scar on your body, or drink 4.";
        rules[34] = "Perform I'm A Little Teapot and give 4 drinks, or drink 4.";
        rules[35] = "Choose someone to make an animal sound every time another person drinks for the rest of the game.";
        rules[36] = "Give 1 for every cat you own. Drink 3 if you have no cats.";
        rules[37] = "The oldest player drinks 4, then gives 4.";
        rules[38] = "Pick someone. You must actively disagree with everything they say for the rest of the game.";
        rules[39] = "Everyone who has never grown a full beard nor French-kissed a man with one must drink 3.";
        rules[40] = "From now on, anyone who talks or makes noise during your turn must drink 2.";
        rules[41] = "Give 2 to the player whom you think has the best behind.";
        rules[42] = "Give 2 to every player you've seen naked.";
        rules[43] = "The two players next to you each drink 4.";
        rules[44] = "If you're single, give 5 each to the smuggest pair playing. If you're taken, you and your honey drink 5 each.";
        rules[45] = "Switch hands you pull with.";
        rules[46] = "Drink 3 and skip your next turn.";
        rules[47] = "You can not speak for the rest of the game.";
        rules[48] = "You must sing whenever you talk for the rest game.";
        rules[49] = "Give 1. That player may give you 1 back. You may then give them 2; they may give 2 back. Continue until one of you opts not to give drinks.";
        rules[50] = "You must have drink in your other hand whenever you pull a block.";
        rules[51] = "Anyone who picks up their cell phone must drink 1 for the remainder of the game.";
        rules[52] = "Do your best impression of someone at the table.";
        rules[53] = "You must talk like a robot for the rest of the game.";
        return rules;
    }

    private static String[] shuffle() {
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

    public static void initializeRules() {
        Log.d("Rules", "initializeRules(): Initializing rules to default rules list");
        rules = setDefaultRules();
        rulesHistory = new LinkedList<String>();
//        printCurrentRules();
    }

    public static void initializeDevRules() {
        Log.d("TowerRules_Rules", "initializeDevRules(): Initializing rules to developer rules list");
        rules = setDeveloperRules();
        rulesHistory = new LinkedList<String>();
    }

    public static void initializeKidsRules() {
        Log.d("TowerRules_Rules", "initializeKidsRules(): Initializing rules to kids rules list");
        rules = setDefaultRules();
        rulesHistory = new LinkedList<String>();
    }

    public static void initializeCustomRules() {
        Log.d("TowerRules_Rules", "initializeCustomRules(): Initializing rules to custom rules list");
        rules = setDefaultRules();
        rulesHistory = new LinkedList<String>();
    }

    public static void shuffleRules() {
        Log.d("TowerRules_Rules", "shuffleRules(): Shuffling rules");
        rules = shuffle();
        rule_index = 0;
    }

    public static String nextRule() {
        if (rule_index >= rules.length) {
            shuffleRules();
        }

        printLastTen();

        String nextRule = rules[rule_index];

        // This checks if the selected rule was already selected
        // recently and if so, swaps it with another rule.
        // This algorithm assumes that the list of rules > (2 * RULE_HISTORY_SIZE)
        // and that there are no duplicates throughout the list.
        if (rulesHistory.contains(nextRule)) {
            Log.d("TowerRules_Rules", "Duplicate rule found, swapping");
            Log.d("TowerRules_Rules", "Current index " + rule_index);
            int temp_index = rule_index;
            String temp_rule = "";
            do {
                temp_index++;
                temp_rule = rules[temp_index];
            } while (temp_index < rules.length && rulesHistory.contains(temp_rule));
            Log.d("TowerRules_Rules", "New index " + temp_index);
            if (temp_rule.length() > 0) {
                temp_rule = rules[rule_index];
                rules[rule_index] = rules[temp_index];
                rules[temp_index] = temp_rule;
                nextRule = rules[rule_index];
                Log.d("TowerRules_Rules", "Swapped " + temp_rule + " with " + nextRule);
            } else {
                Log.e("TowerRules_Rules", "Unable to perform swap, reusing rule " + nextRule);
            }

        }

        rulesHistory.addLast(nextRule);
        while (rulesHistory.size() > RULE_HISTORY_SIZE) {
            rulesHistory.removeFirst();
        }

        rule_index++;
        return nextRule;
    }

    public static String[] getRuleList () {
        return rules;
    }

    public static void printCurrentRules() {
        Log.d("TowerRules_Rules", "Current Rules:");
        for (int i = 0; i < rules.length; i++) {
            if (rules[i] != null && rules[i].length() > 0) {
                Log.d("TowerRules_Rules", i + ": " + rules[i]);
            }
        }
    }

    public static void printLastTen() {
        Log.d("TowerRules_Rules", "Last 10 Rules:");
        for (int i = 0; i < rulesHistory.size(); i++) {
            Log.d("TowerRules_Rules", i + ": " + rulesHistory.get(i));
        }
    }
}
