package common.netentities;

import java.io.Serializable;

public class ResponseEntity<T extends Serializable> implements Serializable {
    private T value;

    public ResponseEntity() {}
    public ResponseEntity(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
