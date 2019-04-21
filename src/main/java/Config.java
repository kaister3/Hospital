package main.java;

public class Config {
    //tables
    public static String NameTableDepartment = "t_ksxx";
    public static String NameTableDoctor = "t_ksys";
    public static String NameTableCategoryRegister = "t_hzxx";
    public static String NameTablePatient = "t_brxx";
    public static String NameTableRegister = "t_ghxx";

    //KSXX
    public static String NameTableColumnDepartmentNumber = "KSBH";
    public static String NameTableColumnDepartmentName = "KSMC";
    public static String NameTableColumnDepartmentPronounce = "PYZS";

    //KSYS
    public static String NameTableColumnDoctorNumber = "YSBH";
    public static String NameTableColumnDoctorDepartmentNumber = "KSBH";
    public static String NameTableColumnDoctorName = "YSMC";
    public static String NameTableColumnDoctorPronounce = "PYZS";
    public static String NameTableColumnDoctorPassword = "DLKL";
    public static String NameTableColumnDoctorIsSpecialist = "SFZJ";
    public static String NameTableColumnDoctorLastLogin = "DLRQ";

    //HZXX
    public static String NameTableColumnCategoryRegisterNumber = "HZBH";
    public static String NameTableColumnCategoryRegisterName = "HZMC";
    public static String NameTableColumnCategoryRegisterPronounce = "PYZS";
    public static String NameTableColumnCategoryRegisterDepartment = "KSBH";
    public static String NameTableColumnCategoryRegisterIsSpecialist = "SFZJ";
    public static String NameTableColumnCategoryRegisterMaxRegisterNumber = "GHRC";
    public static String NameTableColumnCategoryRegisterFee = "GHFY";

    //BRBH
    public static String NameTableColumnPatientNumber = "BRBH";
    public static String NameTableColumnPatientName = "BRMC";
    public static String NameTableColumnPatientPassword = "DLKL";
    public static String NameTableColumnPatientBalance = "YCJE";
    public static String NameTableColumnPatientLastLogin = "DLRQ";

    //GHXX
    public static String NameTableColumnRegisterNumber = "GHBH";
    public static String NameTableColumnRegisterCategoryNumber = "HZBH";
    public static String NameTableColumnRegisterDoctorNumber = "YSBH";
    public static String NameTableColumnRegisterPatientNumber = "BRBH";
    public static String NameTableColumnRegisterCurrentRegisterCount = "GHRC";
    public static String NameTableColumnRegisterUnregister = "THBZ";
    public static String NameTableColumnRegisterFee = "GHFY";
    public static String NameTableColumnRegisterDateTime = "RQSJ";
    public static String NameTableColumnCureDateTime = "KBSJ";

}