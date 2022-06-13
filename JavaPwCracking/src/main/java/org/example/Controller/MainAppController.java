package org.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.*;
import org.example.BruteForce.BruteForceManager;
import org.example.BruteForce.CharArea;
import org.example.BruteForce.PasswordAnalyzer;
import org.example.Model.SettingsModel;
import org.example.TextFileChecker.TextFileChecker;

import java.io.IOException;
import java.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainAppController {
    public Button StartButtonFx;
    public TextField OutputFx;
    public PasswordField InputFx;
    public TextField HashedInputFx;
    public Label MethodFx;
    public TextField HashedOutputFx;
    public Label TimeTookFx;

    private String _hashedInput;
    private long _startTime;
    private boolean _foundRightAnswer;
    private String _input;
    private TextFileChecker _commonPasswords;
    private TextFileChecker _dictionary;

    public MainAppController() {
        _commonPasswords = new TextFileChecker("./passwordlist/rockyou.txt", "Common Password List");
        _dictionary = new TextFileChecker("./passwordlist/englishWords.txt", "English Words List");
    }

    @FXML
    public void OnStartClick() {
        _input = InputFx.getText();

        int maxLength = SettingsModel.getInstance().getMaxLengthOfPw();
        if (InputFx.getText().length() > maxLength) {
            _input = InputFx.getText().substring(0, maxLength);
            InputFx.setText(_input);
        }

        _foundRightAnswer = false;

        _startTime = System.nanoTime();
        _hashedInput = getHash(_input);
        HashedInputFx.setText(_hashedInput);

        _commonPasswords.checkAllEntries(this);
        _dictionary.checkAllEntries(this);

        bruteForce();
    }

    @FXML
    public void OnSettingsClick() {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(App.loadFXML("Settings")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Settings");
        stage.show();
    }

    @FXML
    void OnCreditsClick() {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(App.loadFXML("Credits")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Credits");
        stage.show();
    }

    private void bruteForce() {
        SettingsModel model = SettingsModel.getInstance();
        PasswordAnalyzer analyzer = new PasswordAnalyzer(_input);
        char[] possibleChars = model.getKnowsChars() ?
                analyzer.getPossibleCharsInText() :
                CharArea.getAllChars();
        int lengthOfPw = model.getKnowsLength() ?
                _input.length() :
                model.getMaxLengthOfPw();
        BruteForceManager m = new BruteForceManager(this,
                possibleChars,
                lengthOfPw
        );
        m.startBruteForcing();
    }

    private String getHash(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(SettingsModel.getInstance().getCurrHashMethod());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] byteOfTextToHash = input.getBytes(StandardCharsets.UTF_8);
        byte[] hashedByetArray = digest.digest(byteOfTextToHash);

        return Base64.getEncoder().encodeToString(hashedByetArray);
    }

    ///returns true if it should continue guessing
    public boolean guessPlain(String guess, String method) {
        if (_foundRightAnswer) {
            return false;
        }
        if (_hashedInput.equals(getHash(guess))) {
            processCorrectGuess(guess, _hashedInput, method);
            return false;
        }
        return true;
    }

    private void processCorrectGuess(String planeText, String hashedText, String method) {
        System.out.println("Correct guess: " + planeText);
        Platform.runLater(() -> {
            if (!_foundRightAnswer) {
                _foundRightAnswer = true;
                OutputFx.setText(planeText);
                MethodFx.setText("Method used: " + method);
                TimeTookFx.setText("Time taken: " + (System.nanoTime() - _startTime) / 1000000 + " ms");
                HashedOutputFx.setText(hashedText);
            }
        });
    }
}
