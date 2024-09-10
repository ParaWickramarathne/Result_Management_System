package com.exam;


public class Student {
    private String studentId;
    private String studentName;
    private String[] moduleResults = new String[8];
    private float gpa;

    // Constructor
    public Student() {}

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String[] getModuleResults() {
        return moduleResults;
    }

    public void setModuleResults(String[] moduleResults) {
        this.moduleResults = moduleResults;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public void calculateGPA() {
        float totalPoints = 0;
        int creditHours = 16; // 8 modules * 2 credit hours each
        for (String grade : moduleResults) {
            totalPoints += getGradePoint(grade) * 2; // Each module is 2 credit hours
        }
        this.gpa = totalPoints / creditHours;
    }

    private float getGradePoint(String grade) {
        switch (grade) {
            case "A+": return 4.0f;
            case "A": return 4.0f;
            case "A-": return 3.7f;
            case "B+": return 3.3f;
            case "B": return 3.0f;
            case "B-": return 2.7f;
            case "C+": return 2.3f;
            case "C": return 2.0f;
            case "C-": return 1.7f;
            case "D+": return 1.3f;
            case "D": return 1.0f;
            case "D-": return 0.7f;
            case "F": return 0.0f;
            case "AB": return 0.0f; // Assuming AB (Absent) is treated as 0 points
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }
}