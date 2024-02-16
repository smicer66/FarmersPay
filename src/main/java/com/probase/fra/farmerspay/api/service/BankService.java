package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.FarmBankAccountStatus;
import com.probase.fra.farmerspay.api.models.Bank;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmBankAccount;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.*;
import com.probase.fra.farmerspay.api.repository.BankRepository;
import com.probase.fra.farmerspay.api.repository.FarmBankAccountRepository;
import com.probase.fra.farmerspay.api.repository.FarmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public Bank save(NewBankRequest newBankRequest, User authenticatedUser){
        Bank bank = new Bank();
        bank.setBankCode(newBankRequest.getBankCode());
        bank.setBankName(newBankRequest.getBankName());
        bank.setMobileMoney(newBankRequest.getIsMobileMoney());
        return bankRepository.save(bank);
    }

    public List<Bank> getBanksByMobileMoney(Boolean isMobileMoney){
        List<Bank> bankList = bankRepository.findMobileMoneyBanks(isMobileMoney);
        return bankList;
    }


    public Bank updateFarmBankAccount(UpdateBankRequest updateBankRequest, User authenticatedUser) {
        Bank bank = bankRepository.getById(updateBankRequest.getBankId());
        bank.setBankCode(updateBankRequest.getBankCode());
        bank.setBankName(updateBankRequest.getBankName());
        bank.setMobileMoney(updateBankRequest.getIsMobileMoney());
        bankRepository.save(bank);

        return bank;
    }

    public List<Bank> getAllBanks(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Bank> allBanks = new ArrayList<Bank>();

        if(pageNumber==null || pageSize==null)
            allBanks = bankRepository.findAll();
        else
            allBanks = bankRepository.findAllByPages(pageable);

        return allBanks;
    }

    public Bank getBankById(Long bankId) {
        Optional<Bank> bankOpt = bankRepository.findById(bankId);
        if(bankOpt.isPresent())
            return bankOpt.get();
        else
            return null;
    }
}
