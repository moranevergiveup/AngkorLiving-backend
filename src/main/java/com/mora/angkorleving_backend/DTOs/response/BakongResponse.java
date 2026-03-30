//package com.mora.angkorleving_backend.DTOs.response;
//
//
//public class BakongResponse {
//    private String qr;
//    private String md5;
//    private String status;
//    private String transactionId;
//    private Double amount;
//    private String currency;
//    private String timestamp;
//
//    // getters and setters
//    public String getMd5() { return md5; }
//    public void setMd5(String md5) { this.md5 = md5; }
//
//    public String getQr() { return qr;}
//    public void setQr(String qr) { this.qr = qr; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getTransactionId() { return transactionId; }
//    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
//
//    public Double getAmount() { return amount; }
//    public void setAmount(Double amount) { this.amount = amount; }
//
//    public String getCurrency() { return currency; }
//    public void setCurrency(String currency) { this.currency = currency; }
//
//    public String getTimestamp() { return timestamp; }
//    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
//}
//
package com.mora.angkorleving_backend.DTOs.response;

import lombok.Data;

@Data
public class BakongResponse {
    private String md5;
    private String status;          // PENDING, PAID, FAILED
    private String transactionId;   // hash from API
    private Double amount;
    private String currency;
    private String timestamp;       // acknowledgedDateMs or createdDateMs
    private String qr;
    private String message;         // responseMessage from API
}