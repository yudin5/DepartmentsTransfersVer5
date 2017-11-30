import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

/** Класс для решения задачи о возможных переводах сотрудников
 * @author Виталий Юдин
 * @version 1.0
 */
public class TransfersTestDrive {
    public static void main(String[] args) {
        // Проверяем аргументы
        if (args[0] == null || args[1] == null || args.length != 2) {
            System.out.println("Введите корректные пути имен файлов");
        } else {
            String inputFileName = args[0];
            String outputFileName = args[1];

            try {
                // Создаем нашу орг.структуру посредством чтения файла
                ArrayList<Department> departments = readFile(inputFileName);
                // Готовим департаменты, вычисляем все теоретические перестановки
                for (Department d : departments) {
                    d.calcAllPossiblePermutations();
                }
                // Вычисляем возможные переводы в отделах
                ArrayList<String> dispositions = makeDispositions(departments);
                // Выводим полученные результаты в файл
                writeResultToFile(departments, dispositions, outputFileName);
            } catch (IOException ioEx) {
                System.out.println("Ошибка во время чтения / записи файла.");
            } catch (NumberFormatException nfEx) {
                System.out.println("Ошибка с представлением зарплаты.");
            }
        }
    }

    /** Записывает полученные результаты в выходной файл
     * @param departments список всех департаментов
     * @param dispositions список всех возможных перестановок, удовлетворяющих условию
     * @param outputFileName путь к выходному файлу
     * @throws IOException При ошибках записи готового файла
     * @see Department#getPermutations()
     * @see Department#calcAllPossiblePermutations()
     */
    static void writeResultToFile(ArrayList<Department> departments, ArrayList<String> dispositions, String outputFileName) throws IOException {
        // Формируем выходной файл
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFileName));
        // Проходимся по списку отделов и выводим среднюю зарплату в каждом
        for (Department dpt : departments) {
            fileWriter.newLine();
            fileWriter.write("Отдел " + dpt.getName().toUpperCase());
            fileWriter.newLine();
            for (String s : dpt.printAllEmployees()) {
                fileWriter.write(s);
                fileWriter.newLine();
            }
            fileWriter.newLine();
        }

        if (dispositions.size() == 0) {
            System.out.println("Допустимые перестановки отсутствуют.");
        } else {
            fileWriter.newLine();
            fileWriter.write("Чтобы увеличить средние ЗП, возможны следующие варианты переводов: \r\n");
            fileWriter.newLine();
            for (String line : dispositions) {
                fileWriter.write(line + "\r\n");
            }
        }

        fileWriter.close();
        System.out.println("Готово. Проверьте файл с результатом");
    }

    /** Читает файл, полученный в качестве параметра и формирует на его основе структуру департаментов
     * @param inputFileName список всех департаментов
     * @return Возвращает список департаментов
     * @throws IOException При ошибках в исходном файле (нет ФИО, департамента, зарплаты..)
     * @throws NumberFormatException При ошибках в указании зарплаты (отрицательная, не число)
     * @see Department
     * @see Employee
     */
    static ArrayList<Department> readFile(String inputFileName) throws IOException, NumberFormatException {
        ArrayList<Department> departments = new ArrayList<>();
        // Проходимся по файлу, сплитим по разделителю, заполняем списки отделов и сотрудников
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName), "windows-1251"));
        while (fileReader.ready()) {
            String nextLine = fileReader.readLine();
            if (nextLine != null && nextLine.trim().length() != 0) {
                String[] data = nextLine.split(";");
                if (data.length != 3) { // Проверка на количество элементов (должно быть 3)
                    System.out.println("Неверный формат данных.");
                    System.out.println("Данные должны быть указаны в формате <<Фамилия; Отдел; Зарплата(число)>>");
                    System.out.println("Ошибка в строке: \'" + nextLine + "\'");
                    throw new IOException();
                }

                String nextEmployeeName = data[0].trim(); // Фамилия очередного сотрудника
                if (nextEmployeeName.equals(null) || nextEmployeeName.trim().length() == 0) {
                    System.out.println("Отсутствует ФИО сотрудника.");
                    System.out.println("Ошибка в строке: \'" + nextLine + "\'");
                    throw new IOException();
                }

                String nextEmployeeDpt = data[1].trim(); // Департамент очередного сотрудника
                if (nextEmployeeDpt.equals(null) || nextEmployeeDpt.trim().length() == 0) {
                    System.out.println("Отсутствует название департамента сотрудника.");
                    System.out.println("Ошибка в строке: \'" + nextLine + "\'");
                    throw new IOException();
                }

                BigDecimal nextEmployeeSalary = new BigDecimal(data[2].trim()); // Зарплата очередного сотрудника
                if (nextEmployeeSalary.compareTo(new BigDecimal(0)) < 0) {
                    System.out.println("Зарплата не может быть отрицательным числом");
                    System.out.println("Ошибка в строке: \'" + nextLine + "\'");
                    throw new NumberFormatException();
                }

                // Теперь, когда все данные есть, создаем очередного сотрудника
                Employee nextEmployee = new Employee(nextEmployeeName, nextEmployeeSalary);

                boolean contains = false; // Полагаем, что отдела нет в списке
                for (Department dpt : departments) { // Проходимся по списку, ищем этот отдел
                    if (nextEmployeeDpt.equals(dpt.getName())) {
                        contains = true;
                        dpt.addToEmployeeList(nextEmployee); // Отдел найден, просто добавляем туда сотрудника
                    }
                }

                // Если после цикла отдел не найден, то добавляем этот новый отдел в список,
                // заранее добавив в отдел сотрудника.
                if (!contains) {
                    Department nextDepartment = new Department(nextEmployeeDpt);
                    nextDepartment.addToEmployeeList(nextEmployee);
                    departments.add(nextDepartment);
                }
            }
        }
        return departments;
    }

    /** Читает файл, полученный в качестве параметра и формирует на его основе структуру департаментов
     * @param departments список всех департаментов
     * @return Возвращает список возможножных переходов сотрудников, удовлетворяющих условию
     * @see Department#getPermutations()
     * @see Department#getListOfEmployees()
     * @see Department#getAverageSalary()
     * @see Department#addToEmployeeList(Employee)
     * @see Department#removeFromEmployeeList(Employee)
     * @see Employee#getAverSalaryGroup(ArrayList)
     */
    static ArrayList<String> makeDispositions(ArrayList<Department> departments) {
        /*  Вычисляем перевод одного сотрудника
            Для этого необходимы 2 условия:
            1. Чтобы его ЗП была меньше средней по его отделу
            2. Его ЗП должна быть больше средней по другому отделу
        */
        ArrayList<String> dispositions = new ArrayList<>();
        for (Department dpt : departments) {
            BigDecimal averageDptSalary = dpt.getAverageSalary(); // Средняя ЗП отдела
            for (ArrayList<Employee> group : dpt.getPermutations()) { // Пройдемся группам (перестановки) для каждого отдела
                // Если средняя ЗП этой группы больше средней текущего отдела, то останов
                if (Employee.getAverSalaryGroup(group).compareTo(averageDptSalary) > 0) break;
                // Если средняя ЗП этой группы меньше средней в отделе, то перебираем и сравниваем со средними ЗП других отделов
                for (Department dptToTransfer : departments) {
                    if (Employee.getAverSalaryGroup(group).compareTo(dptToTransfer.getAverageSalary()) > 0 && dpt.getListOfEmployees().size() > 1) { // И чтобы там хотя бы 1 сотрудник был
                        // Оба условия выполнены, добавляем этих сотрудников в список допустимых перестановок
                        dispositions.add("==================================================================");
                        dispositions.add("Сотрудники " + group.toString().toUpperCase() +
                                " из <" + dpt.getName() +
                                "> в ----->  <" + dptToTransfer.getName() + ">");

                        // Формируем новый состав отдела - КУДА переводим
                        Department newToDepComposition = new Department(dptToTransfer.getName()); // КУДА переводим. Создаем новые теоретические составы отделов
                        for (Employee e : dptToTransfer.getListOfEmployees()) { // Заносим всех сотрудников отдела в новый отдел для перевода
                            newToDepComposition.addToEmployeeList(e);
                        }
                        // Добавляем туда всю группу сотрудников для перевода
                        for (Employee e : group) {
                            newToDepComposition.addToEmployeeList(e);
                        }
                        // Заносим в выходной список перестановок
                        dispositions.addAll(newToDepComposition.printAllEmployees());

                        // Формируем новый состав отдела - ОТКУДА переводим
                        Department newFromDepComposition = new Department(dpt.getName()); // ОТКУДА переводим.
                        for (Employee e : dpt.getListOfEmployees()) { // Заносим всех сотрудников отдела в новый отдел для перевода
                            newFromDepComposition.addToEmployeeList(e);
                        }
                        // Убираем оттуда сотрудников для перестановки
                        for (Employee e : group) {
                            newFromDepComposition.removeFromEmployeeList(e);
                        }
                        // Заносим в выходной список перестановок
                        dispositions.addAll(newFromDepComposition.printAllEmployees());
                        dispositions.add("\r\n");
                    }
                }
            }
        }
        return dispositions;
    }
}
