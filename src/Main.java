import server.handlers.Server;
import server.logic.Chats;
import server.logic.Notifs;
import server.logic.Tweets;
import server.logic.Users;
import server.util.ModelLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ModelLoader m = new ModelLoader();
        Tweets tweets = new Tweets(m);
        Notifs notifs = new Notifs(m);
        Chats chats = new Chats(m);
        Users users = new Users(m,tweets,chats,notifs);
        Server server = new Server();
        server.start();
    }
}
