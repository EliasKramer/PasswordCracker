package org.example.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.*;
import org.example.BruteForce.BruteForceManager;
import org.example.BruteForce.CharArea;
import org.example.BruteForce.PasswordAnalyzer;
import org.example.Model.SettingsModel;
import org.example.TextFileChecker.TextFileChecker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static java.lang.Thread.sleep;

public class MainAppController {
    public Button StartButtonFx;
    public TextField OutputFx;
    public PasswordField InputFx;
    public TextField HashedInputFx;
    public Label MethodFx;
    public TextField HashedOutputFx;
    public Label TimeTookFx;
    public ListView Logs;

    private String _hashedInput;
    private long _startTime;
    private boolean _foundRightAnswer;
    private String _input;
    private TextFileChecker _commonPasswords;
    private TextFileChecker _dictionary;

    private SimpleDateFormat _formatter = new SimpleDateFormat("HH:mm:ss");
    private int _maxLogs = 3;

    private int _guessID = 0;
    public MainAppController() {
        _commonPasswords = new TextFileChecker("./passwordlist/rockyou.txt", "Common Password List",this);
        _dictionary = new TextFileChecker("./passwordlist/englishWords.txt", "English Words List",this);
    }
    @FXML
    public void OnStartClick() {
        _guessID++;
        addLog("Started Cracking");
        OutputFx.setText("");
        HashedOutputFx.setText("");

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

        _commonPasswords.checkAllEntries(this,_guessID);
        _dictionary.checkAllEntries(this,_guessID);

        bruteForce(_guessID);
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

    private void bruteForce(int id) {
        SettingsModel model = SettingsModel.getInstance();
        PasswordAnalyzer analyzer = new PasswordAnalyzer(_input);
        char[] possibleChars = model.getKnowsChars() ?
                analyzer.getPossibleCharsInText() :
                CharArea.getAllChars();
        int lengthOfPw = model.getKnowsLength() ?
                _input.length() :
                model.getMaxLengthOfPw();
        BruteForceManager m = new BruteForceManager(this,
                id,
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
        byte[] hashedByteArray = digest.digest(byteOfTextToHash);

        return Base64.getEncoder().encodeToString(hashedByteArray);
    }

    ///returns true if it should continue guessing
    public boolean guessPlain(String guess, int id, String method) {
        if (_foundRightAnswer) {
            return false;
        }
        if (id != _guessID) {
            return false;
            //the id is not the current one another password is currently beeing guessed
        }
        if (_hashedInput.equals(getHash(guess))) {
            processCorrectGuess(guess, _hashedInput, method);
            return false;
        }
        return true;
    }

    private void processCorrectGuess(String planeText, String hashedText, String method) {
        addLog("Correct Guess: " + planeText);
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

    public void addLog(String log) {
        Platform.runLater(() -> {
            if (Logs.getItems().size()+1 > _maxLogs) {
                Logs.getItems().remove(0);
            }
            Date date = new Date(System.currentTimeMillis());
            String prefix = _formatter.format(date);

            Logs.getItems().add(prefix + " " + log);
        });
    }
}