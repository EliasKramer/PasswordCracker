package org.example.BruteForce;

import java.util.LinkedList;

public class PasswordAnalyzer {
    private String _plainText;
    public PasswordAnalyzer(String plainText) {
        _plainText = plainText;
    }
    public char[] getPossibleCharsInText()
    {
        LinkedList<Character> possibleChars = new LinkedList<>();
        boolean smallAlphabetExists = false;
        boolean bigAlphabetExists = false;
        boolean numbersExists = false;
        boolean specialCharsExists = false;

        for (Character curr : _plainText.toCharArray()) {
            if(CharArea.isBetween('a','z',curr) && !smallAlphabetExists) {
                possibleChars.addAll(CharArea.charsBetween('a','z'));
                smallAlphabetExists = true;
            }
            else if(CharArea.isBetween('A','Z',curr) && !bigAlphabetExists)
            {
                possibleChars.addAll(CharArea.charsBetween('A','Z'));
                bigAlphabetExists = true;
            }
            else if(CharArea.isBetween('0','9',curr) && !numbersExists)
            {
                possibleChars.addAll(CharArea.charsBetween('0','9'));
                numbersExists = true;
            }
            else if (CharArea.isSpecialChar(curr) && !specialCharsExists)
            {
                possibleChars.addAll(CharArea.getAllSpecialChars());
                specialCharsExists = true;
            }
            else{
                break;
            }
        }

        char[] retVal = new char[possibleChars.size()];
        for(int i = 0; i < possibleChars.size(); i++)
        {
            retVal[i] = possibleChars.get(i);
        }
        return retVal;
    }
}
