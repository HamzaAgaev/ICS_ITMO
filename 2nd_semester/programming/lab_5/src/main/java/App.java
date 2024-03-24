import collection.CollectionManager;
import utils.ScriptScanner;

public class App {
    public static void main(String[] args) {
        try {
            String collectionFilename = args[0];
            if (CollectionManager.getInstance().tryToSetCollectionFilename(collectionFilename).getValue()) {
                ScriptScanner scriptScanner = new ScriptScanner();
                scriptScanner.run();
            } else {
                System.out.println("Collection File " + collectionFilename + " doesn't exist.");
            }
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            System.out.println("Collection Filename is empty.");
        }
    }
}
