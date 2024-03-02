package com.uriel.travel.service;


import com.uriel.travel.domain.entity.Order;
import com.uriel.travel.domain.entity.Payment;
import com.uriel.travel.repository.CancelRepository;
import com.uriel.travel.repository.OrderRepository;
import com.uriel.travel.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Transactional
@RequiredArgsConstructor
@Service
public class TossService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CancelRepository cancelRepository;

    public void createPayment(String orderNumber, JSONObject jsonObject) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Payment payment = Payment.builder()
                .paymentKey(jsonObject.get("paymentKey").toString())
                .mid(jsonObject.get("mid").toString())
                .method(jsonObject.get("method").toString())
                .totalAmount(Integer.parseInt(jsonObject.get("totalAmount").toString()))
                .balanceAmount(Integer.parseInt(jsonObject.get("balanceAmount").toString()))
                .status(jsonObject.get("status").toString())
                .requestedAt(LocalDateTime.parse(jsonObject.get("requestedAt").toString(), formatter))
                .approvedAt(LocalDateTime.parse(jsonObject.get("approvedAt").toString(), formatter))
                .lastTransactionKey(jsonObject.get("lastTransactionKey").toString())
                .build();

        Order order = orderRepository.findByOrderNumber(orderNumber);
        payment.setOrder(order);

        paymentRepository.save(payment);
    }
}
