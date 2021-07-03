package server.shared;

public class Event {
    String AuthToken;
    int type;
    int[] xoy;

    Event(int[] xoy){
        this.xoy = xoy;
    }


    public int[] getClickedPosition(){
        return xoy;
    }

}
