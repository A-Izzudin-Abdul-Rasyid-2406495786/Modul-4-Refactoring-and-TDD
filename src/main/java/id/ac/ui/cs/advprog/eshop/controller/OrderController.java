package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId, @RequestParam String method, @RequestParam java.util.Map<String, String> allParams, Model model) {
        Order order = orderService.findById(orderId);
        Payment payment = paymentService.addPayment(order, method, allParams);
        model.addAttribute("paymentId", payment.getId());
        return "OrderPaySuccess";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "OrderPay";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam String author) {
        List<Product> products = productService.findAll();

        if (products.isEmpty()) {
            Product dummy = new Product();
            dummy.setProductId(UUID.randomUUID().toString());
            dummy.setProductName("Dummy Product");
            dummy.setProductQuantity(1);
            productService.create(dummy);
            products.add(dummy);
        }

        Order order = new Order(UUID.randomUUID().toString(), products, System.currentTimeMillis(), author);
        orderService.createOrder(order);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String historyPost(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderList";
    }
}