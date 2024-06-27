package util;

public class GameThread extends Thread{
    private boolean paused = false;
    private Logic logic;
    private int fps;
    private boolean running = true;
    public GameThread(Logic logic, int fps) {
        this.logic = logic;
        this.fps = fps;
    }
    public void run()
    {
        gameLoop();
        /*while (true) {
            while (!paused) {
                gameLoop();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void pause() {
        paused = true;
    }

    public void unPause() {
        paused = false;
    }

    public void stopRunning() {
        running = false;
    }
    
    private void gameLoop() {
        double hertz = (double) fps;
        double timeBetweenUpdates = 1000000000 / hertz;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        double targetFps = fps;
        double targetTimeBetweenUpdates = 1000000000 / targetFps;
        int maxUpdatesBeforeRender = 1;

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!paused) {
                while (now - lastUpdateTime > timeBetweenUpdates && updateCount < maxUpdatesBeforeRender) {
                    logic.tick();
                    lastUpdateTime += timeBetweenUpdates;
                    updateCount++;
                }
            
                if (now - lastUpdateTime > timeBetweenUpdates) {
                    lastUpdateTime = now - timeBetweenUpdates;
                }

                while (now - lastRenderTime < targetTimeBetweenUpdates && now - lastUpdateTime < timeBetweenUpdates) {
                    Thread.yield();
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {}
                    now = System.nanoTime();
                }
            }
        }


        logic.tick();
        try {
            Thread.sleep(1000/fps);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
