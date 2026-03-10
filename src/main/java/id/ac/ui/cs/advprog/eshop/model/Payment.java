package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    public static final String METHOD_VOUCHER = "VOUCHER";
    public static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_REJECTED = "REJECTED";

    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int VOUCHER_LENGTH = 16;
    private static final int VOUCHER_NUM_DIGITS = 8;

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

        if (METHOD_VOUCHER.equals(method)) {
            processVoucher();
        } else if (METHOD_BANK_TRANSFER.equals(method)) {
            processBankTransfer();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void processVoucher() {
        String voucherCode = paymentData.get("voucherCode");
        boolean isValid = false;

        if (voucherCode != null && voucherCode.length() == VOUCHER_LENGTH && voucherCode.startsWith(VOUCHER_PREFIX)) {
            long digitCount = voucherCode.chars().filter(Character::isDigit).count();
            if (digitCount == VOUCHER_NUM_DIGITS) {
                isValid = true;
            }
        }
        this.status = isValid ? STATUS_SUCCESS : STATUS_REJECTED;
    }

    private void processBankTransfer() {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        if (bankName != null && !bankName.trim().isEmpty() &&
                referenceCode != null && !referenceCode.trim().isEmpty()) {
            this.status = STATUS_SUCCESS;
        } else {
            this.status = STATUS_REJECTED;
        }
    }

    public void setStatus(String status) {
        if (STATUS_SUCCESS.equals(status) || STATUS_REJECTED.equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}