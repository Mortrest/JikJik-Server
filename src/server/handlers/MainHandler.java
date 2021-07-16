package server.handlers;

import com.google.gson.Gson;
import server.logic.Chats;
import server.logic.Notifs;
import server.logic.Users;
import server.model.Models.Chat;
import server.model.Models.Notif;
import server.model.Models.Room;
import server.shared.*;
import server.model.Models.User;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class MainHandler extends Thread {
    User user;
    Gson gson;
    private String AuthToken;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public MainHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.gson = new Gson();
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
                    case EventsDirections.SIGN_IN -> signIn();
                    case EventsDirections.SIGN_UP -> signUp();
//                    case EventsDirections.HOME -> home();
                    case "GET_USER" -> specifiedUser();
                    case "GET_CHAT" -> specifiedChat();
                    case "GET_ROOM" -> specifiedRoom();
                    case EventsDirections.EXPLORE -> explore();
                    case EventsDirections.NOTIF -> notif();
                    case EventsDirections.MSG -> messages();
                    case EventsDirections.PROFILE -> profile();
                    case "CHATS" -> chats();
                    case "DO_NOTIF" -> doNotif();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void specifiedUser() throws IOException {
        String username = dataInputStream.readUTF();
        User u = Users.searchUsername(username);
        dataOutputStream.writeUTF(gson.toJson(u));
    }


    private void specifiedRoom() throws IOException {
        String username = dataInputStream.readUTF();
        Room u = Chats.searchRoomID(username);
        dataOutputStream.writeUTF(gson.toJson(u));

    }

    private void specifiedChat() throws IOException {
        String username = dataInputStream.readUTF();
        Chat u = Chats.searchChat(username);
        dataOutputStream.writeUTF(gson.toJson(u));

    }

    private void chats() throws IOException {
        String str = dataInputStream.readUTF();
        if (str.equals("SAVE")) {
            Chats.saveChats();
        }
        if (str.equals("NULL")) {
            Chats.setRoomID(null);
        } else {
            Chats.setRoomID(str);
        }
    }

    private void doNotif() throws IOException {
        Notif notif = gson.fromJson(dataInputStream.readUTF(), Notif.class);
        int type = Integer.parseInt(dataInputStream.readUTF());
        Notifs.acceptOrDeclineReq(notif, type);
    }


    private void profile() {

    }


    private void notif() throws IOException {
        LinkedList<Notif> notifs = Users.getNotifs().showNotif(user.getUsername());
        NotifResponse notifResponse = new NotifResponse(notifs);
        dataOutputStream.writeUTF(gson.toJson(notifResponse));

    }


    private void messages() throws IOException {
        String str = dataInputStream.readUTF();
        switch (str) {
            case "SPEC" -> {
                String st = dataInputStream.readUTF();
                if (Chats.searchRoomID(Chats.searchGroup(st)) == null) {
                    dataOutputStream.writeUTF("NULL");
                } else {
                    Room room = Chats.searchRoomID(Chats.searchGroup(st));
                    dataOutputStream.writeUTF(gson.toJson(room));
                }
            }
            case "CREATE_GROUP" -> {
                String strs = dataInputStream.readUTF();
                CreateGroupResponse cr = gson.fromJson(strs, CreateGroupResponse.class);
                Chats.creatGroupRoom(cr.getMembers(), cr.getGroupName());
            }
            case "DATA" -> {
                LinkedList<Room> rooms = Chats.userRoom(user.getUsername());
                RoomResponse rs = new RoomResponse(rooms);
                dataOutputStream.writeUTF(gson.toJson(rs));
            }
            case "GET_CHATS" -> {
                System.out.println("\n\n\n\n\naalwkdmawldkmawdlkawm");
                String strs = dataInputStream.readUTF();
                System.out.println(strs);
                LinkedList<Chat> chats = Users.getChats().showChats(strs);
                System.out.println(chats.size());
                ShowChatResponse sr = new ShowChatResponse(chats);
                dataOutputStream.writeUTF(gson.toJson(sr));
            }
        }
    }


    private void explore() {
    }

//    private void home() throws IOException {
//
//        while (isRunning) {
//            String str = null;
//            try {
//                str = (dataInputStream.readUTF());
//            } catch (IOException ignored) {
//            }
//            if (str != null) {
//                if (str.charAt(0) == (mainEvent)) {
//                    try {
//
//                        dataOutputStream.writeUTF(gameState.timeBack());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else if (str.equals(reqEvent)) {
//                    gameState.changeBoard(playerType);
//                    try {
//                        dataOutputStream.writeUTF(gameState.sendBack(playerType));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    recentEvent = (makeEvent(str));
//                    gameState.changeMatrix(playerType, recentEvent.getClickedPosition());
//                    try {
//                        dataOutputStream.writeUTF(gameState.sendBack(playerType));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//
//        Thread thread = new Thread(() -> {
//            while (true) {
//                String str = null;
//                try {
//                    str = (dataInputStream.readUTF());
//                } catch (IOException ignored) {
//                }
//                if (str != null) {
//                    if (str.equals("DATA")) {
//
//                        dataOutputStream.writeUTF();
//                    }
//                }
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//
//    }

    private void signUp() throws IOException {
        SignUpResponse res = gson.fromJson(dataInputStream.readUTF(), SignUpResponse.class);
        if (Users.signUp(res.getUsername(), res.getfName(), res.getlName(), res.getEmail(), res.getEmail()).equals("Success")) {
            dataOutputStream.writeUTF("Signed Up");
        } else {
            dataOutputStream.writeUTF("Username is already exists");
        }
    }

    private void signIn() throws IOException {
        SignInResponse res = gson.fromJson(dataInputStream.readUTF(), SignInResponse.class);
        boolean str = Users.signIn(res.getUsername(), res.getPassword());
        if (str) {
            this.user = Users.searchUsername(res.getUsername());
            assert user != null;
            String auth = Users.authMaker(user);
            dataOutputStream.writeUTF("Signed In");
            System.out.println("signed In");
            dataOutputStream.writeUTF(auth);
            dataOutputStream.writeUTF(gson.toJson(user));
            // Auth Token
//            if (user != null) {
//                Users.addOnline(this);
//            }
            // Solve onlines
        } else {
            dataOutputStream.writeUTF("Username or Password is incorrect");
        }
    }

    public User getUser() {
        return user;
    }
}
