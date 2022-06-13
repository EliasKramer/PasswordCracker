module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.Controller;
    opens org.example.Controller to javafx.fxml;
    exports org.example.Model;
    opens org.example.Model to javafx.fxml;
    exports org.example.BruteForce;
    opens org.example.BruteForce to javafx.fxml;
    exports org.example.TextFileChecker;
    opens org.example.TextFileChecker to javafx.fxml;
}