package dev.joeyfoxo.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordGame {

    List<String> enteredWords = new ArrayList<>();
    List<String> dictionaryWords = new ArrayList<>();
    boolean outcome = false;
    boolean continueORterminate;
    final String defaultError = "Entered word is invalid or does not exist in the dictionary, please try again";

    public WordGame() {
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

        checkWords();
    }

    private void gameRestart() {
        enteredWords.clear();
        gameStart();
    }

    private void checkWords() {

        boolean continueChecking = true;
        String currentWord;
        Scanner scanner = new Scanner(System.in);
        String previousWord = enteredWords.get(0);
        System.out.println("\nPlease input your word: ");
        do {

            do {
                currentWord = scanner.nextLine();
                if (didTheUserEnterAnything(currentWord)) {
                    gameRestart();
                    return;
                }
                if (hasWordAlreadyBeenEntered(currentWord)) {
                    System.out.println("GAME OVER - That word has already been entered");
                    System.exit(0);
                    return;
                }
                if (isWordValid(currentWord) && isWordInDictionary(currentWord) && doesWordStartsWithLastChar(previousWord, currentWord)) {
                    previousWord = currentWord;
                    System.out.println("Amazing Please enter another word starting with the letter: " + getLastCharOfWord(previousWord));
                    enteredWords.add(currentWord);
                    continueChecking = false;
                } else {
                    System.out.println(defaultError);
                }

            }
            while (continueChecking);
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
    private char getLastCharOfWord(String word) {
        return word.charAt(word.length() - 1);
    }
}
