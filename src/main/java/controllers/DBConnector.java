package main.java.controllers;

import main.java.Config;

import java.sql.*;

public class DBConnector {
    private static DBConnector instance = null;
    private Connection connection;
    private Connection transactionConnection;
    private Statement statement;
    private Statement transactionStatement;
    private final static String URL = "jdbc:mysql://localhost:3306/hospital";

    private DBConnector() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    static public DBConnector getInstance() {
        try {
            if (instance == null)
                instance = new DBConnector();
        } catch (ClassNotFoundException e) {
            System.err.print("cannot load sql driver.");
            System.exit(1);
        }
        return instance;
    }

    public void connectDataBase( ) throws SQLException {
        connection = DriverManager.getConnection(URL, "root", "");
        statement = connection.createStatement();
        System.out.println("Hello world");
        transactionConnection =  DriverManager.getConnection(URL, "root", "");
        transactionConnection.setAutoCommit(false);
        transactionStatement = transactionConnection.createStatement();
    }

    public void disconnectDataBase() throws SQLException {
        connection.close();
    }

    public ResultSet getWholeTable(String tableName){
        try {
            return statement.executeQuery("select * from " + tableName);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * get patient's password by patient id
     * @param number
     * @return null if patient not found, else the password
     */
    public String getPatientPassword(String number) {
        try {
            ResultSet res =  statement.executeQuery(
                    "select " + Config.NameTableColumnPatientPassword +
                            " from " + Config.NameTablePatient +
                            " where " + Config.NameTableColumnPatientNumber + "=" + number);
            if (!res.next())
                return null;
            return res.getString(Config.NameTableColumnPatientPassword);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getPatientInfo(String number) {
        try {
            System.out.println("hellop");
            return statement.executeQuery(
                    "select * from " + Config.NameTablePatient +
                            " where " + Config.NameTableColumnPatientNumber + "=" + number);
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getDoctorInfo(String number) {
        try {
            return statement.executeQuery(
                    "select * from " + Config.NameTableDoctor +
                            " where " + Config.NameTableColumnDoctorNumber + "=" + number + ";");
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * try adding register info to the database
     * @param registerCategoryNumber register category number
     * @param doctorNumber doctor number
     * @param patientNumber patient number
     * @param registerFee register fee
     * @return register number if registeration is successful, null otherwise.
     * @throws RegisterException if register failed
     */
    public int tryRegister(
            String registerCategoryNumber,
            String doctorNumber,
            String patientNumber,
            Double registerFee,
            boolean deductFromBalance,
            Double addToBalance) throws RegisterException {
        try{
            // decide the register id
            ResultSet result = transactionStatement.executeQuery(
                    "select * from " + Config.NameTableRegister +
                            " order by " + Config.NameTableColumnRegisterNumber +
                            " desc limit 1"
            );
            int regNumber, currentCount;
            if (!result.next())
                regNumber = 0;
            else
                regNumber = Integer.parseInt(result.getString(Config.NameTableColumnRegisterNumber)) + 1;

            result = transactionStatement.executeQuery(
                    "select * from " +  Config.NameTableRegister +
                            " where " + Config.NameTableColumnRegisterCategoryNumber +
                            "=" + registerCategoryNumber +
                            " order by " + Config.NameTableColumnCategoryRegisterNumber +
                            " desc limit 1"
            );
            if(!result.next())
                currentCount = 0;
            else
                currentCount = result.getInt(Config.NameTableColumnRegisterCurrentRegisterCount);

            // decide patient id
            result = transactionStatement.executeQuery(
                    "select * from " + Config.NameTablePatient +
                            " where " + Config.NameTableColumnPatientNumber +
                            "=" + patientNumber
            );
            if(!result.next())
                throw new RegisterException("patient does not exist", RegisterException.ErrorCode.patientNotExist);

            double balance = result.getDouble(Config.NameTableColumnPatientBalance);

            // decide if exceeded the max register count
            result = transactionStatement.executeQuery(
                    "select " + Config.NameTableColumnCategoryRegisterMaxRegisterNumber +
                            " from " + Config.NameTableCategoryRegister +
                            " where " + Config.NameTableColumnCategoryRegisterNumber +
                            "=" + registerCategoryNumber
            );
            int maxRegCount;
            if(!result.next())
                throw new RegisterException("illegal table entry",
                        RegisterException.ErrorCode.registerCategoryNotFound);
            maxRegCount = result.getInt(Config.NameTableColumnCategoryRegisterMaxRegisterNumber);

            if(currentCount > maxRegCount) {
                throw new RegisterException("max register number reached",
                        RegisterException.ErrorCode.registerNumberExceeded);
            }

            // try insert
            transactionStatement.executeUpdate(
                    String.format(
                            "insert into %s values (\"%06d\",\"%s\",\"%s\",\"%s\",%d,false,%s, current_timestamp)",
                            Config.NameTableRegister,
                            regNumber,
                            registerCategoryNumber,
                            doctorNumber,
                            patientNumber,
                            currentCount + 1,
                            registerFee
                    )
            );

            // deduct from balance
            if(deductFromBalance){
                transactionStatement.executeUpdate(
                        String.format("update %s set %s=%.2f where %s=%s",
                                Config.NameTablePatient,
                                Config.NameTableColumnPatientBalance,
                                (balance -= registerFee),
                                Config.NameTableColumnPatientNumber,
                                patientNumber)
                );
            }

            if(addToBalance != 0) {
                transactionStatement.executeUpdate(
                        String.format("update %s set %s=%.2f where %s=%s",
                                Config.NameTablePatient,
                                Config.NameTableColumnPatientBalance,
                                (balance += addToBalance),
                                Config.NameTableColumnPatientNumber,
                                patientNumber)
                );
            }

            // all successfulHH
            transactionConnection.commit();
            return regNumber;
        } catch (SQLException e) {
            try {
                transactionConnection.rollback();
            } catch (SQLException ee) { }
            throw new RegisterException("sql exception occurred", RegisterException.ErrorCode.sqlException);
        }
    }

    public ResultSet getRegisterInfo(String ksmc, String ysmc, String sfzj, String hzmc) {
        //filters
        String filterKSMC = "", filterYSMC = "", filterSFZJ = "", filterHZMC = "";
        int flag = 0;
        if (ksmc == "") {
            filterKSMC = "where ";
        }
        else {
            filterKSMC = "where a.KSMC = \"" + ksmc + "\" ";
            flag = 1;
        }
        if (ysmc == "") {
            filterYSMC = "";
        }
        else {
            if (flag == 1) { filterYSMC += "and ";}
            filterYSMC += "a.YSMC = \"" + ysmc + "\" ";
            flag = 1;
        }
        if (sfzj == "") {
            filterSFZJ = "";
        }
        else {
            if (flag == 1) { filterSFZJ += "and ";}
            filterSFZJ += "a.SFZJ = " + sfzj + " ";
            flag = 1;
            //String sfzj can only be 1 or 0.
        }
        if (hzmc == "") {
            filterHZMC = "";
        }
        else {
            if (flag == 1) { filterHZMC += "and ";}
            filterHZMC += "a.HZMC = \"" + hzmc + "\" ";
            flag = 1;
        }
        if (flag == 0) {
            filterKSMC = "";
        }
        try {
            String sql = "select * " +
                    "from (select t_ksxx.KSMC, t_ksys.YSMC, t_ksys.SFZJ, t_hzxx.HZMC, t_hzxx.GHFY, t_hzxx.GHRS " +
                    "from t_ksxx, t_ksys, t_hzxx " +
                    "where t_ksys.KSBH = t_ksxx.KSBH and t_ksxx.KSBH = t_hzxx.KSBH) as a " +
                    filterKSMC + filterYSMC + filterSFZJ + filterHZMC;
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getPatientForDoctor() {
        try {
            String sql = "select a.GHBH, b.BRMC, a.RQSJ, c.SFZJ from " +
                    "  (" +
                    "  (select * from t_ghxx) as a " +
                    "  inner join (select * from t_brxx) as b on a.BRBH = b.BRBH " +
                    "    inner join (select * from t_hzxx) as c on a.HZBH = c.HZBH " +
                    "    )";
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getIncomeInfo() {
        try {
            String sql = "select t_ksxx.KSMC, t_ksys.YSBH, t_ksys.YSMC, t_ksys.SFZJ, t_ghxx.GHRC " +
                    "from t_ksxx, t_ksys, t_ghxx " +
                    "where t_ksys.KSBH = t_ksxx.KSBH and t_ksys.YSBH = t_ghxx.YSBH";
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePatientLoginTime(String patientId, String time){
        try {
            statement.executeUpdate(
                    "update " + Config.NameTablePatient +
                            " set " + Config.NameTableColumnPatientLastLogin +
                            "=\"" + time +
                            "\" where " + Config.NameTableColumnPatientNumber +
                            "=" + patientId
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public void updateDoctorLoginTime(String doctorId, String time){
        try {
            statement.executeUpdate(
                    "update " + Config.NameTableDoctor +
                            " set " + Config.NameTableColumnDoctorLastLogin +
                            "=\"" + time +
                            "\" where " + Config.NameTableColumnRegisterDoctorNumber+
                            "=" + doctorId
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }
}

class RegisterException extends Exception {
    public enum ErrorCode {
        noError,
        registerCategoryNotFound,
        registerNumberExceeded,
        patientNotExist,
        sqlException,
        retryTimeExceeded,
    }
    ErrorCode error;
    RegisterException(String reason, ErrorCode err){
        super(reason);
        error = err;
    }
}
