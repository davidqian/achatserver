package com.achatserver.uid.pojo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "idSegment")
public class IdSegment {

    @Id
    private String type;

    private BigDecimal maxId;

    private BigDecimal step;

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }

    public BigDecimal getMaxId() {
        return maxId;
    }

    public void setMaxId(BigDecimal maxId) {
        this.maxId = maxId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
