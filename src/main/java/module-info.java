module com.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires json.smart;


    opens com.example.demo4 to javafx.fxml;
    exports com.example.demo4;
}