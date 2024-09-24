package com.web.service;

import com.web.config.Environment;
import com.web.constants.LogUtils;
import com.web.constants.RequestType;
import com.web.dto.request.PaymentDto;
import com.web.dto.response.ResponsePayment;
import com.web.entity.HistoryPay;
import com.web.entity.User;
import com.web.enums.PayStatus;
import com.web.models.PaymentResponse;
import com.web.models.QueryStatusTransactionResponse;
import com.web.processor.CreateOrderMoMo;
import com.web.processor.QueryTransactionStatus;
import com.web.repository.HistoryPayRepository;
import com.web.repository.UserRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;

@Component
public class PaymentService {

    @Autowired
    private HistoryPayRepository historyPayRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserRepository userRepository;

    public ResponsePayment createUrl(PaymentDto paymentDto){
        LogUtils.init();
        String orderId = String.valueOf(System.currentTimeMillis());
        String requestId = String.valueOf(System.currentTimeMillis());
        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureATMMoMoResponse = null;
        try {
            captureATMMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(paymentDto.getAmount()), paymentDto.getContent(), paymentDto.getReturnUrl(), paymentDto.getNotifyUrl(), "", RequestType.PAY_WITH_ATM, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("url ====: "+captureATMMoMoResponse.getPayUrl());
        ResponsePayment responsePayment = new ResponsePayment(captureATMMoMoResponse.getPayUrl(),orderId,requestId);
        return responsePayment;
    }

    public PayStatus checkPayment(String orderId, String requestId) throws Exception {
        Environment environment = Environment.selectEnv("dev");
        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryTransactionStatus.process(environment, orderId, requestId);
        Long amount = queryStatusTransactionResponse.getAmount();
        if(queryStatusTransactionResponse.getResultCode() == 0){
            // nếu với orderId và requestId đã được nạp trước đó thì hủy
            if(historyPayRepository.findByOrderIdAndRequestId(orderId, requestId).isPresent()){
                return PayStatus.DA_NAP;
            }
            User user = userUtils.getUserWithAuthority();
            if (user.getAmount() == null){
                user.setAmount(0D);
            }
            user.setAmount(user.getAmount() + amount);
            userRepository.save(user);
            HistoryPay historyPay = new HistoryPay();
            historyPay.setCreatedDate(new Date(System.currentTimeMillis()));
            historyPay.setCreatedTime(new Time(System.currentTimeMillis()));
            historyPay.setOrderId(orderId);
            historyPay.setRequestId(requestId);
            historyPay.setUser(user);
            historyPay.setTotalAmount(Double.valueOf(amount));
            historyPayRepository.save(historyPay);
            return PayStatus.NAP_TIEN_THANH_CONG;
        }
        return PayStatus.NAP_TIEN_THAT_BAI;
    }
}
