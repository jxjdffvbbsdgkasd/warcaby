package warcaby;
import java.io.Serializable;

public class info implements Serializable {
    private static final long serialVersionUID = 1L;
    public Field[][] fields;
    public double time;
    public int status;
    public info(Field[][] f, double time, int status) {
        this.fields =f ;
        this.time = time;
        this.status = status;
    }
}