package com.micropos.cart.rest;

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

    @Autowired
    private HttpSession session;

    @GetMapping("/session")
    public String session() {
        logger.info(session.getId());
        return session.getId();
    }
}
