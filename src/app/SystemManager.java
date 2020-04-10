package app;

import entities.Mandate;
import entities.MyObject;
import entities.User;

import javax.tools.ForwardingJavaFileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemManager {
    public List<User> users;
    public List<MyObject> objects;
    public User authorized;

    public SystemManager(List<User> users, List<MyObject> objects) {
        this.users = users;
        this.objects = objects;
        this.authorized = null;
    }

    public void save() throws IOException {
        FileManager.saveObjects(objects);
        FileManager.saveUsers(users);
    }

    public boolean userExists(String name) {
        for (User user : users) {
            if (user.name.equals(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean objectExists(String name) {
        for (MyObject obj : objects) {
            if (obj.name.equals(name)) {
                return true;
            }
        }

        return false;
    }

    public User authorize(String name) {
        for (User user : users) {
            if (user.name.equals(name)) {
                authorized = user;
                return user;
            }
        }

        return null;
    }

    public void unauthorize() {
        authorized = null;
    }

    public List<MyObject> getAvailableObjects(User user) {
        ArrayList<MyObject> available = new ArrayList<>();
        for (MyObject object : objects) {
            if (object.mandate <= user.mandate) {
                available.add(object);
            }
        }
        return available;
    }

    public String request(User user, String name) {
        for (MyObject obj : objects) {
            if (obj.name.equals(name)) {
                if (obj.mandate <= user.mandate) {
                    return "Операция прошла успешно.";
                } else {
                    return "Отказ в выполнении операции. Недостаточно прав.";
                }
            }
        }

        return "Ошибка.";
    }

    public String chmod(User user, String target, String mod) {
        String message = "Ошибка";
        byte newmod = -1;
        try {
            newmod = Byte.parseByte(mod);
        } catch (Exception e) {
            return message;
        }

        for (MyObject obj : objects) {
            if (obj.name.equals(target) && user.mandate == Mandate.ROOT) {
                if (newmod >= Mandate.NOT_SECRET && newmod < Mandate.ROOT) {
                    obj.mandate = newmod;
                    message = "Операция прошла успешно.";
                    return message;
                }
            } else {
                message = "Отказ в выполнении операции. Недостаточно прав.";
            }
        }

        for (User usr : users) {
            if (usr.name.equals(target) && user.mandate == Mandate.ROOT) {
                if (newmod >= Mandate.NOT_SECRET && newmod < Mandate.ROOT) {
                    usr.mandate = newmod;
                    message = "Операция прошла успешно.";
                    return message;
                }
            } else {
                message = "Отказ в выполнении операции. Недостаточно прав.";
            }
        }

        return message;
    }
}
