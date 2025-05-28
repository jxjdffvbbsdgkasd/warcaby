import java.io.Serializable;

public class info implements Serializable {
    public Field[][] fields;
    double time;
    int status;
    info(Field[][]f, double time, int status) {
        this.fields = f;
        this.time = time;
        this.status = status;
    }
}