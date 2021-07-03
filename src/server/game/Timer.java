package server.game;

public class Timer extends Thread{
    private boolean isRunning;
    public int time;
    public boolean isTurn1;
    Timer(){
        isRunning = true;
        time = 30;
        isTurn1 = true;
    }

    @Override
    public void run() {
        super.run();
        while (isRunning) {
            if (time <= 0) {
                time = 30;
                isTurn1 = !isTurn1;
            } else {
                time--;
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
