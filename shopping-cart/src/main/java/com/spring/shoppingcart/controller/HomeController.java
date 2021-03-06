package com.spring.shoppingcart.controller;

import com.spring.shoppingcart.model.Cart;
import com.spring.shoppingcart.model.OrderLine;
import com.spring.shoppingcart.model.Product;
import com.spring.shoppingcart.model.SendMesage;
import com.spring.shoppingcart.repository.ProductRepository;
import com.spring.shoppingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    JavaMailSender sender;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String home(HttpSession session,Model model){
        model.addAttribute("products", productRepository.getAll());
        model.addAttribute("cartCount", cartService.countItemInCart(session));
        return "home-main";
    }
    @GetMapping("/buy/{id}")
    public String buy(@PathVariable(name = "id") Long id, HttpSession session, Model model) {
        cartService.addToCart(id, session);
        return "redirect:/";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("mesage", new SendMesage());
        return "checkout";
    }


    //add count
    @GetMapping("addcount/{id}")
    public String addcount(HttpSession session, @PathVariable("id") long id){
        Optional<Product> product = productRepository.findById(id);
        Cart cart = cartService.getCart(session);
        cart.update(product.get());
        cartService.addToCart(id, session);
        return "redirect:/checkout";
        
    }

    //removecount
    @GetMapping("removecount/{id}")
    public String remove(@PathVariable("id") long id, HttpSession session){
        Optional<Product> product = productRepository.findById(id);
        Cart cart = cartService.getCart(session);
        cart.removecount(product.get());
        cartService.addToCart(id, session);
        return "redirect:/checkout";
    }

    //remove
    @PostMapping("remove")
    public String remove(HttpSession session, @RequestParam("id") long id){
        HashMap<Long, Cart> cartItems = (HashMap<Long, Cart>) session.getAttribute("CART");
//        cartItems.size();
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        if (cartItems.containsKey(id)) {
            cartItems.remove(id);
        }
//        cartItems.size();
        session.setAttribute("CART", cartItems);
        return "redirect:/checkout";
    }

    @PostMapping("email")
    public String send(@ModelAttribute SendMesage mesagerq ){
        String from = "duyqt5695@gmail.com";
        String to = mesagerq.getEmail();
        String mes = "Xin Ch??o " + mesagerq.getName() + " ch??ng t??i ???? nh???n ???????c ????n ?????t h??ng c???a b???n.Ch??ng t??i s??? li??n h??? t???i s??? ??i???n tho???i "
                + mesagerq.getPhone() + " b???n ???? cung c???p ????? x??c minh ????n h??ng. Xin c??m ??n!";
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject("DuyNguyen Shop");
        message.setText(mes);
        sender.send(message);

        return "sendmesage";
    }
}
