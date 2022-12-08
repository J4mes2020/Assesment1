package dev.joeyfoxo.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordGame2 {

    List<String> enteredWords = new ArrayList<>();
    List<String> dictionaryWords = new ArrayList<>();
    boolean outcome = false;
    boolean continueORterminate;
    final String defaultError = "Entered word is invalid or does not exist in the dictionary, please try again";

    public WordGame2() {

        gameStart();

    }

    private void gameStart() {

        addWordsToDictionary("src/main/resources/dictionary.txt");

        enteredWords.add(dictionaryWords.get(ThreadLocalRandom.current().nextInt(0, dictionaryWords.size())));
        System.out.println("""
                ======================================================
                Welcome to the word game
                                
                Here you will enter a word until you either repeat yourself or\s
                break the scheme of ending with the same character as you start
                ======================================================
                                
                Word Game started with: 
                """ + enteredWords.get(0));



        promptForWord();

    }

    private void gameRestart() {
        enteredWords.clear();
        gameStart();
    }

    private boolean doesWordStartsWithLastChar(String previousWord, String word) {
        return word.startsWith(String.valueOf(previousWord.charAt(previousWord.length() - 1)));
    }

    private boolean hasWordAlreadyBeenEntered(String word) {
        return enteredWords.contains(word);
    }

    private boolean isWordValid(String word) {
        return word.matches("[A-Za-z]+");
    }

    private boolean isWordInDictionary(String word) {
        return dictionaryWords.contains(word);
    }

    private boolean didTheUserEnterAnything(String word) {

        return word.trim().length() == 0 || word.isEmpty();

    }


    private void checkWords(Scanner scanner) {

        boolean continueChecking = true;
        String currentWord = null;

        do {

            //TODO: Fix the previous work check


            currentWord = scanner.nextLine();

            if (didTheUserEnterAnything(currentWord)) {
                gameRestart();
                return;
            }

            if (enteredWords.contains(currentWord)) {
                System.out.println("GAME OVER - That word has already been entered");
                return;
            }

            if (isWordValid(currentWord) && isWordInDictionary(currentWord) && doesWordStartsWithLastChar(previousWord, currentWord)) {
                continueChecking = false;
            } else {
                System.out.println(defaultError);

            }

            System.out.println(previousWord);
            System.out.println(currentWord);

        }
        while (continueChecking);

        System.out.println("Amazing Please enter another word: ");
        enteredWords.add(currentWord);
    }

    private void promptForWord() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nPlease input your word: ");

        do {

            checkWords(scanner);

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

        } catch (FileNotFoundException ignored) {

        }

    }
}
