package com.micropos.cart.service;

import com.micropos.cart.dto.ProductDto;
import com.micropos.cart.model.Item;
import com.micropos.cart.model.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {
    private Log logger = LogFactory.getLog(CartServiceImpl.class);

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    public void checkout(List<Item> cart) {
        double price = 0;
        for (Item i : cart) {
            price += i.getQuantity() * i.getProduct().getPrice();
        }
        logger.info("check out, total price: " + price);
        cart.clear();
    }

    public List<Item> add(List<Item> cart, Product product, int amount) {
        //getProductFromId(productId);
        if (product != null) {
            for (Item i : cart) {
                if (Objects.equals(i.getProduct().getId(), product.getId())) {
                    if (i.getQuantity() + amount <= 0) {
                        cart.remove(i);
                    } else i.setQuantity(i.getQuantity() + amount);
                    return cart;
                }
            }
            cart.add(new Item(product, amount));
        }
        return cart;
    }

    public ProductDto getProductFromId(String productId) {
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance serviceInstance = loadBalancerClient.choose("POS-PRODUCTS");
        String url = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort())
                + "/products-api/products/" + productId;
        //logger.info(restTemplate.getForObject(url, ProductDto.class));
        return restTemplate.getForObject(url, ProductDto.class);
    }

    public List<Item> delete(List<Item> cart, String productId) {
        for (Item i : cart) {
            if (Objects.equals(i.getProduct().getId(), productId)) {
                cart.remove(i);
                break;
            }
        }
        return cart;
    }

    public List<Item> modify(List<Item> cart, String productId, int amount) {
        for (Item i : cart) {
            if (Objects.equals(i.getProduct().getId(), productId)) {
                cart.remove(i);
                i.setQuantity(amount);
                cart.add(i);
                break;
            }
        }
        return cart;
    }

}
