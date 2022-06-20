package org.example.BruteForce;

import org.example.Controller.MainAppController;
import org.example.Model.SettingsModel;

import javax.swing.*;

public class BruteForceManager {
    private MainAppController _callBackClass;
    private int _maxLengthOfStringToTry;
    private char[] _possibleChars;
    private int _id;

    public BruteForceManager(MainAppController givenController, int id, char[] givenPossibleChars, int givenLength) {
        _callBackClass = givenController;
        _possibleChars = givenPossibleChars;
        _maxLengthOfStringToTry = givenLength;
        _id = id;
    }

    public void startBruteForcing() {
        if (SettingsModel.getInstance().getKnowsLength()) {
            initThreadWithLength(_maxLengthOfStringToTry);
        } else {
            //try empty string
            guessString("");
            for (int currLength = 1; currLength <= _maxLengthOfStringToTry; currLength++) {
                initThreadWithLength(currLength);
            }
        }
    }

    private void initThreadWithLength(int length) {
        final int lengthOfGuessingString = length;

        Thread t = new Thread(() -> {
            int diff = _possibleChars.length - 1;
            int[] AddingToArray = new int[lengthOfGuessingString];
            boolean shallContinue = true;
            //initial guess
            buildStringAndGuess(AddingToArray);
            long numberOfGuesses = 1;
            long maxNumberOfGuesses = (long) Math.pow(_possibleChars.length, lengthOfGuessingString);

            long lastPrintedPercent = 0;
            do {
                AddingToArray[0]++;
                int indexTillAdding = 0;
                while (AddingToArray[indexTillAdding] > diff) {
                    AddingToArray[indexTillAdding] = 0;
                    indexTillAdding++;
                    if (indexTillAdding == lengthOfGuessingString) {
                        _callBackClass.addLog("Brute Force: Password is longer than " +
                                lengthOfGuessingString);
                        return;
                    } else {
                        AddingToArray[indexTillAdding]++;
                    }
                }
                shallContinue = buildStringAndGuess(AddingToArray);
                numberOfGuesses++;
                long percent = (numberOfGuesses*100 / maxNumberOfGuesses);
                if (lengthOfGuessingString >= 5 &&
                        (numberOfGuesses % 10000000 == 0) &&
                        percent != lastPrintedPercent)
                {
                    _callBackClass.addLog("Brute Force: " + length + " to " + percent + "% done");
                    lastPrintedPercent = percent;
                }
            } while (shallContinue);
        });
        t.start();
    }

    private boolean buildStringAndGuess(int[] indexes) {
        char[] guessArr = new char[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            guessArr[i] = _possibleChars[indexes[i]];
        }
        String guess = new String(guessArr);
        return guessString(guess);
    }

    private boolean guessString(String s) {
        return _callBackClass.guessPlain(s, _id, "Brute Force");
    }
}