package Spring_AdamStore.controller;

import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.exception.FileException;
import Spring_AdamStore.service.FileService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j(topic = "FILE-CONTROLLER")
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/file")
@RestController
public class FileController {

    private final FileService productImageService;

    @PostMapping(value = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileResponse> uploadImage(@RequestParam("fileImage") MultipartFile file) throws IOException, FileException {

        return ApiResponse.<FileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Upload File")
                .result(productImageService.uploadFile(file))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ApiResponse<PageResponse<FileResponse>> getAllFiles(@Min(value = 1, message = "pageNo phải lớn hơn 0")
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @RequestParam(required = false) String sortBy){
        return ApiResponse.<PageResponse<FileResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(productImageService.getAllFiles(pageNo, pageSize, sortBy))
                .message("Fetch All Files With Pagination")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) throws Exception {
         productImageService.deleteFile(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete File")
                .result(null)
                .build();

    }

}
