package engine.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class NameCollection {
    private static Random rand = new Random();

    public static String[] names = new String[] {
            "Alligator",
            "Dog",
            "Cat",
            "Roboter",
            "Lion",
            "Liger",
            "Axolotl",
            "Machine",
            "Bird",
            "Eagle",
            "Raptor",
            "Piranha",
            "Butterfly",
            "Mouse",
            "Snake"
    };

    public static String[] adjectives = new String[] {
            "Fancy",
            "Mighty",
            "Beautiful",
            "Dizzy",
            "Happy",
            "Perfect",
            "Dull",
            "Mechanic",
            "Young",
            "Majestic",
            "Elegant",
            "Chaotic"
    };

    public static String buildName() {
        return adjectives[rand.nextInt(adjectives.length)] + " " + names[rand.nextInt(names.length)];
    }
}
