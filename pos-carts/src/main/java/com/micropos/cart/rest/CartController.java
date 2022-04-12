package com.micropos.cart.rest;

import com.micropos.cart.api.CartApi;
import com.micropos.cart.api.CheckoutApi;
import com.micropos.cart.dto.ItemDto;
import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.model.Item;
import com.micropos.cart.model.Product;
import com.micropos.cart.service.CartService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cart-api")
public class CartController implements CheckoutApi, CartApi {
    private Log logger = LogFactory.getLog(CartController.class);

    private final CartMapper cartMapper;

    private final CartService cartService;

    private List<Item> cart = null;

    public void setCart(List<Item> c) {
        cart = c;
    }

    public CartController(CartService cs, CartMapper cm) {
        cartMapper = cm;
        cartService = cs;
    }

    @Autowired
    private HttpSession session;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<List<ItemDto>> checkout() {
        logger.info("checkout()");
        getCart();
        cartService.checkout(cart);
        session.setAttribute("cart", cart);
        List<ItemDto> castDto = new ArrayList<>(cartMapper.toCartDto(cart));
        return new ResponseEntity<>(castDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemDto>> listCart() {
        logger.info("listCart()");
        getCart();
        List<ItemDto> castDto = new ArrayList<>(cartMapper.toCartDto(cart));
        return new ResponseEntity<>(castDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemDto>> addProduct(
            @Parameter(name = "productId", description = "The id of the product to add to Cart", required = true, schema = @Schema(description = "")) @PathVariable("productId") String productId
    ) {
        logger.info("addProduct(String productId)" + productId);
        getCart();
        Product product = cartMapper.toProduct(cartService.getProductFromId(productId));
        cart = cartService.add(cart, product, 1);
        session.setAttribute("cart", cart);
        List<ItemDto> castDto = new ArrayList<>(cartMapper.toCartDto(cart));
        return new ResponseEntity<>(castDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemDto>> deleteProduct(
            @Parameter(name = "productId", description = "The id of the product to delete in Cart", required = true, schema = @Schema(description = "")) @PathVariable("productId") String productId
    ) {
        logger.info("deleteProduct(String productId)" + productId);
        getCart();
        cart = cartService.delete(cart, productId);
        session.setAttribute("cart", cart);
        List<ItemDto> castDto = new ArrayList<>(cartMapper.toCartDto(cart));
        return new ResponseEntity<>(castDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemDto>> modifyProduct(
            @Parameter(name = "productId", description = "The id of the product to modify in Cart", required = true, schema = @Schema(description = "")) @PathVariable("productId") String productId,
            @NotNull @Min(1) @Parameter(name = "productNum", description = "The new number of the product to modify in Cart", required = true, schema = @Schema(description = "")) @Valid @RequestParam(value = "productNum", required = true) Integer productNum
    ) {
        logger.info("modifyProduct(String productId, Integer productNum)" + productId + " " + productNum);
        getCart();
        cart = cartService.modify(cart, productId, productNum);
        session.setAttribute("cart", cart);
        List<ItemDto> castDto = new ArrayList<>(cartMapper.toCartDto(cart));
        return new ResponseEntity<>(castDto, HttpStatus.OK);
    }

    @GetMapping("/session")
    public String session() {
        logger.info(session.getId());
        return session.getId();
    }

    @SuppressWarnings("unchecked")
    private void getCart() {
        logger.info(session.getId());
        List<Item> cart1 = (List<Item>) session.getAttribute("cart");
        setCart(cart1 == null ? new ArrayList<Item>() : cart1);
    }
}
