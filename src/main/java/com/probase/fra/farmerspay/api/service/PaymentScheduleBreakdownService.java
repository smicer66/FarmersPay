package com.probase.fra.farmerspay.api.service;


import com.probase.fra.farmerspay.api.enums.PaymentScheduleStatus;
import com.probase.fra.farmerspay.api.models.FarmGroup;
import com.probase.fra.farmerspay.api.models.PaymentSchedule;
import com.probase.fra.farmerspay.api.models.PaymentScheduleBreakdown;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.CreatePaymentScheduleRequest;
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
import java.util.stream.Collectors;

@Service
public class PaymentScheduleBreakdownService {
    @Autowired
    private PaymentScheduleBreakdownRepository paymentScheduleBreakdownRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<PaymentScheduleBreakdown> createPaymentScheduleBreakdown(CreatePaymentScheduleRequest createPaymentScheduleRequest, PaymentSchedule paymentSchedule, FarmService farmService) {
        logger.info("{}....pss ", createPaymentScheduleRequest.getPaymentScheduleBreakdownRequest().size());
        return createPaymentScheduleRequest.getPaymentScheduleBreakdownRequest().stream().map(psb -> {
            logger.info("psb...{}", psb.getAmountToPay());
            FarmGroup farmGroup = farmService.getFarmGroupById(psb.getFarmGroupId());
            PaymentScheduleBreakdown paymentScheduleBreakdown = new PaymentScheduleBreakdown();
            paymentScheduleBreakdown.setPaymentScheduleId(paymentSchedule.getId());
            paymentScheduleBreakdown.setAmountToPay(psb.getAmountToPay());
            paymentScheduleBreakdown.setFarmGroupId(psb.getFarmGroupId());
            paymentScheduleBreakdown.setFarmGroupName(farmGroup.getFarmGroupName());
            paymentScheduleBreakdown.setCreatedAt(LocalDateTime.now());
            paymentScheduleBreakdown.setUpdatedAt(LocalDateTime.now());
            paymentScheduleBreakdown = paymentScheduleBreakdownRepository.save(paymentScheduleBreakdown);
            return paymentScheduleBreakdown;
        }).collect(Collectors.toList());
    }

    public Map getAllPaymentScheduleBreakdown(Long paymentScheduleId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PaymentScheduleBreakdown> paymentScheduleList = paymentScheduleBreakdownRepository.getPaymentScheduleBreakdownByPayScheduleId(paymentScheduleId, pageable);
        List<Integer> count = paymentScheduleBreakdownRepository.findPaymentScheduleBreakdownByScheduleIdCount(paymentScheduleId);
        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("paymentScheduleList", paymentScheduleList);
        map.put("count", count);

        return map;
    }

    public Map getPaymentScheduleBreakdownByPayScheduleId(Long paymentScheduleId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PaymentScheduleBreakdown> paymentScheduleList = paymentScheduleBreakdownRepository.getPaymentScheduleBreakdownByPayScheduleId(paymentScheduleId, pageable);
        List<Integer> count = paymentScheduleBreakdownRepository.findPaymentScheduleBreakdownByScheduleIdCount(paymentScheduleId);
        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("paymentScheduleList", paymentScheduleList);
        map.put("count", count);

        return map;
    }
}
