package server.util;

import com.google.gson.Gson;
import server.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;

public class ModelLoader {
    private final File userDirectory;
    /**
     * DO NOT CHANGE ANYTHING IN CONSTRUCTOR.
     */
    public ModelLoader() {
        userDirectory = Config.getConfig("mainConfig").getProperty(File.class, "userDirectory");
        if (!userDirectory.exists()) userDirectory.mkdirs();
        Ann();
    }


    public void Ann(){
        File File  = Config.getConfig("mainConfig").getProperty(File.class, "blueColor");
        File File1  = Config.getConfig("mainConfig").getProperty(File.class, "redColor");
        File File2  = Config.getConfig("mainConfig").getProperty(File.class, "database");
        File File3  = Config.getConfig("mainConfig").getProperty(File.class, "login");
        File File4  = Config.getConfig("mainConfig").getProperty(File.class, "signUp");
        File File5  = Config.getConfig("mainConfig").getProperty(File.class, "mainPage");
        File File6  = Config.getConfig("mainConfig").getProperty(File.class, "settings");
        File File7  = Config.getConfig("mainConfig").getProperty(File.class, "groups");
        File File8  = Config.getConfig("mainConfig").getProperty(File.class, "tweetsPane");
        File File9  = Config.getConfig("mainConfig").getProperty(File.class, "messages");
        File File10  = Config.getConfig("mainConfig").getProperty(File.class, "port");
        File File11 = Config.getConfig("mainConfig").getProperty(File.class, "connection");

    }
    public void save(LinkedList<?> users,String name) {
        try {
            File file = getUserFile(name+".txt");
            assert file != null;
            PrintStream printStream = new PrintStream(file);
            Gson gson = new Gson();
            for (Object us : users) {
                String jsonInString = gson.toJson(us);
                printStream.print(jsonInString);
                printStream.println();
            }

            printStream.close();
        } catch (FileNotFoundException e) {
        }
    }


    public LinkedList<User> loadUsers() {
        try {
            File userFile = getUserFile("Users.txt");
            if (userFile == null){
                System.out.println("not found");
            } else {
                Gson gson = new Gson();
                Scanner ss = new Scanner(userFile);
                LinkedList<User> users = new LinkedList<>();
                while (ss.hasNextLine()) {
                    User user = gson.fromJson(ss.nextLine(), User.class);
                    users.add(user);
                }
                return users;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private File getUserFile(String name) {
        File directory = new File(userDirectory.getAbsolutePath());
        File[] d = directory.listFiles();
        if (d != null) {
            for (File dd : d) {
                if (dd.getName().equals(name)) {
                    return dd;
                }
            }
            return null;
        } else {
            return null;

        }
    }

}
