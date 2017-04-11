import java.io.Serializable;

/**
 * Created by Andre on 27-03-2017.
 */
public class PDU implements Serializable{
    int seq;
    TipoPDU tipo;

    PDU(int s,TipoPDU t){
        seq=s;
        tipo=t;
    }
}
