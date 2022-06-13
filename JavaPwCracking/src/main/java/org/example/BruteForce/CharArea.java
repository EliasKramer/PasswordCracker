package org.example.BruteForce;

import java.util.LinkedList;

public class CharArea {
    public static LinkedList<Character> charsBetween(char start, char end) {
        LinkedList<Character> retVal = new LinkedList<>();
        for (char i = start; i <= end; i++) {
            retVal.add(i);
        }
        return retVal;
    }
    public static boolean isBetween(char start, char end, char c) {
        return c >= start && c <= end;
    }
    public static boolean isSpecialChar(char c) {
        return isBetween('!', '/', c) ||
                        isBetween(':', '@', c) ||
                        isBetween('[', '`', c) ||
                        isBetween('{', '~', c);
    }
    public static LinkedList<Character> getAllSpecialChars() {
        LinkedList<Character> retVal = new LinkedList<>();
        retVal.addAll(charsBetween('!', '/'));
        retVal.addAll(charsBetween(':', '@'));
        retVal.addAll(charsBetween('[', '`'));
        retVal.addAll(charsBetween('{', '~'));
        return retVal;
    }
    public static char[] getAllChars() {
        LinkedList<Character> chars = charsBetween('!', '~');
        char[] retVal = new char[chars.size()];
        for(int i = 0; i < chars.size(); i++)
        {
            retVal[i] = chars.get(i);
        }
        return retVal;
    }
}
