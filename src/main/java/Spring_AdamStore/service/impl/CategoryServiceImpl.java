package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.request.CategoryRequest;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.entity.*;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.CategoryMapper;
import Spring_AdamStore.mapper.ProductMapper;
import Spring_AdamStore.repository.CategoryRepository;
import Spring_AdamStore.repository.ProductRepository;
import Spring_AdamStore.service.CategoryService;
import Spring_AdamStore.service.PageableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static Spring_AdamStore.constants.EntityStatus.ACTIVE;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PageableService pageableService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if(categoryRepository.countByName(request.getName()) > 0){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = categoryMapper.toCategory(request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse fetchById(Long id) {
        Category category = findActiveCategoryById(id);

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public PageResponse<CategoryResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Category.class);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return PageResponse.<CategoryResponse>builder()
                .page(categoryPage.getNumber() + 1)
                .size(categoryPage.getSize())
                .totalPages(categoryPage.getTotalPages())
                .totalItems(categoryPage.getTotalElements())
                .items(categoryMapper.toCategoryResponseList(categoryPage.getContent()))
                .build();
    }

    @Override
    public PageResponse<CategoryResponse> fetchAllCategoriesForAdmin(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Category.class);

        Page<Category> categoryPage = categoryRepository.findAllCategories(pageable);

        return PageResponse.<CategoryResponse>builder()
                .page(categoryPage.getNumber() + 1)
                .size(categoryPage.getSize())
                .totalPages(categoryPage.getTotalPages())
                .totalItems(categoryPage.getTotalElements())
                .items(categoryMapper.toCategoryResponseList(categoryPage.getContent()))
                .build();

    }


    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findActiveCategoryById(id);

        if(!request.getName().equals(category.getName()) && categoryRepository.countByName(request.getName()) > 0){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        categoryMapper.update(category, request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Category category = findActiveCategoryById(id);

        if(productRepository.countActiveProductsByCategoryId(category.getId(), ACTIVE.name()) > 0){
            throw new AppException(ErrorCode.CATEGORY_DELETE_CONFLICT);
        }

        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public CategoryResponse restore(long id) {
        Category category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        category.setStatus(ACTIVE);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public PageResponse<ProductResponse> fetchByCategoryId(int pageNo, int pageSize, String sortBy, Long categoryId) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Product.class);

        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

        return PageResponse.<ProductResponse>builder()
                .page(productPage.getNumber() + 1)
                .size(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .items(productMapper.toProductResponseList(productPage.getContent()))
                .build();
    }

    @Override
    public PageResponse<ProductResponse> fetchByCategoryIdForAdmin(int pageNo, int pageSize, String sortBy, Long categoryId) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Product.class);

        Page<Product> productPage = productRepository.findAllByCategoryId(categoryId, pageable);

        return PageResponse.<ProductResponse>builder()
                .page(productPage.getNumber() + 1)
                .size(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .items(productMapper.toProductResponseList(productPage.getContent()))
                .build();
    }

    private Category findActiveCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
    }
}
