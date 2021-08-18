package server.handlers;

import com.google.gson.Gson;
import server.logic.Chats;
import server.logic.Notifs;
import server.logic.Tweets;
import server.logic.Users;
import server.model.Models.*;
import server.shared.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;

public class MainHandler extends Thread {
    User user;
    Gson gson;
    int Time;
    private String AuthToken;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public MainHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.Time = 1;
        this.gson = new Gson();
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start() {
        try {
            String st = dataInputStream.readUTF();
            if (!st.equals("NULL")){
                user = gson.fromJson(st,User.class);
                Users.setProfile(user);
                Users.setCurrentUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }
    public void run() {
        while (true) {
            try {
                String a = dataInputStream.readUTF();
                System.out.println(a);
                switch (a) {
                    case EventsDirections.SIGN_IN -> signIn();
                    case EventsDirections.SIGN_UP -> signUp();
                    case "GET_USER" -> specifiedUser();
                    case "GET_CHAT" -> specifiedChat();
                    case "GET_ROOM" -> specifiedRoom();
                    case EventsDirections.NOTIF -> notif();
                    case EventsDirections.MSG -> messages();
                    case "CHATS" -> chats();
                    case "TWEETS" -> tweets();
                    case "TIME" -> time();
                    case "USERS" -> users();
                    case "DO_NOTIF" -> doNotif();
                    case "ONLINE" -> online();
                    case "TW" -> tw();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void online() throws IOException {
        dataOutputStream.writeUTF(gson.toJson(Users.getOnlines()));
    }

    private void tw() throws IOException {
        LinkedList<Tweet> profileTweet = Users.getTweets().showTweetOwnPage(Users.class, user.getUsername(),2);
        LinkedList<Tweet> exploreTweet = Users.getTweets().showTweetOwnPage(Users.class, user.getUsername(),3);
        LinkedList<Tweet> homeTweet = Users.getTweets().showTweetOwnPage(Users.class, user.getUsername(),1);
        ShowTweetResponse sr = new ShowTweetResponse(homeTweet,exploreTweet,profileTweet);
        dataOutputStream.writeUTF(gson.toJson(sr));
    }

    private void time() throws IOException {
        String a = dataInputStream.readUTF();
        Time = Integer.parseInt(a);
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
        switch (str) {
            case "GET_ROOMID" -> {
                if (Chats.getRoomID() == null) {
                    dataOutputStream.writeUTF("NULL");
                } else {
                    String id = Chats.getRoomID();
                    dataOutputStream.writeUTF(id);
                }
            }
            case "ADD_GROUP" -> {
                String gpName = dataInputStream.readUTF();
                Chats.addToGroup(user.getUsername(),gpName);
            }
            case "MAKE_CHAT" -> {
                MakeChatResponse mr = gson.fromJson(dataInputStream.readUTF(), MakeChatResponse.class);
                if (mr.isImage()) {
                    Chats.makeImageChat(mr.getText(), mr.getOwner(), mr.getRoomID(), mr.getImageUrl(),mr.isSch(),Time);
                } else {
                    Chats.makeChat(mr.getText(), mr.getOwner(), mr.getRoomID(),mr.isSch(),Time);
                }
            }
            case "LEFT" -> {
                String a = dataInputStream.readUTF();
                Chats.leftGroup(user.getUsername(),a);
//                Chats.setRoomID(null);
            }
            case "MAKE_ROOM" -> {
                String name = dataInputStream.readUTF();
                Chats.makeRoom(user.getUsername(), name);
            }
            case "HANDLE_EDIT" -> {
                String a = dataInputStream.readUTF();
                String b = dataInputStream.readUTF();
                Chats.editChat(a, b);
            }
            case "SEEN" -> {
                String a = dataInputStream.readUTF();
                String b = dataInputStream.readUTF();
                Chats.seen(a, b);
            }
            case "EDIT_ID" -> dataOutputStream.writeUTF(Chats.getEditID());
            case "DELETE_CHAT" -> {
                String id = dataInputStream.readUTF();
                Chats.deleteChat(id);
            }
            case "GET_IMAGE" -> {
                String im = Chats.getImage();
                if (im == null) {
                    dataOutputStream.writeUTF("NULL");
                } else {
                    dataOutputStream.writeUTF(im);
                }
            }
            case "EDIT_CHAT" -> {
                String id = dataInputStream.readUTF();
                Chats.setEditID(id);
            }
            case "SEARCH_GROUP" -> {
                String name = dataInputStream.readUTF();
                if (Chats.searchGroup(name) == null) {
                    dataOutputStream.writeUTF("NULL");
                } else {
                    dataOutputStream.writeUTF(Objects.requireNonNull(Chats.searchGroup(name)));
                }
            }
            case "SEARCH_ROOM" -> {
                String id = dataInputStream.readUTF();
                dataOutputStream.writeUTF(gson.toJson(Chats.searchRoomID(id)));
            }
            case "SEARCH_ROOM2" -> {
                String us = dataInputStream.readUTF();
                if (Chats.searchRoom(user.getUsername(), us) == null) {
                    dataOutputStream.writeUTF("NULL");
                } else {
                    Room room = Chats.searchRoom(user.getUsername(), us);
                    dataOutputStream.writeUTF(gson.toJson(room));
                }
            }
            case "GET_CHAT" -> {
                String id = dataInputStream.readUTF();
                dataOutputStream.writeUTF(gson.toJson(Chats.searchChat(id)));
            }
            case "IMAGE_NULL" -> Chats.setImage(null);
            case "SET_IMAGE" -> {
                String id = dataInputStream.readUTF();
                Chats.setImage(id);
            }
            case "EDIT_NULL" -> Chats.setEditID(null);
            case "SAVE" -> Chats.saveChats();
            case "NULL" -> Chats.setRoomID(null);
            default -> Chats.setRoomID(str);
        }
    }

    public void users() throws IOException {
        String str = dataInputStream.readUTF();
        if (str.equals("SAVE")) {
            Users.save();
        } else if (str.equals("DEACTIVATE")) {
            Users.deactivate(user);
        }
        else if (str.equals("LOG_OUT")){
            Users.logOut(user.getUsername());
        }
        else if (str.equals("CHANGE_PRIVATE")){
            Users.changePrivate();
        }
        else if (str.equals("SAVE_SETTINGS")){
            User us = gson.fromJson(dataInputStream.readUTF(),User.class);
            Users.setting(us);
        }
        else if (str.equals("CHANGE_PASSWORD")){
            System.out.println("lll");
            String gf = dataInputStream.readUTF();
            System.out.println(gf);
            Users.changePassword(gf);
        }
        else if (str.equals("CHANGE_LASTSEEN")){
            Users.changeLastSeen();
        }
        else if (str.equals("UPDATE_CATEGORIES")){
            UpdateCategoriesResponse up = gson.fromJson(dataInputStream.readUTF(),UpdateCategoriesResponse.class);
            Users.updateCategories(up.getFollowers(),up.getStr());
        }
        else if (str.equals("CREATE_CATEGORIES")){
            UpdateCategoriesResponse up = gson.fromJson(dataInputStream.readUTF(),UpdateCategoriesResponse.class);
            Users.createCatg(user,up.getFollowers());
        }
        else if (str.equals("DELETE")) {
            Users.deleteProfile(user);
        } else if (str.equals("FOLLOW")) {
            String s = dataInputStream.readUTF();
            System.out.println(s);
            Users.followProfile(user, s);
        } else if (str.equals("USER_NULL")) {
            Users.setCurrentUser(null);
        } else if (str.equals("PROFILE_NULL")) {
            Users.setProfile(null);
        } else if (str.equals("SET_PROFILE")) {
            System.out.println("hello");
            String f = dataInputStream.readUTF();
            System.out.println(f);
            User us = gson.fromJson(f, User.class);
            System.out.println(us.getUsername());
            Users.setProfile(us);
        } else if (str.equals("BLOCK")) {
            String owner = dataInputStream.readUTF();
            Users.blockProfile(user, owner);

        } else if (str.equals("MUTE")) {
            String owner = dataInputStream.readUTF();
            Users.muteProfile(user, owner);
        } else if (str.equals("GET_PROFILE")) {
            if (Users.getProfile() == null) {
                dataOutputStream.writeUTF("NULL");
            } else {
                dataOutputStream.writeUTF(gson.toJson(Users.getProfile()));
            }
        } else if (str.equals("GET_COMMENTS")) {
            String ID = dataInputStream.readUTF();
            GetTweetsResponse gs = new GetTweetsResponse(Users.getTweets().getComments(ID));
            dataOutputStream.writeUTF(gson.toJson(gs));
        } else if (str.equals("SHOW_TWEET")) {
            String type = dataInputStream.readUTF();
            if (type.equals("2")) {
                System.out.println(Users.getProfile().getUsername());
                GetTweetsResponse gs = new GetTweetsResponse(Users.getTweets().showTweetOwnPage(Users.class, Users.getProfile().getUsername(), 2));
                dataOutputStream.writeUTF(gson.toJson(gs));
            } else {
                GetTweetsResponse gs = new GetTweetsResponse(Users.getTweets().showTweetOwnPage(Users.class, user.getUsername(), Integer.parseInt(type)));
                dataOutputStream.writeUTF(gson.toJson(gs));
            }
        }
    }

    public void tweets() throws IOException {
        String str = dataInputStream.readUTF();
        if (str.equals("SET_IMAGE")) {
            String path = dataInputStream.readUTF();
            Tweets.setImage(path);
        } else if (str.equals("TWEET_NULL")) {
            Tweets.setTweetID(null);
        } else if (str.equals("LIKE")) {
            String id = dataInputStream.readUTF();
            Tweets.likeTweet(user, Tweets.search(id));
        } else if (str.equals("RETWEET")) {
            String id = dataInputStream.readUTF();
            Tweets.reTweet(Tweets.search(id), user);
        } else if (str.equals("REPORT")) {
            String owner = dataInputStream.readUTF();
            Tweets.reportUser(user, owner);
        } else if (str.equals("COMMENT_ID")) {
            dataOutputStream.writeUTF(Tweets.getComment());
        } else if (str.equals("MAKE_TWEET")) {
            MakeTweetResponse mr = gson.fromJson(dataInputStream.readUTF(), MakeTweetResponse.class);
            Tweets.makeTweet(mr.getText(), mr.getComment(), mr.getOwner(), mr.getFollowers());
        } else if (str.equals("SET_TWEET_ID")) {
            Tweets.setTweetID(dataInputStream.readUTF());
        }
        // If eligible for hyperlink for privacy of the user
        else if (str.equals("ELIGIBLE")){
            String ID = dataInputStream.readUTF();
            if (Tweets.search(ID) == null){
                dataOutputStream.writeUTF("NO");
            } else {
                Tweet tw = Tweets.search(ID);
                assert tw != null;
                if (Users.searchUsername(tw.getOwner()).isPrivate()){
                    if (tw.getUsers().contains(user)){
                        dataOutputStream.writeUTF("YES");
                    } else {
                        dataOutputStream.writeUTF("NO");
                    }
                } else {
                    dataOutputStream.writeUTF("YES");
                }
            }
        }
        else if (str.equals("SET_COMMENT_ID")) {
            Tweets.setComment(dataInputStream.readUTF());
        } else if (str.equals("SET_FORWARD_ID")) {
            String ID = dataInputStream.readUTF();
            Tweets.setForwardTweetID(ID);
        } else if (str.equals("GET_FORWARD_ID")) {
            if (Tweets.getForwardTweetID() == null) {
                dataOutputStream.writeUTF("NULL");
            } else {
                dataOutputStream.writeUTF(Tweets.getForwardTweetID());
            }
        } else if (str.equals("SEARCH_TWEET")) {
            String ID = dataInputStream.readUTF();
            Tweet tw = Tweets.search(ID);
            dataOutputStream.writeUTF(gson.toJson(tw));
        } else if (str.equals("GET_IMAGE")) {
            if (Tweets.getImage() == null) {
                dataOutputStream.writeUTF("NULL");
            } else {
                dataOutputStream.writeUTF((Tweets.getImage()));
            }
        } else if (str.equals("GET_COMMENT")) {
            dataOutputStream.writeUTF(Tweets.getComment());
        } else if (str.equals("GET_TWEET_ID")) {
            if (Tweets.getTweetID() == null) {
                dataOutputStream.writeUTF("NULL");
            } else {
                dataOutputStream.writeUTF(Tweets.getTweetID());
            }
        }
    }

    private void doNotif() throws IOException {
        Notif notif = gson.fromJson(dataInputStream.readUTF(), Notif.class);
        int type = Integer.parseInt(dataInputStream.readUTF());
        Notifs.acceptOrDeclineReq(notif, type);
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
                String strs = dataInputStream.readUTF();
                LinkedList<Chat> chats = Users.getChats().showChats(strs);
                ShowChatResponse sr = new ShowChatResponse(chats);
                dataOutputStream.writeUTF(gson.toJson(sr));
            }

        }
    }

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
            Users.setProfile(user);
            Users.setCurrentUser(user);
            Users.addToOnline(user.getUsername());
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
