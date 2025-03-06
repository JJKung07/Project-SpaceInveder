module se233.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires slf4j.api;


    opens se233.project2 to javafx.fxml;
    exports se233.project2.view;
    opens se233.project2.view to javafx.fxml;
    exports se233.project2.model;
    opens se233.project2.model to javafx.fxml;
    exports se233.project2.controller;
    opens se233.project2.controller to javafx.fxml;
}