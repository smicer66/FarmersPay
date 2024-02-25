package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.FarmBankAccountStatus;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmBankAccount;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import com.probase.fra.farmerspay.api.models.User;
import com.probase.fra.farmerspay.api.models.requests.AddFarmerBankAccountRequest;
import com.probase.fra.farmerspay.api.models.requests.DataTablesRequest;
import com.probase.fra.farmerspay.api.models.requests.UpdateFarmerBankAccountStatusRequest;
import com.probase.fra.farmerspay.api.repository.FarmBankAccountRepository;
import com.probase.fra.farmerspay.api.repository.FarmRepository;
import com.probase.fra.farmerspay.api.repository.impl.FarmRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private FarmRepositoryImpl farmRepositoryImpl;

    @Autowired
    private FarmBankAccountRepository farmBankAccountRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public Farm save(Farm farm){
        return farmRepository.save(farm);
    }

    public Map getFarmsByUserId(DataTablesRequest listFarmsRequest, Long userId, Integer pageSize, Integer pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<FarmDTO> farmList = new ArrayList<FarmDTO>();


//        logger.info("xxx {}", listFarmsRequest.getDraw());
        if(listFarmsRequest!=null && listFarmsRequest.getSearch()!=null && listFarmsRequest.getSearch().containsKey("value")) {
            logger.info("xxx {}", listFarmsRequest.getDraw());
            logger.info("xxx {}", listFarmsRequest.getSearch());
            String searchStringLike = "%".concat(listFarmsRequest.getSearch().get("value")).concat("%");
            farmList = farmRepository.filterFarmsByUserId(userId, searchStringLike, searchStringLike, searchStringLike, searchStringLike, pageable);
        }
        else {
            farmList = farmRepository.findFarmsByUserId(userId, pageable);
        }


        List<Integer> count = new ArrayList<Integer>();
        if(listFarmsRequest!=null && listFarmsRequest.getSearch()!=null && listFarmsRequest.getSearch().containsKey("value")) {

            String searchStringLike = "%".concat(listFarmsRequest.getSearch().get("value")).concat("%");
            count = farmRepository.findFarmsCountByUserId(userId, searchStringLike, searchStringLike, searchStringLike, searchStringLike);
        }
        else {
            count = farmRepository.findFarmsCountByUserId(userId);
        }


        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("farmList", farmList);
        map.put("count", count);

        return map;
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

    public Map getAllFarms(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<FarmDTO> farmList = farmRepository.findAllFarms(pageable);
        List<Integer> count = farmRepository.findAllFarmsCount();
        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("farmList", farmList);
        map.put("count", count);

        return map;
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
