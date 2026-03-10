package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.*;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sabun");
        product.setProductQuantity(2);
        products.add(product);
        order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");

        // Menggunakan class Payment biasa dengan menambahkan argumen "BANK_TRANSFER"
        payment = new Payment("pay-1", "BANK_TRANSFER", paymentData, order);
    }

    @Test
    void testAddPaymentBankTransfer() {
        doReturn(payment).when(paymentRepository).save(any(Payment.class));
        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", payment.getPaymentData());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testSetStatusSuccessUpdatesOrderStatus() {
        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", result.getOrder().getStatus());
    }

    @Test
    void testSetStatusRejectedUpdatesOrderStatus() {
        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", result.getOrder().getStatus());
    }

    @Test
    void testSetStatusPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("invalid-id");

        Payment invalidPayment = new Payment("invalid-id", "BANK_TRANSFER", payment.getPaymentData(), order);

        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(invalidPayment, "SUCCESS"));
    }

    @Test
    void testGetPayment() {
        doReturn(payment).when(paymentRepository).findById(payment.getId());
        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("zczc");
        assertNull(paymentService.getPayment("zczc"));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment);
        doReturn(paymentList).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
        assertEquals(payment.getId(), result.get(0).getId());
    }
}