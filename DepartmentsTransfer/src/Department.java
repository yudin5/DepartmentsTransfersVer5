import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Department {
    private String name;
    private ArrayList<Employee> listOfEmployees;
    public ArrayList<ArrayList<Employee>> allPossiblePermutations = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Employee> getListOfEmployees() {
        if (listOfEmployees == null) {
            listOfEmployees = new ArrayList<>();
        }
        return listOfEmployees;
    }

    public void addToEmployeeList(Employee employee) {
        getListOfEmployees().add(employee);
    }

    public void removeFromEmployeeList(Employee employee) {
        getListOfEmployees().remove(employee);
    }

    public ArrayList<String> printAllEmployees() {
        ArrayList<String> composition = new ArrayList<>();
        composition.add(String.format("%-30s%-15s%-8s","ФИО","Отдел","Зарплата"));
        composition.add("-----------------------------------------------------");
        listOfEmployees = getListOfEmployees();
        for (Employee employee : listOfEmployees) {
            String empData = String.format("%-30s%-15s%8s", employee.getName(), this.getName(), employee.getSalary());
            composition.add(empData);
        }
        String averSalarEmp = String.format(String.format("%-45s%8s", "Средняя зарплата ", getAverageSalary().setScale(2, RoundingMode.HALF_EVEN)));
        composition.add(averSalarEmp);
        composition.add("\n\r==================================================================\n\r");
        return composition;
    }

    public BigDecimal getAverageSalary() {
        BigDecimal sum = new BigDecimal(0);
        listOfEmployees = getListOfEmployees();
        for (Employee employee : listOfEmployees) {
            sum = sum.add(employee.getSalary());
        }
        if (listOfEmployees.size() != 0) {
            BigDecimal size = new BigDecimal(listOfEmployees.size());
            return sum.divide(size, 2, RoundingMode.HALF_EVEN);
        } else {
            return BigDecimal.ZERO;
        }

    }

    public void calcAllPossiblePermutations() {
        // Берем список сотрудников и вычисляем все возможные перестановки
        ArrayList<Employee> argList = getListOfEmployees();
        for (int i = 0; i < argList.size(); i++) {
            // Очередной сотрудник
            Employee tmp = argList.get(i);
            // Список из этого сотрудника
            ArrayList<Employee> tmpList = new ArrayList<>();
            tmpList.add(tmp);
            // Добавляем этот список к супер-списку
            allPossiblePermutations.add(tmpList);

            // Проходимся по суперсписку
            int listNumber = allPossiblePermutations.size() - 1;
            for (int j = 0; j < listNumber; j++) {
                ArrayList<Employee> newList = new ArrayList<>();
                // Создаем копию очередного списка из суперлиста
                newList.addAll(allPossiblePermutations.get(j));
                newList.add(tmp);
                allPossiblePermutations.add(newList);
            }
        }
    }

}
