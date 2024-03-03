package com.probase.fra.farmerspay.api.service;


import com.probase.fra.farmerspay.api.enums.PaymentScheduleStatus;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.*;
import com.probase.fra.farmerspay.api.models.requests.CreatePaymentScheduleRequest;
import com.probase.fra.farmerspay.api.repository.BankRepository;
import com.probase.fra.farmerspay.api.repository.PaymentScheduleBreakdownRepository;
import com.probase.fra.farmerspay.api.repository.PaymentScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentScheduleService {
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private PaymentScheduleBreakdownRepository paymentScheduleBreakdownRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Map getAllPaymentSchedules(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAllPaymentSchedule(pageable);
        List<Integer> count = paymentScheduleRepository.findAllPaymentScheduleCount();
        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("paymentScheduleList", paymentScheduleList);
        map.put("count", count);

        return map;
    }

    public PaymentSchedule createPaymentSchedule(CreatePaymentScheduleRequest createPaymentScheduleRequest, User user, FarmService farmService) {
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setCreatedAt(LocalDateTime.now());
        paymentSchedule.setUpdatedAt(LocalDateTime.now());
        paymentSchedule.setScheduleMonth(createPaymentScheduleRequest.getScheduleMonth());
        paymentSchedule.setScheduleYear(createPaymentScheduleRequest.getScheduleYear());
        paymentSchedule.setCreatedByUserId(user.getId());
        paymentSchedule.setCreatedByUserName(user.getFirstName().concat(" ").concat(user.getLastName()));
        paymentSchedule.setPaymentScheduleStatus(PaymentScheduleStatus.PENDING);
        paymentSchedule = paymentScheduleRepository.save(paymentSchedule);
        return paymentSchedule;
    }
}
