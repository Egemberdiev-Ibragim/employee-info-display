package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Test {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static Connection CONNECTION;
    private static Statement STATEMENT;
    private static ResultSet RS;


    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String query = "SELECT * FROM developers";
        Company company = new Company();
        try {
            ArrayList<Developer> developers = new ArrayList<>();
            CONNECTION = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            STATEMENT = CONNECTION.createStatement();
            RS = STATEMENT.executeQuery(query);

            System.out.println("Выберите функционал:" + "\n"
                    + "(1)Вывести информацию о работниках " + "\n"
                    + "(2)Добавить работников " + "\n"
                    + "(3)Удаление работника" + "\n"
                    + "(4)Сортировать по критериям");

            int num = scanner.nextInt();
            scanner.nextLine();
            switch (num) {
                case 1:
                    printEmployee(developers);
                    break;
                case 2:
                    System.out.println("Введите данные!");

                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine().trim();

                    System.out.print("Введите специальность:");
                    String specialty = scanner.nextLine().trim();

                    BigDecimal salary = null;
                    while (salary == null) {
                        System.out.print("Введите зарплату: ");
                        String salaryInput = scanner.nextLine().trim();

                        if (!salaryInput.isEmpty()) {
                            try {
                                salary = new BigDecimal(salaryInput);
                            } catch (NumberFormatException e) {
                                System.out.println("Ошибка ввода. Введите корректное число.");
                            }
                        } else {
                            System.out.println("Ошибка ввода. Поле не может быть пустым. Попробуйте еще раз.");
                        }
                    }

                    Developer developer = new Developer(name, specialty, salary);
                    company.addDeveloper(developer);
                    addEmployeeFromDatabase(developer);
                    break;
                case 3:
                    try {
                        System.out.println("Увольнение работника!");
                        System.out.println("Выберите работника:");
                        printEmployee(developers);
                        System.out.print("Ввод: ");
                        int index = scanner.nextInt() - 1;
                        company.deleteDeveloper(index);
                        deleteEmployeeFromDatabase(developers, index);
                        break;
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Ошибка. Некорректный индекс");
                    }
                case 4:
                    System.out.println("Выберите критерий сортировки:" + "\n"
                            + "(1) По имени" + "\n"
                            + "(2) По специальности" + "\n"
                            + "(3) По зарплате");

                    int sortBy = scanner.nextInt();
                    sortEmployee(sortBy);
                    break;

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                if (CONNECTION != null) {
                    CONNECTION.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
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

    public static ArrayList<Developer> printEmployee(ArrayList<Developer> developers) throws SQLException {
        System.out.println("Информация о работниках: \n");
        while (RS.next()) {
            String name = RS.getString("name");
            String specialty = RS.getString("specialty");
            BigDecimal salary = RS.getBigDecimal("salary");

            Developer developer = new Developer(name, specialty, salary);
            developers.add(developer);
        }
        int i = 1;
        for (Developer developer : developers) {
            System.out.println("Id: " + i + "\n" + "Имя: " + developer.getName() + "\n"
                    + "Специальность: " + developer.getSpecialty() + "\n"
                    + "Зарплата: " + developer.getSalary() + "\n"
            );
            i++;
        }
        return developers;
    }

    public static void addEmployeeFromDatabase(Developer developer) throws SQLException {
        CONNECTION = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        String insert = "INSERT INTO developers (name, specialty, salary) VALUES (?, ?, ?)";
        PreparedStatement preparedStatementInsert = CONNECTION.prepareStatement(insert);
        preparedStatementInsert.setString(1, (developer.getName() != null) ? developer.getName() : null);
        preparedStatementInsert.setString(2, developer.getSpecialty());
        preparedStatementInsert.setBigDecimal(3, (developer.getSalary() != null) ? developer.getSalary() : null);
        preparedStatementInsert.executeUpdate();
        System.out.println("Новый разработчик успешно добавлен в таблицу.");
    }


    private static void deleteEmployeeFromDatabase(ArrayList<Developer> developers, int index) throws SQLException {
        CONNECTION = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        String delete = "DELETE FROM developers WHERE name = ? AND specialty = ? AND salary = ?";
        PreparedStatement preparedStatementDelete = CONNECTION.prepareStatement(delete);
        preparedStatementDelete.setString(1, developers.get(index).getName());
        preparedStatementDelete.setString(2, developers.get(index).getSpecialty());
        preparedStatementDelete.setBigDecimal(3, developers.get(index).getSalary());
        int check = preparedStatementDelete.executeUpdate();

        if (check > 0) {
            System.out.println("Разработчик успешно удален из базы данных.");
        } else {
            System.out.println("Разработчик с указанным ID не найден в базе данных.");
        }
    }
    public static void sortEmployee(int count) throws SQLException {
        String sortQuery;
        switch (count) {
            case 1:
                sortQuery = "SELECT * FROM developers ORDER BY name";
                break;
            case 2:
                sortQuery = "SELECT * FROM developers ORDER BY specialty";
                break;
            case 3:
                sortQuery = "SELECT * FROM developers ORDER BY salary";
                break;
            default:
                throw new IllegalArgumentException("Некорректный критерий сортировки");
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sortQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Список отсортирован!");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialty = resultSet.getString("specialty");
                BigDecimal salary = resultSet.getBigDecimal("salary");

                System.out.println("Id: " + id + "\n" +
                        "Имя: " + name + "\n" +
                        "Специальность: " + specialty + "\n" +
                        "Зарплата: " + salary + "\n");
            }
        }
    }

}