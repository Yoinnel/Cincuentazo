module com.univalle.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.univalle.cincuentazo to javafx.fxml;
    exports com.univalle.cincuentazo;
}