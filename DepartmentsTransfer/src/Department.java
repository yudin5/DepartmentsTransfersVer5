import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/** Класс служит для хранения департамента со списком сотрудников и возможными перемещениями.
 * Поля: Название, Список сотрудников, Список возможных перестановок сотрудников
 * @author Виталий Юдин
 * @version 1.0
 */
public class Department {
    private String name;
    private ArrayList<Employee> listOfEmployees;
    private ArrayList<ArrayList<Employee>> allPossiblePermutations;

    public ArrayList<ArrayList<Employee>> getPermutations() {
        if (allPossiblePermutations == null) {
            allPossiblePermutations = new ArrayList<ArrayList<Employee>>();
        }
        return allPossiblePermutations;
    }

    public void setPermutations(ArrayList<ArrayList<Employee>> allPossiblePermutations) {
        this.allPossiblePermutations = allPossiblePermutations;
    }

    /** Создает новый департамент с указанным названием
     * @param name название департамента
     * @see TransfersTestDrive#readFile(String)
     */
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

    /** Вычисляет среднюю зарплату департамента
     * @return Возвращает среднюю зарплату департамента в формате BigDecimal
     */
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

    /** Вычисляет все возможные перестановки для департамента
     * @see TransfersTestDrive
     */
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
            getPermutations().add(tmpList);

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
