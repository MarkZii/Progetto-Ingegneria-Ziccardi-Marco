module progetto.progettokenkne {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens progetto.progettokenken to javafx.fxml;
    exports progetto.progettokenken;
}