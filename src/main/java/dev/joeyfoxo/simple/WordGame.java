package dev.joeyfoxo.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordGame extends Thread {

    List<String> words = new ArrayList<>(); //The arraylist the user will input their words to
    List<String> dictionaryWords = new ArrayList<>(); //The arraylist the words from the dictionary file will be put in
    final String defaultError = "Entered word is invalid or does not exist in the dictionary, please try again";
    //The default error on some cases

    boolean outcome = false; //The boolean which returns true if the game should end
    @Override
    public void run() { //The default run method for the thread
        gameStart(); //Running the method to start the game
    }
    private void gameStart() {

        addWordsToDictionary("src/main/resources/dictionary.txt"); //Adds a file text to the dictionary
        words.add(dictionaryWords.get(ThreadLocalRandom.current().nextInt(0, dictionaryWords.size())));
        //Choose a random word from the dictionary from the first index to the size of the dictionary

        System.out.println("======================================================");
        System.out.println("Welcome to the word game");
        System.out.println("\nHere you will enter a word until you either repeat yourself or");
        System.out.println("break the scheme of ending with the same character as you start");
        System.out.println("======================================================");
        System.out.println("Word Game started with: " + words.get(0));

        wordValid(); //Calling the method to run all the user checks and word checks
    }

    private void gameRestart() {
        words.clear(); //Clears the user entered words
        System.out.println("Game Was restarted due to no input");
        gameStart(); //Recalls the game start method to restart the game
    }

    private void wordValid() {
        boolean continueORterminate = true;
        Scanner scanner = new Scanner(System.in); //Opening the stream for a user input
        String previousWord = words.get(0);
        System.out.println("\nPlease input your word: ");
        do {

            do {
                String currentWord = scanner.nextLine(); //Stores the user input as a String
                if (didTheUserEnterAnything(currentWord)) { // If the user didn't enter anything run this code
                    gameRestart(); //Calls the restart method which handles all restarting
                    return;
                }
                if (hasWordAlreadyBeenEntered(currentWord)) { // If the words array contains the current word run this code
                    System.out.println("GAME OVER - That word has already been entered");
                    System.out.println("FINAL SCORE: " + words.size()); //Returns a score which is the amount of words in the array
                    outcome = true; //Exit the loop and end the game
                    return; //Terminate so no further code is ran
                }
                if (isWordValid(previousWord, currentWord)) { // If the word only contains alphanumeric characters run this code
                    previousWord = currentWord;
                    words.add(currentWord); //Adds the word they entered to the word entered array
                    System.out.println("Currently entered words: " + words);
                    System.out.println("Please enter another word starting with the letter " + getLastCharOfWord(previousWord) + ": ");
                    continueORterminate = false; //Exit the checking loop
                } else {
                    System.out.println(defaultError); //else if none of these have been passed print out the default error
                }

            }
            while (continueORterminate);
        }
        while (!outcome);
    }

    private void addWordsToDictionary(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                dictionaryWords.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException ignored) {}
    }

    private boolean hasWordAlreadyBeenEntered(String word) {
        //Return true if the array contains the entered word
        return words.contains(word);
    }
    private boolean isWordValid(String previousWord, String word) {

        /*
        Return true if the word has only alphanumeric characters
        and the dictionary contains the word and the current word
        starts with the last character of the previous word
        */
        return word.matches("[A-Za-z]+") && dictionaryWords.contains(word) && word.startsWith(String.valueOf(previousWord.charAt(previousWord.length() - 1)));
    }
    private boolean didTheUserEnterAnything(String word) {
        //Returns true if the string is empty
        return word.trim().length() == 0 || word.isEmpty();
    }
    private char getLastCharOfWord(String word) {
        //A simple method to return the last character of any String
        return word.charAt(word.length() - 1);
    }
}
