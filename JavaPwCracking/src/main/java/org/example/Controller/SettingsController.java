package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.example.Model.SettingsModel;

public class SettingsController {
    @FXML
    private Label CurrPasswordLengthFx;
    @FXML
    private Slider MaxLengthOfPw;
    @FXML
    private CheckBox KnowsLengthFx;
    @FXML
    private CheckBox KnowsCharsFx;
    @FXML
    private ChoiceBox<String> HashMethodsFx;

    private SettingsModel _model;

    public void initialize() {
        _model = SettingsModel.getInstance();

        ObservableList<String> hashMethods = FXCollections.observableArrayList(_model.getHashMethods());
        HashMethodsFx.setItems(hashMethods);
        HashMethodsFx.setValue(_model.getCurrHashMethod());

        KnowsCharsFx.setSelected(_model.getKnowsChars());
        KnowsLengthFx.setSelected(_model.getKnowsLength());
        MaxLengthOfPw.setValue(_model.getMaxLengthOfPw());
        CurrPasswordLengthFx.setText(String.valueOf(_model.getMaxLengthOfPw()));

        HashMethodsFx.setOnAction(event -> {
            _model.setCurrHashMethod(HashMethodsFx.getValue());
        });
    }
    @FXML
    public void updateKnowsLength() {
        System.out.println("updateKnowsLength");
        _model.setKnowsLength(KnowsLengthFx.isSelected());
    }
    @FXML
    public void updateKnowsChars() {
        System.out.println("updateKnowsChars");
        _model.setKnowsChars(KnowsCharsFx.isSelected());
    }
    @FXML
    public void updateMaxLengthOfPw() {
        System.out.println("updateMaxLengthOfPw "+MaxLengthOfPw.getValue());
        _model.setMaxLengthOfPw((int) MaxLengthOfPw.getValue());
        CurrPasswordLengthFx.setText(String.valueOf(_model.getMaxLengthOfPw()));
    }
}