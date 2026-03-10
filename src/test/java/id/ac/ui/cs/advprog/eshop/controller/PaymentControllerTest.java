package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private OrderService orderService;

    private Order dummyOrder;

    private static final String PAY_ID = "pay-1";

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        p.setProductId("1");
        p.setProductName("Sabun");
        p.setProductQuantity(2);
        products.add(p);
        dummyOrder = new Order("ord-1", products, 1L, "Haikal");
    }

    @Test
    void testPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailForm"));
    }

    @Test
    void testPaymentDetail() throws Exception {
        Payment payment = new Payment(PAY_ID, "BANK_TRANSFER", new HashMap<>(){{put("bankName","BCA"); put("referenceCode","123");}}, dummyOrder);
        when(paymentService.getPayment(PAY_ID)).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/" + PAY_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentAdminList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testPaymentAdminDetail() throws Exception {
        Payment payment = new Payment(PAY_ID, "BANK_TRANSFER", new HashMap<>(){{put("bankName","BCA"); put("referenceCode","123");}}, dummyOrder);
        when(paymentService.getPayment(PAY_ID)).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/" + PAY_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentAdminSetStatus() throws Exception {
        Payment payment = new Payment(PAY_ID, "BANK_TRANSFER", new HashMap<>(){{put("bankName","BCA"); put("referenceCode","123");}}, dummyOrder);
        when(paymentService.getPayment(PAY_ID)).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/" + PAY_ID)
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));

        verify(paymentService, times(1)).setStatus(payment, "SUCCESS");
    }
}