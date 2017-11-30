import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/** Класс служит для хранения сотрудника со свойствами ФИО и Зарплата.
 * @author Виталий Юдин
 * @version 1.0
 */
public class Employee {
    /**Свойство - ФИО*/
    private String name;
    /**Свойство - Зарплата*/
    private BigDecimal salary;

    /** Метод для расчета средней зарплаты отдельной группы сотрудников.
     *  Как правило, это группа из одного отдела.
     * @param list Список сотрудников
     * @return Возвращает среднюю зарплату группы сотрудников, переданной в качестве аргумента
     */
    public static BigDecimal getAverSalaryGroup(ArrayList<Employee> list) {
        if (list == null) return null;
        BigDecimal averGroupSalary = new BigDecimal(0);
        for (Employee person : list) {
            averGroupSalary = averGroupSalary.add(person.getSalary());
        }
        BigDecimal groupSize = new BigDecimal(list.size());
        return averGroupSalary.divide(groupSize, 2, RoundingMode.HALF_EVEN);
    }

    /** Создает нового сотрудника с заданными значениями
     * @param name ФИО
     * @param salary Зарплата
     * @see Department#listOfEmployees
     */
    public Employee(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String toString() {
        return getName();
    }
}
