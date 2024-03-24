package beans;

import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;

@ManagedBean(name = "currentResult")
@SessionScoped
public class CurrentResult extends Result{
    public CurrentResult() {
        this.setX(0);
        this.setY(0);
        this.setR(1);
    }
}
