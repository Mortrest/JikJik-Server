package server.logic;

import server.handlers.Lobby;
import server.handlers.ClientConnection;
import server.handlers.SignUpHandler;
import server.model.User;
import server.util.ModelLoader;

import java.util.LinkedList;
import java.util.Random;

public class Users {
    static ModelLoader ml;
    static LinkedList<User> users;
    static LinkedList<Lobby> lobbies;
    static LinkedList<SignUpHandler> onlines;
    static LinkedList<ClientConnection> clientConnections;

    public Users(ModelLoader modelLoader) {
        users = modelLoader.loadUsers();
        ml = modelLoader;
        lobbies = new LinkedList<>();
        onlines = new LinkedList<>();
        clientConnections = new LinkedList<>();
    }

    public static String signUp(String username, String password) {
        if (!search(username)) {
            User user = new User(username, password);
            users.add(user);
            ml.save(users, "Users");
            return "Signed Up";
        } else {
            return "Username exists";
        }
    }

    public static void won(String winner, String loser) {
        User win = searchUser(winner);
        User lose = searchUser(loser);
        assert win != null;
        win.setWins(win.getWins() + 1);
        assert lose != null;
        lose.setLosses(lose.getLosses() + 1);
        lobbies.removeIf(lobby -> lobby.getGameState().getPlayer1().equals(winner) || lobby.getGameState().getPlayer2().equals(winner));
        save();
    }

    public static void save() {
        ml.save(users, "Users");
    }

    private static boolean search(String username) {
        for (User user : users) {
            return user.getUsername().equals(username);
        }
        return false;
    }

    public static User searchUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean searchOnline(String username) {
        for (SignUpHandler sh : onlines) {
            if (sh.getUser().getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static LinkedList<Lobby> getLobbies() {
        return lobbies;
    }

    public static void setLobbies(LinkedList<Lobby> lobbies) {
        Users.lobbies = lobbies;
    }

    public static LinkedList<User> rank() {
        LinkedList<User> copy = (LinkedList<User>) users.clone();
        LinkedList<User> del = new LinkedList<>();
        while (true) {
            if (copy.size() == 0) {
                break;
            } else {
                User user = copy.getFirst();
                for (User us : copy) {
                    if (us.getWins() > user.getWins()) {
                        user = us;
                    }
                }
                del.add(user);
                copy.remove(user);
            }
        }
        return del;
    }

    public static String signIn(String user, String pass) {
        System.out.println(user + " " + pass);
        System.out.println(users.size());
        for (User u : users) {
            System.out.println(u.getUsername() + " " + u.getPassword());
            if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                Random random = new Random();
                int rand = random.nextInt(10000);
                return Integer.toString(rand);
            }
        }
        return "Error";
    }



    public static void addOnline(SignUpHandler onlines) {
        Users.onlines.add(onlines);
    }


}



