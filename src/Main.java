import server.handlers.Server;
import server.logic.Users;
import server.util.ModelLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ModelLoader m = new ModelLoader();
        Users users = new Users(m);
        Server server = new Server();
        server.start();
    }
}
