package Application;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные в формате: Фамилия Имя Отчество датарождения номертелефона пол (разделенные пробелом):");

        String input = scanner.nextLine();
        String[] data = input.split("\\s+"); 

        if (data.length != 6) {
            System.out.println("Ошибка: вы ввели " + (data.length < 6 ? "меньше" : "больше") + " данных, чем требуется.");
            return;
        }

        try {
            String lastName = parseName(data[0], "Фамилия");
            String firstName = parseName(data[1], "Имя");
            String patronymic = parseName(data[2], "Отчество");
            String birthDate = parseBirthDate(data[3]);
            long phoneNumber = parsePhoneNumber(data[4]);
            char gender = parseGender(data[5]);

            String userInfo = String.format("%s %s %s %s %d %c", lastName, firstName, patronymic, birthDate, phoneNumber, gender);

            writeToFile(lastName, userInfo);

            System.out.println("Данные успешно записаны в файл: " + lastName + ".txt");

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close(); 
        }
    }

    private static String parseName(String name, String fieldName) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Ошибка: " + fieldName + " не может быть пустым.");
        }
        return name;
    }

    private static String parseBirthDate(String birthDate) throws IllegalArgumentException {
        String birthDatePattern = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        if (!Pattern.matches(birthDatePattern, birthDate)) {
            throw new IllegalArgumentException("Ошибка: дата рождения должна быть в формате dd.mm.yyyy.");
        }
        return birthDate;
    }

    private static long parsePhoneNumber(String phoneNumberStr) throws IllegalArgumentException {
        try {
            long phoneNumber = Long.parseLong(phoneNumberStr);
            if (phoneNumber <= 0) {
                throw new IllegalArgumentException("Ошибка: номер телефона должен быть положительным числом.");
            }
            return phoneNumber;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка: номер телефона должен быть целым числом.");
        }
    }

    private static char parseGender(String genderStr) throws IllegalArgumentException {
        if (genderStr.length() != 1 || (!genderStr.equalsIgnoreCase("f") && !genderStr.equalsIgnoreCase("m"))) {
            throw new IllegalArgumentException("Ошибка: пол должен быть указан как 'f' или 'm'.");
        }
        return genderStr.charAt(0);
    }

    private static void writeToFile(String lastName, String userInfo) throws IOException {
        File file = new File(lastName + ".txt");
        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(userInfo);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            throw e; 
        }
    }
}
