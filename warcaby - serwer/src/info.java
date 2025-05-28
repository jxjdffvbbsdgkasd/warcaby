import java.io.Serializable;

public class info implements Serializable {
    public Field[][] fields;
    // za duzo jebania z wysylaniem pojedynczego ruchu, bo sa jeszcze te wieloktrotne
    // i to trzeba wysylac w petli i aktualizowac w petli a tak to pyk raz wyslane i 1 repaint
    double time;
    int status;
    info(Field[][] f, double time, int status) {
        this.fields =f ;
        this.time = time;
        this.status = status;
    }
}