package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 従業員一覧画面※確認
    @GetMapping
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail) {
        if(userDetail.getEmployee().getRole() == Employee.Role.ADMIN) {
            // 全ての日報を取得し、そのサイズ（件数）をモデルに追加
            model.addAttribute("listSize", reportService.findAll().size());
            // 全ての日報を取得し、リストをモデルに追加
            model.addAttribute("reportList", reportService.findAll());
        }else {
            model.addAttribute("listSize", reportService.findByEmployee(userDetail.getEmployee()).size());
            model.addAttribute("reportList", reportService.findByEmployee(userDetail.getEmployee()));
        }

        // "reports/list" という名前のテンプレートを返す
        return "reports/list";
    }

    // 従業員詳細画面　追加
    @GetMapping(value  = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail"; // このパスは templates/reports/detail.html に対応
    }
    // 従業員新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report ,@AuthenticationPrincipal UserDetail userDetail, Model model) {
        report.setEmployee(userDetail.getEmployee());
        return "reports/new";
    }
    // 従業員新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res,@AuthenticationPrincipal UserDetail userDetail, Model model) {
        if (res.hasErrors()) {
            return create(report,userDetail,model);

        }
        report.setEmployee(userDetail.getEmployee());
        ErrorKinds result = reportService.save(report);
        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return create(report,userDetail,model);
        }
        return "redirect:/reports";

    }
    // 日報更新画面表示
    @GetMapping("/{id}/update")
    public String edit(@PathVariable("id") int id, Model model) {
        Report report = reportService.findById(id);
        model.addAttribute("report", report);
        return "reports/edit"; // このパスは src/main/resources/templates/reports/edit.html に対応
    }

    // 日報更新処理
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") int id, @Validated Report report, BindingResult res, Model model) {
        if (res.hasErrors()) {
            return edit(id, model);
        }
        ErrorKinds result = reportService.update(id, report);
        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return edit(id, model);
        }
        return "redirect:/reports"; // 更新後にリダイレクトするパス
    }
    // 従業員削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        ErrorKinds result = reportService.delete(id, userDetail);
        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            return detail(id, model);
        }
        return "redirect:/reports";
    }

}
