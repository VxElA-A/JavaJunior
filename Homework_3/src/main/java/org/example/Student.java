package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Student {
    private int id;
    private String name;
    private String second_name;
    private int age;
    private String status;

    public Student(int id, String name, String second_name, int age, String status) {
        this.id = id;
        this.name = name;
        this.second_name = second_name;
        this.age = age;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", second_name='" + second_name + '\'' +
                ", age=" + age +
                ", status='" + status + '\'' +
                '}';
    }

    public Student(){
    }
    public static void saveOrupdate(Connection connection, int id, String name, String second_name, int age, String status ) throws SQLException{
        Student student = new Student(id,name,second_name,age,status);
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from student");
            while (resultSet.next()){
                if(resultSet.getInt(1) == id){
                    statement.execute("delete student where id ="+resultSet.getInt(1));
                }
            }
            statement.execute(" insert into Student(id, name, second_name, age, status) values (" + id + ", '"+name+"', '"+second_name+"', "+age+", '"+status+"')");
        }
    }

}
