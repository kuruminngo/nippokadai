package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository ) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report) {
        // 重複対応不要のことからif文を削除
        // if (findById(report.getId()) != null) {
        //     return ErrorKinds.DUPLICATE_ERROR;
        // }

        // 入力チェック
        if (report.getReportDate() == null) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getTitle() == null || report.getTitle().isEmpty()) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getTitle().length() > 100) {
            return ErrorKinds.TITLE_TOO_LONG; // 桁数チェック
        }
        if (report.getContent() == null || report.getContent().isEmpty()) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getContent().length() > 600) {
            return ErrorKinds.CONTENT_TOO_LONG; // 桁数チェック
        }

        // 業務チェック：同一日付の日報が既に存在するか確認
        boolean isDuplicate = reportRepository.existsByEmployeeAndReportDate(report.getEmployee(), report.getReportDate());
        if (isDuplicate) {
            return ErrorKinds.DATECHECK_ERROR; // 重複チェック
        }

        report.setDeleteFlg(false);
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);
        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findById(Integer id) {
        Optional<Report> option = reportRepository.findById(id);
        return option.orElse(null);
    }

    @Transactional
    public ErrorKinds update(int id, Report report) {
        Report existingReport = findById(id);
        if (existingReport == null) {
            return ErrorKinds.NOT_FOUND; // レコードが見つからない場合
        }

        // 入力チェック
        if (report.getReportDate() == null) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getTitle() == null || report.getTitle().isEmpty()) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getTitle().length() > 100) {
            return ErrorKinds.TITLE_TOO_LONG; // 桁数チェック
        }
        if (report.getContent() == null || report.getContent().isEmpty()) {
            return ErrorKinds.BLANK_ERROR; // 空チェック
        }
        if (report.getContent().length() > 600) {
            return ErrorKinds.CONTENT_TOO_LONG; // 桁数チェック
        }

        // 業務チェック：同一日付の日報が既に存在するか確認
        boolean isDuplicate = reportRepository.existsByEmployeeAndReportDateAndIdNot(report.getEmployee(), report.getReportDate(), id);
        if (isDuplicate) {
            return ErrorKinds.DATECHECK_ERROR; // 重複チェック
        }

        existingReport.setReportDate(report.getReportDate());
        existingReport.setTitle(report.getTitle());
        existingReport.setContent(report.getContent());
        existingReport.setUpdatedAt(LocalDateTime.now());

        reportRepository.save(existingReport);
        return ErrorKinds.SUCCESS;
    }
        // 日報削除
        @Transactional
        public ErrorKinds delete(int id) {
            Report existingReport = findById(id);
            if (existingReport == null) {
                return ErrorKinds.NOT_FOUND; // レコードが見つからない場合
            }
            existingReport.setDeleteFlg(true);
            existingReport.setUpdatedAt(LocalDateTime.now());
            reportRepository.save(existingReport);
            return ErrorKinds.SUCCESS;
        }
    }
