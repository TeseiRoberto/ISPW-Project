module com.rt.ispwproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rt.ispwproject to javafx.fxml;
    exports com.rt.ispwproject;
}