package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static Connection CONNECTION;
    private static Statement STATEMENT;
    private static ResultSet RS;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner scanner = new Scanner(System.in);
        String query = "SELECT * FROM developers";
        try {
            ArrayList<Developer> developers = new ArrayList<>();
            CONNECTION = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            STATEMENT = CONNECTION.createStatement();
            RS = STATEMENT.executeQuery(query);
            System.out.println("Выберите функционал:" + "\n" + "(1)Вывести информацию о работниках " + "\n");
            int num = scanner.nextInt();
            switch (num) {
                case 1:
                    printEmployee(developers);

                    break;
                //TODO Место для последующего функционала, пока не придумал)))
            }
            try {
                if (CONNECTION != null) {
                    CONNECTION.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {

            try {
                CONNECTION.close();
            } catch (SQLException se) {
                System.out.println("Произошла ошибка при закрытии ");
            }
            try {
                STATEMENT.close();
            } catch (SQLException se) {
                System.out.println("Произошла ошибка при закрытии ");
            }
            try {
                RS.close();
            } catch (SQLException se) {
                System.out.println("Произошла ошибка при закрытии ");
            }
        }
    }

    public static void printEmployee(ArrayList<Developer> developers) throws SQLException {
        while (RS.next()) {
            String name = RS.getString("name");
            String specialty = RS.getString("specialty");
            BigDecimal salary = RS.getBigDecimal("salary");

            Developer developer = new Developer(name, specialty, salary);
            developers.add(developer);
            for (Developer razrab : developers) {
                System.out.println("Имя: " + developer.getName() + "\n" + "Специальность: " + developer.getSpecialty() + "\n" + "Зарплата: " + developer.getSalary());
            }
        }
    }
}