package Spring_AdamStore.controller;

import Spring_AdamStore.dto.request.ReviewRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.service.RevenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j(topic = "REVENUE-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1")
@RestController
public class RevenueController {

    private final RevenueService revenueService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetched monthly revenue data",
    description = "API này dùng để ấy doanh thu theo tháng trong khoảng (startDate (yyyy-MM-dd) đến endDate (yyyy-MM-dd))")
    @GetMapping("/revenues/monthly")
    public ApiResponse<List<RevenueByMonthDTO>> getMonthlyRevenue(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                      @Parameter(example = "2025-02-20") LocalDate startDate,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       @Parameter(example = "2025-05-10") LocalDate endDate){
        return ApiResponse.<List<RevenueByMonthDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetched monthly revenue data")
                .result(revenueService.getRevenueByMonth(startDate, endDate))
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Fetched daily order revenue data",
    description = "API này dùng để lấy dữ liệu doanh thu của các đơn hàng (yyyy-MM-dd)")
    @GetMapping("/revenues/daily-orders")
    public ApiResponse<PageResponse<OrderRevenueDTO>> getOrderRevenueByDate(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                        @RequestParam(defaultValue = "1") int pageNo,
                                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                                            @RequestParam(required = false) String sortBy,
                                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        @Parameter(example = "2025-02-20") LocalDate startDate,
                                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        @Parameter(example = "2025-05-10") LocalDate endDate){
        return ApiResponse.<PageResponse<OrderRevenueDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetched daily order revenue data")
                .result(revenueService.getOrderRevenueByDate(pageNo, pageSize, sortBy, startDate, endDate))
                .build();
    }
}
