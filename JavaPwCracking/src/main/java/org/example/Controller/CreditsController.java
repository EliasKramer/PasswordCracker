package org.example.Controller;

import javafx.event.ActionEvent;

import java.net.URI;
import java.net.URL;
import java.awt.Desktop;

public class CreditsController {
    private static final String _ownerGit = "https://github.com/EliasKramer";
    private static final String _rockYouPage = "https://www.kaggle.com/datasets/wjburns/common-password-list-rockyoutxt";
    private static final String _englishDictionaryPage = "https://github.com/dwyl/english-words";

    public void openGithubOfOwner() {
        openWebpage(_ownerGit);
    }
    public void openRockYouPage() {
        openWebpage(_rockYouPage);
    }
    public void openEnglishDictionaryPage() {
        openWebpage(_englishDictionaryPage);
    }

    public void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
