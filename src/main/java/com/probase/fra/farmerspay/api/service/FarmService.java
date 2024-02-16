package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.FarmBankAccountStatus;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmBankAccount;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.AddFarmerBankAccountRequest;
import com.probase.fra.farmerspay.api.models.requests.UpdateFarmerBankAccountStatusRequest;
import com.probase.fra.farmerspay.api.repository.FarmBankAccountRepository;
import com.probase.fra.farmerspay.api.repository.FarmRepository;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FarmBankAccountRepository farmBankAccountRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public Farm save(Farm farm){
        return farmRepository.save(farm);
    }

    public List<Farm> getFarmsByUserId(Long userId, Integer pageSize, Integer pageNumber){
        List<Farm> farmList = farmRepository.findFarmsByUserId(userId, pageSize, pageNumber*pageSize);
        return farmList;
    }

    public FarmBankAccount addNewFarmBankAccount(AddFarmerBankAccountRequest addFarmerBankAccountRequest, User authenticatedUser) {
        FarmBankAccount farmBankAccount = new FarmBankAccount();
        farmBankAccount.setBankAccountName(addFarmerBankAccountRequest.getBankAccountName());
        farmBankAccount.setFarmId(addFarmerBankAccountRequest.getFarmId());
        farmBankAccount.setFarmBankAccountStatus(FarmBankAccountStatus.ACTIVE);
        farmBankAccount.setBankAccountNumber(addFarmerBankAccountRequest.getBankAccountNumber());
        farmBankAccount.setBankId(addFarmerBankAccountRequest.getBankId());
        farmBankAccount.setCreatedAt(LocalDateTime.now());
        farmBankAccount.setUpdatedAt(LocalDateTime.now());
        farmBankAccount.setFarmerUserId(authenticatedUser.getId());
        farmBankAccount = farmBankAccountRepository.save(farmBankAccount);

        return farmBankAccount;
    }


    public List<FarmBankAccount> getAllFarmBankAccounts() {
        List<FarmBankAccount> farmBankAccountList = farmBankAccountRepository.findAll();

        return farmBankAccountList;
    }

    public List<FarmBankAccount> getFarmBankAccountsByFarmId(Long farmId) {
        List<FarmBankAccount> farmBankAccountList = farmBankAccountRepository.findFarmBankAccountByFarmId(farmId);

        return farmBankAccountList;
    }

    public List<FarmBankAccount> getFarmBankAccountsByFarmIdAndUserId(Long farmId, Long userId) {
        List<FarmBankAccount> farmBankAccountList = farmBankAccountRepository.findFarmBankAccountByFarmIdAndUserId(farmId, userId);

        return farmBankAccountList;
    }

    public FarmBankAccount updateFarmBankAccountStatus(UpdateFarmerBankAccountStatusRequest updateFarmerBankAccountStatusRequest) {
        FarmBankAccount farmBankAccount = farmBankAccountRepository.getById(updateFarmerBankAccountStatusRequest.getFarmBankAccountId());

        if(farmBankAccount==null)
        {
            return null;
        }
        farmBankAccount.setFarmBankAccountStatus(FarmBankAccountStatus.valueOf(updateFarmerBankAccountStatusRequest.getBankAccountStatus()));
        farmBankAccount.setUpdatedAt(LocalDateTime.now());
        farmBankAccountRepository.save(farmBankAccount);

        return farmBankAccount;

    }

    public List<Farm> getAllFarms(Integer pageSize, Integer pageNumber) {
        List<Farm> farmList = farmRepository.findAllFarms(pageSize, pageNumber);
        return farmList;
    }

    public Farm getFarmById(Long farmId) {
        Farm farm = farmRepository.getById(farmId);
        return farm;
    }

    public void deleteFarmBankAccount(FarmBankAccount farmBankAccount) {

        farmBankAccount.setDeletedAt(LocalDateTime.now());
        farmBankAccountRepository.save(farmBankAccount);
    }
}
