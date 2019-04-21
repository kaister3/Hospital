package main.java.layout;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class Patient extends RecursiveTreeObject<Patient> {
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty id;
    public StringProperty name;
    public StringProperty time;
    public StringProperty type;

    public Patient(String id, String namePatient, Timestamp dateTime, boolean isSpecialist){
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(namePatient);
        this.time = new SimpleStringProperty(dateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.type = new SimpleStringProperty(isSpecialist ? "专家号" : "普通号");
    }
}