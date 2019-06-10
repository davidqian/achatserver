package com.achatserver.seqid.pojo;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seqidSegment")
public class SeqIdSegment {

    @Id
    @GeneratedValue
    private long seqId;
    private BigDecimal maxSeqId;
    private BigDecimal step;
    private BigDecimal start;
    private BigDecimal end;

    public BigDecimal getEnd() {
        return end;
    }

    public void setEnd(BigDecimal end) {
        this.end = end;
    }

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getMaxSeqId() {
        return maxSeqId;
    }

    public void setMaxSeqId(BigDecimal maxSeqId) {
        this.maxSeqId = maxSeqId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }
}