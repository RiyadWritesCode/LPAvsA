module com.example.lpavsa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;


    opens pathfinding to javafx.fxml;
    exports pathfinding;
}