package com.ss.heartlinkapi.report.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ss.heartlinkapi.report.dto.ReportDTO;
import com.ss.heartlinkapi.report.entity.ReportEntity;
import com.ss.heartlinkapi.report.repository.ReportRepository;

@Service
public class ReportService {
	
	private final ReportRepository reportRepository;
	
	public ReportService(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

}
