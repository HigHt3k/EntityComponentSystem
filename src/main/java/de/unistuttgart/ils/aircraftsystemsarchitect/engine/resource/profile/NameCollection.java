package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.profile;

import java.util.Random;

/**
 * A collection of names and adjectives used to generate random names for profiles.
 */
public class NameCollection {
    private static final Random rand = new Random();

    /**
     * Array of possible names for profiles.
     */
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

    /**
     * Array of possible adjectives for profiles.
     */
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

    /**
     * Generates a random name by combining an adjective and a name from the respective arrays.
     *
     * @return A randomly generated name as a String.
     */
    public static String buildName() {
        return adjectives[rand.nextInt(adjectives.length)] + " " + names[rand.nextInt(names.length)];
    }
}
