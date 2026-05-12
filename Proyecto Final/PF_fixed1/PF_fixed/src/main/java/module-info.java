module co.edu.uniquindio.poo.PF {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens co.edu.uniquindio.poo.PF to javafx.fxml;
    opens co.edu.uniquindio.poo.PF.view to javafx.fxml;
    opens co.edu.uniquindio.poo.PF.controller to javafx.fxml;

    exports co.edu.uniquindio.poo.PF;
    exports co.edu.uniquindio.poo.PF.view;
    exports co.edu.uniquindio.poo.PF.controller;
    exports co.edu.uniquindio.poo.PF.model.domain;
    exports co.edu.uniquindio.poo.PF.model.enums;
}
