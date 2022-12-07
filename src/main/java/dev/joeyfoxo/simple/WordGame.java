package dev.joeyfoxo.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordGame extends Thread {

    List<String> enteredWords = new ArrayList<>();
    List<String> dictionaryWords = new ArrayList<>();
    boolean outcome;
    boolean continueORterminate;
    String defaultError = "Entered word is invalid or does not exist in the dictionary, please try again: ";

    public WordGame() {

        gameStart();

    }

    private void gameStart() {
        addWordsToDictionary("src/main/resources/dictionary.txt");

        System.out.println("""
                ======================================================
                Welcome to the word game
                                
                Here you will enter a word until you either repeat yourself or\s
                break the scheme of ending with the same character as you start
                ======================================================
                                
                Please enter a word to begin for example: \n""" +
                dictionaryWords.get(ThreadLocalRandom.current().nextInt(0, dictionaryWords.size())) + " \n(No this one won't count)");

        while (!outcome) {

            promptForWord();
        }

    }

    private boolean isWordValid(String word) {

        return word.matches("[A-Za-z]+");


    }

    private void gameRestart() {


    }

    private void addWordsToDictionary(String path) {

        try {

            InputStream inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                dictionaryWords.add(scanner.nextLine());
            }

        } catch (FileNotFoundException ignored) {

        }

    }

    private void promptForWord() {

        Scanner scanner = new Scanner(System.in);
        boolean redo;
        System.out.println("Please enter a word: ");
        String word;
        do {
            word = scanner.nextLine();
            if (word.isEmpty() || word.trim().length() == 0) {
                gameRestart();
                return;
            }
            if (!isWordValid(word) || !dictionaryWords.contains(word)) {
                System.out.println(defaultError);
                redo = true;
            }
            else {
                redo = false;
            }
        }
        while (redo);

        if (enteredWords.contains(word)) {

            System.out.println("Game Over");
            outcome = true;
            return;

        }

        enteredWords.add(word);

        System.out.println("Perfect " + word + " was a valid word, your next word must start with the letter "
        + word.charAt(word.length() - 1));

    }

}
