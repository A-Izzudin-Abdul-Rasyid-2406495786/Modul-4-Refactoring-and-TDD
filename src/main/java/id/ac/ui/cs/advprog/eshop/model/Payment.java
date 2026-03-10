package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this.id = id;
        this.method = method;
        this.order = order;

        if (paymentData == null || paymentData.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }

        // Pengecekan metode pembayaran langsung di dalam constructor
        if ("VOUCHER".equals(method)) {
            processVoucher();
        } else if ("BANK_TRANSFER".equals(method)) {
            processBankTransfer();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void processVoucher() {
        String voucherCode = paymentData.get("voucherCode");
        boolean isValid = false;

        if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
            long digitCount = voucherCode.chars().filter(Character::isDigit).count();
            if (digitCount == 8) {
                isValid = true;
            }
        }
        this.setStatus(isValid ? "SUCCESS" : "REJECTED");
    }

    private void processBankTransfer() {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        if (bankName != null && !bankName.trim().isEmpty() &&
                referenceCode != null && !referenceCode.trim().isEmpty()) {
            this.setStatus("SUCCESS");
        } else {
            this.setStatus("REJECTED");
        }
    }

    public void setStatus(String status) {
        if ("SUCCESS".equals(status) || "REJECTED".equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}