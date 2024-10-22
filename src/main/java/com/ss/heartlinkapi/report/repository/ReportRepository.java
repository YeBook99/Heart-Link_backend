package com.ss.heartlinkapi.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.heartlinkapi.report.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>{

}
