package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String LIST = "list";
    private static final String PRODUCT_ATTR = "product";
    private static final String URL_CREATE = "/product/create";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get(URL_CREATE))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists(PRODUCT_ATTR));
    }

    @Test
    void testCreateProductPost() throws Exception {
        mockMvc.perform(post(URL_CREATE)
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST));
        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    void testCreateProductPostWithEmptyId() throws Exception {
        product.setProductId("");
        mockMvc.perform(post(URL_CREATE)
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST));
        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    void testProductListPage() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList(product));
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testEditProductPage() throws Exception {
        when(productService.findById(product.getProductId())).thenReturn(product);
        mockMvc.perform(get("/product/edit/" + product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists(PRODUCT_ATTR));
    }

    @Test
    void testEditProductPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST));
        verify(productService, times(1)).update(eq(product.getProductId()), any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/product/delete/" + product.getProductId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST));
        verify(productService, times(1)).delete(product.getProductId());
    }

    @Test
    void testCreateProductPostWithNullId() throws Exception {
        product.setProductId(null);
        mockMvc.perform(post(URL_CREATE)
                        .flashAttr(PRODUCT_ATTR, product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LIST));
        verify(productService, times(1)).create(any(Product.class));
    }
}