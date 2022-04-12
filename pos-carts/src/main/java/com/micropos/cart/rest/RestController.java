package com.micropos.cart.rest;

import com.micropos.cart.mapper.CartMapper;
import com.micropos.cart.service.CartService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("cart-api")
public class RestController {
    private Log logger = LogFactory.getLog(RestController.class);

    private final CartMapper cartMapper;

    private final CartService cartService;

    public RestController(CartService cs, CartMapper cm) {
        cartMapper = cm;
        cartService = cs;
    }

    @Autowired
    private HttpSession session;

    @GetMapping("/session")
    public String session() {
        logger.info(session.getId());
        return session.getId();
    }
}
