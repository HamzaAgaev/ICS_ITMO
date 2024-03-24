package common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteManager {
    private static final int intSize = 4;
    private static final int bufferSize = 128; //4096

    public ByteManager() {}

    public static int getBufferSize() {
        return bufferSize;
    }

    public static int getIntSize() {
        return intSize;
    }

    public byte[][] divideToByteArrays(byte[] array) {
        int dataBytesCount = bufferSize - intSize;
        int lengthResidue = array.length % dataBytesCount > 0 ? 1 : 0;
        int resultLength = array.length / dataBytesCount + lengthResidue;
        int resultWidth = bufferSize;
        int notCopiedCount = array.length;
        byte[][] result = new byte[resultLength][resultWidth];
        for (int i = 0; i < resultLength; i++) {
            byte[] arrayNumber = intToByteArray(i);
            System.arraycopy(arrayNumber, 0, result[i], 0, intSize);
            int countToCopy;
            if (notCopiedCount >= dataBytesCount) {
                countToCopy = dataBytesCount;
            } else {
                countToCopy = notCopiedCount;
            }
            notCopiedCount -= countToCopy;
            System.arraycopy(array, i * dataBytesCount, result[i], intSize, countToCopy);
        }
        return result;
    }

    public byte[] joinToByteArray(byte[][] dividedByteArray) {
        int dataBytesCount = bufferSize - intSize;
        int resultLength = dividedByteArray.length * dataBytesCount;
        byte[] result = new byte[resultLength];
        for (int i = 0; i < dividedByteArray.length; i++) {
            System.arraycopy(dividedByteArray[i], intSize, result, i*dataBytesCount, dataBytesCount);
        }
        return result;
    }

    public int byteArrayToInt(byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        return byteBuffer.getInt();
    }

    public byte[] intToByteArray(int toByte) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(intSize);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(toByte);
        return byteBuffer.array();
    }

    public boolean containsOnlyZeroesFrom(byte[] array, int fromIndex) {
        if (fromIndex >= array.length) {
            return false;
        }

        for (int i = fromIndex; i < array.length; i++) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
}
