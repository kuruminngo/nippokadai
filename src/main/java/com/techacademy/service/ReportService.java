package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {
	
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository ) {
        this.reportRepository = reportRepository;
    }
    // 従業員保存
	   @Transactional
	    public ErrorKinds save(Report report) {
	       //重複対応不要のことからif文を削除 
		   //if (findById(report.getId()) != null) {
	          // return ErrorKinds.DUPLICATE_ERROR;
	        //}
	        report.setDeleteFlg(false);
	        LocalDateTime now = LocalDateTime.now();
	        report.setCreatedAt(now);
	        report.setUpdatedAt(now);
	        reportRepository.save(report);
	        return ErrorKinds.SUCCESS;
	    }
	    
	  // 従業員一覧表示処理
	    public List<Report> findAll() {
	        return reportRepository.findAll();
	    }

}
