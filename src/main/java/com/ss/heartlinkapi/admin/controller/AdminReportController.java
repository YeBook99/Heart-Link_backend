package com.ss.heartlinkapi.admin.controller;

import com.ss.heartlinkapi.report.dto.ReportDTO;
import com.ss.heartlinkapi.report.entity.ReportEntity;
import com.ss.heartlinkapi.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;

//    reportlist 불러오는 메서드
    @GetMapping("/report")
    public ResponseEntity<List<ReportDTO>> getAllList(){

        List<ReportDTO> reportList = new ArrayList<>();

        List<ReportEntity> reportEntities =  reportService.getAllList();

        for (ReportEntity entity : reportEntities) {

            ReportDTO dto = new ReportDTO();
            dto.setReportId(entity.getReportId());
            dto.setCommentId(String.valueOf(entity.getCommentId().getCommentId()));
            dto.setUserId(String.valueOf(entity.getUserId().getUserId()));
            dto.setPostId(String.valueOf(entity.getPostId().getPostId()));
            dto.setStatus(entity.getStatus());
            dto.setReason(entity.getReason());
            dto.setCreateAt(entity.getCreatedAt().toLocalDateTime());
            reportList.add(dto);
        }

        return ResponseEntity.ok(reportList);
    }
    
//    신고 반려 기능
    @PutMapping("/report/{reportId}/rejection")
    public ResponseEntity<String> rejectReport(@PathVariable("reportId") Long reportId){

        reportService.updateStatus(reportId);

        return ResponseEntity.ok("reject report");
    }
}
