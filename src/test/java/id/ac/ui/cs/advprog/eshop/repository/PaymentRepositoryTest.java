package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Kecap");
        product.setProductQuantity(2);
        products.add(product);
        Order order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");

        payments = new ArrayList<>();
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        payments.add(new Payment("pay-1", "VOUCHER", paymentData1, order));

        // Boleh ditambahkan juga test data untuk bank transfer kalau mau
        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "REF12345");
        payments.add(new Payment("pay-2", "BANK_TRANSFER", paymentData2, order));
    }

    @Test
    void testSaveCreateAndFindById() {
        Payment payment = payments.get(0);
        paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), findResult.getId());
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payments.get(0));
        assertEquals(1, paymentRepository.findAll().size());
    }
}