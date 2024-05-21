package ru.ad.web_shop.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.ad.web_shop.entities.Product;
import ru.ad.web_shop.entities.ShoppingCart;
import ru.ad.web_shop.models.ProductDto;
import ru.ad.web_shop.services.ProductService;

import java.util.List;

import static ru.ad.web_shop.utils.TestLogger.log;

@Controller  //💀
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public String index(
            @RequestParam(name = "brand_id", required = false) Integer brandId,
            @RequestParam(name = "category_id", required = false) Integer categoryId,
            Model model
    ){
        List<Product> products;
        if (categoryId != null) {
            products = service.getByCategoryId(categoryId, model);
        } else if (brandId != null) {
            products = service.getByBrandId(brandId, model);
        } else {
            products = service.getAll();
        }
        model.addAttribute("products", products.stream().map(this::convert).toList());
        return "products/index";
    }

    private ProductDto convert(Product product) {
        return service.convert(product);
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, SessionStatus status) {
        ShoppingCart cart = getCartFromSession(session);
        cart.clearCart();
        status.setComplete();
        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        ShoppingCart cart = getCartFromSession(session);
        model.addAttribute("cart", cart);
        return "products/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        ShoppingCart cart = getCartFromSession(session);

        ProductDto product = service.convert(service.getById(productId));
        cart.addProduct(product);
        log(cart);

        setCartToSession(session, cart);
        return "redirect:/products";
    }

    @DeleteMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("productId") int productId, HttpSession session) {
        ShoppingCart cart = getCartFromSession(session);
        cart.removeProduct(productId);
        setCartToSession(session, cart);
        return "redirect:/products/cart";
    }

    private ShoppingCart getCartFromSession(HttpSession session) {
        if (session.getAttribute("cart") == null) {
            var cart = new ShoppingCart();
//            service.getAll().stream().map(this::convert).limit(5).forEach(cart::addProduct);
//            session.setAttribute("cart", cart);
            session.setAttribute("cart", new ShoppingCart());

        }
        return (ShoppingCart) session.getAttribute("cart");
    }

    private void setCartToSession(HttpSession session, ShoppingCart cart) {
        session.setAttribute("cart", cart);
    }
}
