package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Date;

public class A3Q1 {
    public static void main(String[] args) {
        // JDBC & Database credentials
        String url = "jdbc:postgresql://localhost:5432/A3Q1";
        String user = "postgres";
        String password = "postgres";
        try { // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                // Ask user choose function to run
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("1. getAllStudents\n" + "2. addStudent\n" + "3. updateStudentEmail\n"
                            + "4. deleteStudent\n" + "0. exit\n"
                            + "Choose the function that you want to run by input their number only:");
                    int num = scanner.nextInt();
                    scanner.nextLine();
                    if (num == 1) {
                        getAllStudents(conn);
                    } else if (num == 2) {
                        // get data of new student
                        System.out.println("Please enter the first name:");
                        String fName = scanner.nextLine();
                        System.out.println("Please enter the last name:");
                        String lName = scanner.nextLine();
                        System.out.println("Please enter the email:");
                        String email = scanner.nextLine();
                        System.out.println("Please enter the enrollment date (YYYY-MM-DD):");
                        String date = scanner.nextLine();
                        addStudent(conn, fName, lName, email, date);
                    } else if (num == 3) {
                        //get student id and email to updata
                        System.out.println("Please enter the student ID:");
                        int studentId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Please enter the new email:");
                        String newEmail = scanner.nextLine();
                        updateStudentEmail(conn, studentId, newEmail);
                    } else if (num == 4) {
                        //get student id to delete student
                        System.out.println("Please enter the student ID to delete:");
                        int idToDelete = scanner.nextInt();
                        deleteStudent(conn, idToDelete);
                    } else if (num == 0) {
                        scanner.close();
                        break;
                    }
                }

            } else {
                System.out.println("Failed to establish connection.");
            } // Close the connection (in a real scenario, do this in a finally
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllStudents(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement(); // Execute SQL query
        String SQL = "SELECT * FROM students";
        ResultSet rs = stmt.executeQuery(SQL); // Process the result
        System.out.println("\n--------------------\n");
        //print each row
        while (rs.next()) {
            System.out.println(rs.getInt("student_id") + " " + rs.getString("first_name") + " "
                    + rs.getString("last_name") + " " + rs.getString("email") + " " + rs.getString("enrollment_date"));
        }
        System.out.println("\n--------------------\n");
    }

    public static void addStudent(Connection conn, String fName, String lName, String email, String date)
            throws SQLException {
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            //set student data
            pstmt.setString(1, fName);
            pstmt.setString(2, lName);
            pstmt.setString(3, email);
            pstmt.setDate(4, Date.valueOf(date));
            pstmt.executeUpdate();
            System.out.println("Student added successfully. \n");
        }

    }

    public static void updateStudentEmail(Connection conn, int studentId, String newEmail) throws SQLException {
        String SQL = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //update student email
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, studentId);
            int numUpd = pstmt.executeUpdate();
            if (numUpd > 0) {
                System.out.println("Email updated successfully.\n");
            } else {
                System.out.println("Student not found.\n");
            }
        }
    }

    public static void deleteStudent(Connection conn, int studentId) throws SQLException {
        String SQL = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            //delete student data by id
            pstmt.setInt(1, studentId);
            int numUpd = pstmt.executeUpdate();
            if (numUpd > 0) {
                System.out.println("Student deleted successfully.\n");
            } else {
                System.out.println("Student not found.\n");
            }
        }
    }
}