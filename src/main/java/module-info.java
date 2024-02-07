module com.rt.ispwproject {
    requires java.sql;
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rt.ispwproject to javafx.fxml;
    opens com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers to javafx.fxml;
    opens com.rt.ispwproject.beans to com.google.gson;
    opens com.rt.ispwproject.model to com.google.gson;

    exports com.rt.ispwproject;
    exports com.rt.ispwproject.beans;
    exports com.rt.ispwproject.graphiccontrollers.jfxgraphiccontrollers;
}