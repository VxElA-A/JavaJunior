import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<Department> departments = new ArrayList<>();//Генерация списка с департаментами
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department №"+i));
        }

        List<Person> people = new ArrayList<>();//Генерация списка с людьми
        for (int i = 0; i < 50; i++) {
            people.add(new Person(
                    "Person №"+i,
                    ThreadLocalRandom.current().nextInt(20,61),
                    ThreadLocalRandom.current().nextInt(20000,100000),
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }

        //Вывести на консоль отсортированные (по алфавиту) имена персонов
        printNamesOrdered(people);

        //В каждом департаменте найти самого взрослого сотрудника.
        printDepartmentOldestPerson(people);

        //Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
        findFirstPersons(people);

        //Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
        findTopDepartment(people, departments);
    }
    public static void printNamesOrdered(List<Person> people){
        people.stream().sorted(Comparator.comparing(Person::getName)).forEach(x -> System.out.println(x.getName()));
    }

    public static void printDepartmentOldestPerson(List<Person> people){
        Comparator<Person> AgeComparator = Comparator.comparing(Person::getAge);
        Map<String, Person> MaxAge = people.stream()
                .collect(Collectors.toMap(x -> x.getDepartment().getName(), x -> x, (first, second) -> {
                if(AgeComparator.compare(first, second) > 0){
                    return first;
                }
                return second;
            }));
        System.out.println(MaxAge);

    }

    public static void findFirstPersons(List<Person> people){
        people.stream().filter(x -> x.getAge() < 30).filter(x -> x.getSalary() > 50000).limit(10).forEach(x -> System.out.println(x.getName()));
    }


    public static void findTopDepartment(List<Person> people, List<Department> departments){
        String topDepartment = " ";
        int maxSalary = 0;

        for (int i = 0; i < 10; i++){
            int current_salary = 0;
            for (int j = 0; j < people.size(); j++) {
                if (people.get(j).getDepartment().getName() == departments.get(i).getName()){
                    current_salary += people.get(j).getSalary();
                }
            }
            if (current_salary > maxSalary) {
                maxSalary = current_salary;
                topDepartment = "Department №"+i;
            }
        }
        System.out.println(topDepartment);
    }


    static class Person{
        private String name;
        private int age;
        private int salary;
        private Department department;

        public Person(String name, int age, int salary, Department department) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public int getSalary() {
            return salary;
        }

        public Department getDepartment() {

            return department;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", department=" + department +
                    '}';
        }
    }

    static class Department{
        private String name;

        public Department(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Department that = (Department) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Department{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}

