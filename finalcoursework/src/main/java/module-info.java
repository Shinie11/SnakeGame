module com.example.finalcoursework {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;


    opens com.example.finalcoursework to javafx.fxml;
    exports com.example.finalcoursework;
}