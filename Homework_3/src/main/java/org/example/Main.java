package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try(Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")){
            acceptConnection(connection);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createTable(connection);
        insertTable(connection);
        Student.saveOrupdate(connection, 6, "Sasuke", "Uchiha", 15, "alive");
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from student");
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String second_name = resultSet.getString(3);
                int age = resultSet.getInt(4);
                String status = resultSet.getString(5);
                System.out.println("id - "+id+"; name - "+name+"; second_name - "+second_name+"; age - "+age+"; status - "+status+";" );
            }
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");

        UchihaIncident(connection);
    }

    private static void UchihaIncident(Connection connection) throws SQLException {
        try(Statement statement = connection.createStatement()){
            statement.execute("update student set status = 'dead' where second_name = 'Uchiha' and name != 'Itachi' and name != 'Sasuke'");
            ResultSet resultSet = statement.executeQuery("select * from student");
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String second_name = resultSet.getString(3);
                int age = resultSet.getInt(4);
                String status = resultSet.getString(5);
                System.out.println("id - "+id+"; name - "+name+"; second_name - "+second_name+"; age - "+age+"; status - "+status+";" );
            }
        }
    }

    private static void insertTable(Connection connection) throws SQLException {
        try(Statement statement = connection.createStatement()){
            statement.execute("""
                    insert into Student(id, name, second_name, age, status) values
                    (1, 'Itachi', 'Uchiha', 19, 'alive'),
                    (2, 'Kakashi', 'Hatake', 21, 'alive'),
                    (3, 'Shisui', 'Uchiha', 20, 'alive'),
                    (4, 'Naruto', 'Uzumaki', 15, 'alive'),
                    (5, 'Izumi', 'Uchiha', 20, 'alive')
                    """);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try(Statement statement = connection.createStatement()){
            statement.execute("""
                create table Student(
                id bigint,
                name varchar(256),
                second_name varchar(256),
                age int,
                status varchar(256)
                )
                """);
        }
    }


}