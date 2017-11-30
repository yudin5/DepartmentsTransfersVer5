import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Employee {
    private String name;
    private BigDecimal salary;

    public static BigDecimal getAverSalaryGroup(ArrayList<Employee> list) {
        if (list == null) return null;
        BigDecimal averGroupSalary = new BigDecimal(0);
        for (Employee person : list) {
            averGroupSalary = averGroupSalary.add(person.getSalary());
        }
        BigDecimal groupSize = new BigDecimal(list.size());
        return averGroupSalary.divide(groupSize, 2, RoundingMode.HALF_EVEN);
    }

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
