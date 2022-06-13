package org.example.BruteForce;

import org.example.Controller.MainAppController;
import org.example.Model.SettingsModel;

public class BruteForceManager {
    private MainAppController _callBackClass;
    private int _maxLengthOfStringToTry;
    private char[] _possibleChars;

    public BruteForceManager(MainAppController givenController, char[] givenPossibleChars, int givenLength) {
        _callBackClass = givenController;
        _possibleChars = givenPossibleChars;
        _maxLengthOfStringToTry = givenLength;
    }
    public void startBruteForcing() {
        if(SettingsModel.getInstance().getKnowsLength())
        {
            initThreadWithLength(_maxLengthOfStringToTry);
        }
        else {
            //try empty string
            guessString("");
            for (int currLength = 1; currLength <= _maxLengthOfStringToTry; currLength++) {
                initThreadWithLength(currLength);
            }
        }
    }
    private void initThreadWithLength(int length)
    {
        final int lengthOfGuessingString = length;

        Thread t = new Thread(() -> {
            int diff = _possibleChars.length-1;
            int[] AddingToArray = new int[lengthOfGuessingString];
            boolean shallContinue = true;

            //initial guess
            buildStringAndGuess(AddingToArray);

            do {
                AddingToArray[0]++;
                int indexTillAdding = 0;
                while (AddingToArray[indexTillAdding] > diff) {
                    AddingToArray[indexTillAdding] = 0;
                    indexTillAdding++;
                    if (indexTillAdding == lengthOfGuessingString) {
                        System.out.println("The String is longer than " + lengthOfGuessingString + " characters");
                        return;
                    } else {
                        AddingToArray[indexTillAdding]++;
                    }
                }
                shallContinue = buildStringAndGuess(AddingToArray);

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
        return _callBackClass.guessPlain(s, "Brute Force");
    }
}