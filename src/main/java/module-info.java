module com.rt.ispwproject {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rt.ispwproject to javafx.fxml;
    opens com.rt.ispwproject.graphicControllers.jfxGraphicControllers to javafx.fxml;
    exports com.rt.ispwproject;
    exports com.rt.ispwproject.graphicControllers.jfxGraphicControllers;
}