package com.exam;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ExamManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();

        // Insert student data
        System.out.println("Enter student details:");
        System.out.println("Note: Please enter module results as one of the following grades: A+, A, A-, B+, B, B-, C+, C, C-, D+, D, D-, F, AB");
        System.out.print("Enter the number of students: ");
        int numberOfStudents = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numberOfStudents; i++) {
            Student student = new Student();
            System.out.println("");
            System.out.printf("%d. Student ID: ", i + 1);
            student.setStudentId(scanner.nextLine());
            System.out.print("   Student Name: ");
            student.setStudentName(scanner.nextLine());
            for (int j = 0; j < 8; j++) {
                System.out.printf("   Module %d Result: ", j + 1);
                student.getModuleResults()[j] = scanner.nextLine().toUpperCase();
            }

            student.calculateGPA();
            students.add(student);
            insertStudentData(student);
        }

        // Display all results
        displayAllResults();

        // Display top 5 performers
        displayTopPerformers();
    }

    private static void insertStudentData(Student student) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students (student_id, student_name, module1, module2, module3, module4, module5, module6, module7, module8, gpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, student.getStudentId());
            statement.setString(2, student.getStudentName());
            for (int i = 0; i < 8; i++) {
                statement.setString(i + 3, student.getModuleResults()[i]);
            }
            statement.setFloat(11, student.getGpa());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllResults() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")) {

            System.out.println("");
            System.out.println("All Results:");
            System.out.printf("%-8s%-16s", "ID", "Name");
            for (int i = 1; i <= 8; i++) {
                System.out.printf("%-10s", "Module " + i);
            }
            System.out.printf("%-6s%n", "GPA");

            while (resultSet.next()) {
                System.out.printf("%-8s%-16s", resultSet.getString("student_id"), resultSet.getString("student_name"));
                for (int i = 1; i <= 8; i++) {
                    System.out.printf("%-10s", resultSet.getString("module" + i));
                }
                System.out.printf("%-6.2f%n", resultSet.getFloat("gpa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayTopPerformers() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setStudentId(resultSet.getString("student_id"));
                student.setStudentName(resultSet.getString("student_name"));
                String[] moduleResults = new String[8];
                for (int i = 0; i < 8; i++) {
                    moduleResults[i] = resultSet.getString("module" + (i + 1));
                }
                student.setModuleResults(moduleResults);
                student.calculateGPA();
                students.add(student);
            }

            students.sort(Comparator.comparingDouble(Student::getGpa).reversed());

            System.out.println("");
            System.out.println("Top 5 Performers:");
            System.out.printf("%-8s%-16s%-6s%n", "ID", "Name", "GPA");

            for (int i = 0; i < Math.min(5, students.size()); i++) {
                Student student = students.get(i);
                System.out.printf("%-8s%-16s%-6.2f%n", student.getStudentId(), student.getStudentName(), student.getGpa());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
