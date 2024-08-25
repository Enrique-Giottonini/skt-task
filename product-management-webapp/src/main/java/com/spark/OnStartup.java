package com.spark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnStartup implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (productRepository.count() == 0) {
            productService.requestList();
        }
    }
}
