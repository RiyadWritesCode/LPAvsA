module com.example.lpavsa {
    requires javafx.controls;
    requires javafx.fxml;


    opens pathfinding to javafx.fxml;
    exports pathfinding;
}