package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmDTO;
import com.probase.fra.farmerspay.api.models.WorkflowUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkflowRepository extends JpaRepository<WorkflowUser, Long>
{

    @Query("SELECT wf FROM WorkflowUser wf WHERE wf.deletedAt IS NULL")
    List<WorkflowUser> findAllWorkflow(Pageable pageable);

    @Query("SELECT COUNT(*) as c FROM WorkflowUser wf WHERE wf.deletedAt IS NULL")
    List<Integer> findAllWorkflowCount();
}
