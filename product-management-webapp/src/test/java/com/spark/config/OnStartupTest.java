package com.spark.config;

import com.spark.OnStartup;
import com.spark.ProductRepository;
import com.spark.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnStartupTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OnStartup onStartup;

    @Before
    public void setUp() {
        reset(productRepository, productService); // Reset mocks before each test
    }

    @Test
    public void testOnApplicationEvent_ProductCountZero() {
        // Arrange
        when(productRepository.count()).thenReturn(0);
        ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);

        // Act
        onStartup.onApplicationEvent(event);

        // Assert
        verify(productService, times(1)).requestList();
    }

    @Test
    public void testOnApplicationEvent_ProductCountNonZero() {
        // Arrange
        when(productRepository.count()).thenReturn(5);
        ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);

        // Act
        onStartup.onApplicationEvent(event);

        // Assert
        verify(productService, never()).requestList();
    }
}
