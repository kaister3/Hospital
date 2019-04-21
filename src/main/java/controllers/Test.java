package main.java.controllers;
import main.java.Config;

public class Test {
    String sql = "select reg." + Config.NameTableColumnRegisterNumber +
            ",pat." + Config.NameTableColumnPatientName +
            ",reg." + Config.NameTableColumnRegisterDateTime +
            ",cat." + Config.NameTableColumnCategoryRegisterIsSpecialist +
            " from (select " + Config.NameTableColumnRegisterNumber +
                    "," + Config.NameTableColumnRegisterPatientNumber +
                    "," + Config.NameTableColumnRegisterDateTime +
                    "," + Config.NameTableColumnRegisterCategoryNumber +
                    " from " + Config.NameTableRegister +
                    ") as reg"  +
            " inner join (select " + Config.NameTableColumnPatientNumber +
                    "," + Config.NameTableColumnPatientName +
                    " from " + Config.NameTablePatient +
                    ") as pat"  +
            " on reg." + Config.NameTableColumnRegisterPatientNumber +
            "=pat." + Config.NameTableColumnPatientNumber +
            " inner join (select " + Config.NameTableColumnCategoryRegisterNumber +
                    "," + Config.NameTableColumnCategoryRegisterIsSpecialist +
                    " from " + Config.NameTableCategoryRegister +
                    ") as cat"  +
            " on reg." + Config.NameTableColumnRegisterCategoryNumber +
            "=cat." + Config.NameTableColumnCategoryRegisterNumber;
}
