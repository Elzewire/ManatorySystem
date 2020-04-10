package app;

import entities.MyObject;
import entities.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<User> getUsers() throws IOException {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader("etc/users"));
        String row = r.readLine();
        while (row != null) {
            String[] data = row.split(",");
            users.add(new User(data[0], Byte.parseByte(data[1])));
            row = r.readLine();
        }
        r.close();

        return users;
    }

    public static List<MyObject> getObjects() throws IOException {
        ArrayList<MyObject> objects = new ArrayList<>();
        BufferedReader r = new BufferedReader(new FileReader("etc/objects"));
        String row = r.readLine();
        while (row != null) {
            String[] data = row.split(",");
            objects.add(new MyObject(data[0], Byte.parseByte(data[1])));
            row = r.readLine();
        }
        r.close();

        return objects;
    }

    public static void saveUsers(List<User> users) throws IOException {
        FileWriter w = new FileWriter("etc/users");
        for (User usr : users) {
            w.write(usr.name + "," + usr.mandate + "\n");
        }
        w.close();
    }

    public static void saveObjects(List<MyObject> objects) throws IOException {
        FileWriter w = new FileWriter("etc/objects");
        for (MyObject obj: objects) {
            w.write(obj.name + "," + obj.mandate + "\n");
        }
        w.close();
    }
}
