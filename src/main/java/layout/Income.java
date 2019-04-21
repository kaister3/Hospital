package main.java.layout;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Income extends RecursiveTreeObject<Income> {
    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getIncome() {
        return income.get();
    }

    public StringProperty incomeProperty() {
        return income;
    }

    public void setIncome(String income) {
        this.income.set(income);
    }

    public StringProperty department;
    public StringProperty id;
    public StringProperty name;
    public StringProperty type;
    public StringProperty number;
    public StringProperty income;

    public Income(String depName, String docNum, String docName, boolean isSpec, int regNumPeople, Double incomSum){
        this.department = new SimpleStringProperty(depName);
        this.id = new SimpleStringProperty(docNum);
        this.name= new SimpleStringProperty(docName);
        this.type = new SimpleStringProperty(isSpec ? "专家号" : "普通号");
        this.number = new SimpleStringProperty(Integer.toString(regNumPeople));
        this.income = new SimpleStringProperty(String.format("%.2f", incomSum));
    }
}