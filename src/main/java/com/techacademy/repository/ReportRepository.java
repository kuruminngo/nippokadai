package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
        //追加
    boolean existsByEmployeeAndReportDateAndIdNot(Employee employee, LocalDate reportDate, int id);
        //追加
    boolean existsByEmployeeAndReportDate(Employee employee, LocalDate reportDate);
    // 特定従業員の日報一覧を取得
    List<Report> findByEmployee(Employee employee);
    
}
