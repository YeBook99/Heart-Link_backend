package com.ss.heartlinkapi.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.heartlinkapi.report.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>{

}
