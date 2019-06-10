package com.achatserver.seqid.pojo;

import java.math.BigDecimal;

public class SeqIdRelation {

    private long seqId;
    private BigDecimal maxSeqId;
    private BigDecimal beginSeqId;
    private BigDecimal step;
    private BigDecimal start;
    private BigDecimal end;


    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public BigDecimal getMaxSeqId() {
        return maxSeqId;
    }

    public void setMaxSeqId(BigDecimal maxSeqId) {
        this.maxSeqId = maxSeqId;
    }

    public BigDecimal getBeginSeqId() {
        return beginSeqId;
    }

    public void setBeginSeqId(BigDecimal beginSeqId) {
        this.beginSeqId = beginSeqId;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getEnd() {
        return end;
    }

    public void setEnd(BigDecimal end) {
        this.end = end;
    }
}
