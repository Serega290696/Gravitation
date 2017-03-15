package being.elements;

public class Spacetime {
    private long absoluteTime;
    private boolean stopUniverse;

    private final Object stopMonitor = new Object();

    public void bigBang() {
        System.out.println("Spacetime.bigBang");
        absoluteTime = 0;
    }

    public boolean isStopUniverse() {
        return stopUniverse;
    }

    public void resume() {
        stopUniverse = false;
        synchronized (stopMonitor) {
            stopMonitor.notifyAll();
        }
    }

    public void stop() {
        stopUniverse = true;
    }
}
