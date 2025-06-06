package Spring_AdamStore.service;

import Spring_AdamStore.dto.response.FileResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponse uploadFile(MultipartFile file) throws FileException, IOException;

    void deleteFile(Long id) throws FileException, IOException;

    PageResponse<FileResponse> getAllFiles(int pageNo, int pageSize, String sortBy);
}
