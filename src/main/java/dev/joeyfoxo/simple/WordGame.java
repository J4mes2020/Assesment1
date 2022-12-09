package dev.joeyfoxo.simple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordGame extends Thread{

    List<String> words = new ArrayList<>();
    List<String> dictionaryWords = new ArrayList<>();
    final String defaultError = "Entered word is invalid or does not exist in the dictionary, please try again";
    boolean outcome = false;


    @Override
    public void run() {
        gameStart();
    }

    private void gameStart() {

        addWordsToDictionary("src/main/resources/dictionary.txt");
        words.add(dictionaryWords.get(ThreadLocalRandom.current().nextInt(0, dictionaryWords.size())));
        System.out.println("""
                ======================================================
                Welcome to the word game
                                
                Here you will enter a word until you either repeat yourself or\s
                break the scheme of ending with the same character as you start
                ======================================================
                                
                Word Game started with:
                """ + words.get(0));

        wordValid();
    }

    private void gameRestart() {
        words.clear();
        System.out.println("Game Was restarted due to no input");
        gameStart();
    }

    private void wordValid() {

        boolean continueORterminate = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease input your word: ");
        do {

            do {
                String currentWord = scanner.nextLine();
                String previousWord = words.get(0);
                if (didTheUserEnterAnything(currentWord)) {
                    gameRestart();
                    return;
                }
                if (hasWordAlreadyBeenEntered(currentWord)) {
                    System.out.println("GAME OVER - That word has already been entered");
                    System.out.println("FINAL SCORE: " + words.size());
                    outcome = true;
                    return;
                }
                if (isWordValid(previousWord, currentWord)) {
                    previousWord = currentWord;
                    words.add(currentWord);
                    System.out.println("Currently entered words: " + words);
                    System.out.println("Please enter another word starting with the letter " + getLastCharOfWord(previousWord) + ": ");
                    continueORterminate = false;
                } else {
                    System.out.println(defaultError);
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
        return words.contains(word);
    }
    private boolean isWordValid(String previousWord, String word) {
        return word.matches("[A-Za-z]+") && dictionaryWords.contains(word) && word.startsWith(String.valueOf(previousWord.charAt(previousWord.length() - 1)));
    }
    private boolean didTheUserEnterAnything(String word) {
        return word.trim().length() == 0 || word.isEmpty();
    }
    private char getLastCharOfWord(String word) {
        return word.charAt(word.length() - 1);
    }
}
