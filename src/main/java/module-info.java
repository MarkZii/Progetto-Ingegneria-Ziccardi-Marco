module progetto.progettokenkne {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens progetto.progettokenkne to javafx.fxml;
    exports progetto.progettokenkne;
}