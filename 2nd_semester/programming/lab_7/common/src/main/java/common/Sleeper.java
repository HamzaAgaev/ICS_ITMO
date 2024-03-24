package common;

public class Sleeper {
    public static void tryToSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
