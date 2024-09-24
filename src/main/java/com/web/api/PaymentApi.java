package com.web.api;
import com.web.config.Environment;
import com.web.constants.LogUtils;
import com.web.constants.RequestType;
import com.web.dto.request.PaymentDto;
import com.web.dto.response.ResponsePayment;
import com.web.entity.HistoryPay;
import com.web.enums.PayStatus;
import com.web.models.PaymentResponse;
import com.web.models.QueryStatusTransactionResponse;
import com.web.processor.CreateOrderMoMo;
import com.web.processor.QueryTransactionStatus;
import com.web.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentApi {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/all/create-url")
    public ResponseEntity<?> getUrlPayment(@RequestBody PaymentDto paymentDto){
        ResponsePayment responsePayment = paymentService.createUrl(paymentDto);
        return new ResponseEntity<>(responsePayment, HttpStatus.CREATED);
    }

    @GetMapping("/all/check-payment")
    public ResponseEntity<?> checkPayment(@RequestParam("orderId") String orderId,
                                          @RequestParam("requestId") String requestId) throws Exception {
        PayStatus payStatus = paymentService.checkPayment(orderId,requestId);
        return new ResponseEntity<>(payStatus, HttpStatus.CREATED);
    }
}
