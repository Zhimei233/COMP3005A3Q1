package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/University";
        String user = "postgres";
        String password = "postgres";

        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM student");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.print(resultSet.getString("id") + "\t");
                System.out.print(resultSet.getString("name") + "\t");
                System.out.println(resultSet.getString("tot_cred"));
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}