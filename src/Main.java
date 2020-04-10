import app.FileManager;
import app.SystemManager;
import entities.MyObject;
import entities.User;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        // Initialize command line
        User user = null;
        Scanner sc = new Scanner(System.in);
        SystemManager manager = new SystemManager(FileManager.getUsers(), FileManager.getObjects());
        String input = "";

        //Authorize
        while (!input.equals("exit")) {
            System.out.print("User: ");
            input = sc.nextLine();

            if (!input.equals("exit")) {
                if (manager.userExists(input)) {
                    user = manager.authorize(input);
                    System.out.println("Идентификация прошла успешно, добро пожаловать в систему ");

                    // List all available objects
                    String message = "Список доступных объектов: ";
                    for (MyObject obj : manager.getAvailableObjects(user)) {
                        message += obj.name + "; ";
                    }
                    System.out.println(message);
                } else {
                    System.out.println("Пользователя \"" + input + "\" не существует");
                }
            }

            while (user != null) {
                System.out.print("Жду ваших указаний > ");
                input = sc.nextLine();

                switch (input.split(" ")[0]) {
                    case "exit":
                        System.out.println("Работа пользователя \"" + user.name + "\" завершена. До свидания!");
                        manager.unauthorize();
                        user = null;
                        input = "";
                        break;

                    case "request":
                        if (input.split(" ").length == 2) {
                            String name = input.split(" ")[1];
                            if (manager.objectExists(name)) {
                                System.out.println(manager.request(user, name));
                            } else {
                                System.out.println("Объекта \"" + input + "\" не существует");
                            }
                        } else {
                            System.out.println("Неверное число аргументов команды");
                        }
                        break;

                    case "chmod":
                        if (input.split(" ").length == 3) {
                            String target = input.split(" ")[1];
                            String mod = input.split(" ")[2];
                            if (manager.objectExists(target) || manager.userExists(target)) {
                                System.out.println(manager.chmod(user, target, mod));
                            } else {
                                System.out.println("Объекта/пользователя \"" + input + "\" не существует");
                            }
                        } else {
                            System.out.println("Неверное число аргументов команды");
                        }
                        break;

                    default:
                        System.out.println("Команды \"" + input + "\" не существует");
                        break;
                }
            }

            manager.save();
        }
    }
}
