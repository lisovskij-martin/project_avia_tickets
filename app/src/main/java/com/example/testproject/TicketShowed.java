package com.example.testproject;

import androidx.annotation.NonNull;

public class TicketShowed {
    private String fromSTROKA;
    private String toSTROKA;
    private String idTicket;
    private String currency="rub";
    private String fromDATE;
    private String gate;
    private Double value;
    private Double wantValue;

    public void setFromSTROKA(String stroka){
        fromSTROKA=stroka;
    }

    public String getFromSTROKA(){
        return fromSTROKA;
    }

    public void setToSTROKA(String stroka){
        toSTROKA=stroka;
    }

    public String getToSTROKA(){
        return toSTROKA;
    }

    public void setFromDATE(String date){ fromDATE=date; }

    public String getFromDATE(){ return fromDATE; }

    public void setGate(String gat) { gate = gat; }

    public String getGate() {
        return gate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setValue(Double val) {
        value = val;
    }

    public Double getValue() {
        return value;
    }

    public Double getWantValue() {
        return wantValue;
    }

    public void setWantValue(Double wantValue) {
        this.wantValue = wantValue;
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TicketShowed(String id, String from, String to, String gat, String date, Double val, Double wantVal){
        setFromSTROKA(from);
        setToSTROKA(to);
        setFromDATE(date);
        setGate(gat);
        setValue(val);
        setWantValue(wantVal);
        setIdTicket(id);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder result= new StringBuilder();
        return result
                .append(this.getFromSTROKA().split(" ")[1]).append(" --> ")
                .append(this.getToSTROKA().split(" ")[1]).append("\n")
                .append(this.getFromDATE()).append("  in  ")
                .append(this.getGate()).append("\n")
                .append(Math.round(getValue())).append(" руб ( Желаемая: ")
                .append(Math.round(this.getWantValue())+" )")
                .toString();
    }
}
