package com.probase.fra.farmerspay.api.service;

import com.probase.fra.farmerspay.api.enums.FarmBankAccountStatus;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.*;
import com.probase.fra.farmerspay.api.models.requests.AddFarmerBankAccountRequest;
import com.probase.fra.farmerspay.api.models.requests.CreateWorkflowRequest;
import com.probase.fra.farmerspay.api.models.requests.DataTablesRequest;
import com.probase.fra.farmerspay.api.models.requests.UpdateFarmerBankAccountStatusRequest;
import com.probase.fra.farmerspay.api.repository.FarmBankAccountRepository;
import com.probase.fra.farmerspay.api.repository.FarmRepository;
import com.probase.fra.farmerspay.api.repository.WorkflowRepository;
import com.probase.fra.farmerspay.api.repository.impl.FarmRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    @Autowired
    private WorkflowRepository workflowRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public WorkflowUser save(WorkflowUser workflowUser){
        return workflowRepository.save(workflowUser);
    }

    public Map getAllWorkflowUsers(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<WorkflowUser> workflowUserList = workflowRepository.findAllWorkflow(pageable);
        List<Integer> count = workflowRepository.findAllWorkflowCount();
        logger.info("{}", count);

        Map map = new HashMap<>();
        map.put("workflowUserList", workflowUserList);
        map.put("count", count);

        return map;
    }

    public List<WorkflowUser> createWorkflow(CreateWorkflowRequest createWorkflowRequest, UserService userService) {
        workflowRepository.deleteAll();
        List<WorkflowUser> workflowUserList = createWorkflowRequest.getCreateWorkflowRequestData().stream().map(t -> {

            User user = userService.getUserById(t.getUserId());
            WorkflowUser workflowUser = new WorkflowUser();
            workflowUser.setCreatedAt(LocalDateTime.now());
            workflowUser.setUpdatedAt(LocalDateTime.now());
            workflowUser.setLevel(t.getLevel());
            workflowUser.setUserId(t.getUserId());
            workflowUser.setUserRole(UserRole.ADMINISTRATOR);
            workflowUser.setPermissionList(String.join(", ", t.getPermission()));
            workflowUser.setUserFullName(user.getFirstName().concat(" ").concat(user.getLastName()));
            workflowUser = workflowRepository.save(workflowUser);

            return workflowUser;
        }).collect(Collectors.toList());

        return workflowUserList;
    }
}
