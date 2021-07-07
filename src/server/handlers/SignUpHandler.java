package server.handlers;

import com.google.gson.Gson;
import server.game.GameState;
import server.shared.SpectateResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class SignUpHandler extends Thread {
    Lobby currentLobby;
    User user;
    private String AuthToken;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public SignUpHandler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start() {
        super.start();
    }

    public void run() {
        while (true) {
            try {
                switch (dataInputStream.readUTF()) {
                    case "SIGN_IN" -> signIn();
                    case "SIGN_UP" -> signUp();
                    case "GAME" -> game();
                    case "WAIT" -> waiting();
                    case "SPECTATE" -> spectate();
                    case "PROFILE" -> profile();
                    case "LEADERBOARD" -> leaderboard();
                    case "GO" -> go();
                    case "WATCH" -> watch();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void go() throws IOException {
        if (currentLobby.isReady()) {
            dataOutputStream.writeUTF("YES");
        } else {
            dataOutputStream.writeUTF("NO");
        }
    }

    private void waiting() {
        LinkedList<Lobby> lobbies = Users.getLobbies();
        GameState gameState = new GameState();
        if (lobbies.isEmpty()) {
            lobbies.add(new Lobby(gameState));
        } else {
            if (lobbies.getLast().isFull()) {
                lobbies.add(new Lobby(gameState));
            }
        }
        Lobby lobby = lobbies.getLast();
        int c = 1;
        if (lobby.isSomeOneWaiting()) {
            c++;
        }
        if (lobby.getGameState().getPlayer1() == null) {
            lobby.getGameState().setPlayer1(this.user.getUsername());
        } else {
            lobby.getGameState().setPlayer2(this.user.getUsername());
        }
        currentLobby = lobby;
        lobby.joinMatch(this);
        Users.setLobbies(lobbies);
    }

    private void game() throws IOException {
        int c = currentLobby.getNumber();
        ClientConnection player = new ClientConnection(socket, c, currentLobby.getGameState(), user);
        player.sendToClient(Integer.toString(player.getPlayerType()));
        player.sendToClient(currentLobby.getGameState().sendBack(c));
        player.start();
        currentLobby.start();
    }

    private void leaderboard() throws IOException {
        LinkedList<User> del = Users.rank();
        int c = 0;
        if (del.size() >= 10) {
            c = 10;
            dataOutputStream.writeUTF("10");
        } else {
            c = del.size();
            dataOutputStream.writeUTF(String.valueOf(del.size()));
        }
        Gson gson = new Gson();
        for (int i = 0; i < c; i++) {
            del.get(i).setOnline(Users.searchOnline(del.get(i).getUsername()));
            dataOutputStream.writeUTF(gson.toJson(del.get(i)));

        }
    }

    private void profile() throws IOException {
        dataOutputStream.writeUTF(user.getUsername());
        dataOutputStream.writeUTF(Integer.toString(user.getWins()));
        dataOutputStream.writeUTF(Integer.toString(user.getLosses()));
        dataOutputStream.writeUTF(Integer.toString(user.getScore()));
    }

    private void spectate() throws IOException {
        LinkedList<Lobby> lobbies = Users.getLobbies();
        LinkedList<SpectateResponse> sr = new LinkedList<>();
        for (Lobby lobby : lobbies) {
            GameState gameState = lobby.getGameState();
            SpectateResponse spectateResponse = new SpectateResponse(gameState.getPlayer1(), gameState.getPlayer2(), gameState.getTurns(), gameState.getDestroyed());
            sr.add(spectateResponse);
        }
        Gson gson = new Gson();
        dataOutputStream.writeUTF(String.valueOf(sr.size()));
        for (SpectateResponse spectateResponse : sr) {
            String str = gson.toJson(spectateResponse);
            dataOutputStream.writeUTF(str);
        }
    }

    private void watch() throws IOException {
        String str = dataInputStream.readUTF();
        Lobby lobb = null;
        for (Lobby lobby : Users.getLobbies()) {
            if (lobby.getGameState().getPlayer1().equals(str) || lobby.getGameState().getPlayer2().equals(str)) {
                lobb = lobby;
            }
        }
        assert lobb != null;
        Lobby finalLobb = lobb;
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    dataOutputStream.writeUTF(finalLobb.getGameState().spectate());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void signUp() throws IOException {
        String user = dataInputStream.readUTF();
        System.out.println(user);
        String pass = dataInputStream.readUTF();
        if (Users.signUp(user, pass).equals("Signed Up")) {
            dataOutputStream.writeUTF("Signed Up");
//                        SignInHandler signInHandler = new SignInHandler(socket);
//                        signInHandler.start();
        } else {
            dataOutputStream.writeUTF("Username is already exists");
        }
    }

    private void signIn() throws IOException {
        String username = dataInputStream.readUTF();
        String pass = dataInputStream.readUTF();
        String str = Users.signIn(username,pass);
        if (str != null ) {
            if (!str.equals("Error")) {
                dataOutputStream.writeUTF("Signed In");
                dataOutputStream.writeUTF(str);
                setAuthToken(str);
                // Auth Token
                user = Users.searchUser(username);
                if (user != null) {
                    Users.addOnline(this);
                }
            } else {
                dataOutputStream.writeUTF("Username or Password is incorrect");
            }
        }else {
            dataOutputStream.writeUTF("Username or Password is incorrect");
        }
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public User getUser() {
        return user;
    }
}
