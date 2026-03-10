package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ProductService productService;

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    void testCreateOrderPost() throws Exception {
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        p.setProductId("1");
        p.setProductName("Test");
        p.setProductQuantity(1);
        products.add(p);

        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(post("/order/create")
                        .param("author", "Bambang"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));

        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    void testOrderHistoryPost() throws Exception {
        List<Order> orders = new ArrayList<>();
        when(orderService.findAllByAuthor("Bambang")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Bambang"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderList"))
                .andExpect(model().attributeExists("orders"));
    }
}