package com.ecom.paymentService.service;

import com.ecom.paymentService.dto.request.MakePaymentRequest;
import com.ecom.paymentService.exceptions.PaymentFailed;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean makePayment(MakePaymentRequest request){
        if (request.getTotalPrice()==null){
            throw new PaymentFailed("Total Price is null");
        }
        return true;
    }
}
