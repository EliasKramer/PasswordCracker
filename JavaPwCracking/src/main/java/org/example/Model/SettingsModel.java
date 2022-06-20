package org.example.Model;

public class SettingsModel {
    private static SettingsModel _instance;

    private String[] _hashMethods;
    private String _currHashMethod;

    private boolean _knowsLength;
    private boolean _knowsChars;
    private int _maxLengthOfPw;

    private SettingsModel() {
        _hashMethods = new String[]{"SHA-256", "SHA-512", "SHA-384", "SHA-224", "SHA-1", "MD5"};
        _currHashMethod = _hashMethods[0];
        _knowsChars = false;
        _knowsLength = false;
        _maxLengthOfPw = 8;
    }
    public static SettingsModel getInstance() {
        if (_instance == null) {
            _instance = new SettingsModel();
        }
        return _instance;
    }

    public String[] getHashMethods() {
        return _hashMethods;
    }
    public void setCurrHashMethod(String currHashMethod) {
        _currHashMethod = currHashMethod;
    }
    public void setKnowsLength(boolean knowsLength) {
        _knowsLength = knowsLength;
    }
    public void setKnowsChars(boolean knowsChars) {
        _knowsChars = knowsChars;
    }
    public void setMaxLengthOfPw(int maxLengthOfPw) {
        _maxLengthOfPw = maxLengthOfPw;
    }
    public String getCurrHashMethod() {
        return _currHashMethod;
    }
    public boolean getKnowsLength() {
        return _knowsLength;
    }
    public int getMaxLengthOfPw() {
        return _maxLengthOfPw;
    }
    public boolean getKnowsChars() {
        return _knowsChars;
    }
}