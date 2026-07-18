package com.ecom.orderService.clients.paymentService;

import com.ecom.orderService.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="paymentService", configuration= FeignClientConfig.class)
public interface PaymentClient {

    @PostMapping ("/api/payment/make")
    boolean makePayment(@RequestBody MakePaymentRequest paymentRequest);
}
