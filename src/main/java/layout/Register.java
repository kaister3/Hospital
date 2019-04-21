package main.java.layout;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Register extends RecursiveTreeObject<Register> {
    public StringProperty section;
    public StringProperty docName;
    public StringProperty type;
    public StringProperty regName;

    public double getMoney() {
        return money.get();
    }

    public DoubleProperty moneyProperty() {
        return money;
    }

    public void setMoney(double money) {
        this.money.set(money);
    }

    public DoubleProperty money;

    public String getSection() {
        return section.get();
    }

    public StringProperty sectionProperty() {
        return section;
    }

    public void setSection(String section) {
        this.section.set(section);
    }

    public String getDocName() {
        return docName.get();
    }

    public StringProperty docNameProperty() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName.set(docName);
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

    public String getRegName() {
        return regName.get();
    }

    public StringProperty regNameProperty() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName.set(regName);
    }

    public Register(String section, String docName, boolean isSpecialist, String regName, Double fee){
        this.section = new SimpleStringProperty(section);
        this.docName = new SimpleStringProperty(docName);
        this.type = new SimpleStringProperty(isSpecialist ? "专家号" : "普通号");
        this.regName = new SimpleStringProperty(regName);
        this.money = new SimpleDoubleProperty(fee);
    }
}