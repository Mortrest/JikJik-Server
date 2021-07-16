package server.shared;

import server.model.Models.Room;

import java.util.LinkedList;

public class RoomResponse {
    LinkedList<Room> rooms;

    public RoomResponse(LinkedList<Room> rooms){
        this.rooms = rooms;
    }

    public LinkedList<Room> getRooms() {
        return rooms;
    }
}
