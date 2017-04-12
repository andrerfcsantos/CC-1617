package Utils;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by Andre on 27-03-2017.
 */
public class PDU implements Serializable{
    private int seq;
    private TipoPDU tipo;
    private Instant instant;

    public PDU(){
        seq=0;
        tipo = TipoPDU.DISPONIVEL;
        instant = Instant.now();
    }

    public PDU(int s, TipoPDU t){
        seq=s;
        tipo=t;
        instant = Instant.now();
    }

    public PDU(int s, TipoPDU t, Instant inst){
        seq=s;
        tipo=t;
        instant = inst;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public TipoPDU getTipo() {
        return tipo;
    }

    public void setTipo(TipoPDU tipo) {
        this.tipo = tipo;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }


}
