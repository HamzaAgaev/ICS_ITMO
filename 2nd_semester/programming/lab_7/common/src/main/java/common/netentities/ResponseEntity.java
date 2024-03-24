package common.netentities;

import common.data.LoginStatus;

import java.io.Serializable;

public class ResponseEntity<T extends Serializable> implements Serializable {
    private T value;
    private LoginStatus loginStatus;

    public ResponseEntity() {}
    public ResponseEntity(T value) {
        this.value = value;
        this.loginStatus = LoginStatus.SUCCESS;
    }

    public ResponseEntity(T value, LoginStatus loginStatus) {
        this.value = value;
        this.loginStatus = loginStatus;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }
}
