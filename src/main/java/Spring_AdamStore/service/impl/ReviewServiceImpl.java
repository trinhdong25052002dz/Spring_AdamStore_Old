package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.request.ReviewRequest;
import Spring_AdamStore.dto.request.ReviewUpdateRequest;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.ReviewResponse;
import Spring_AdamStore.entity.Product;
import Spring_AdamStore.entity.Review;
import Spring_AdamStore.entity.User;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.ReviewMapper;
import Spring_AdamStore.repository.ProductRepository;
import Spring_AdamStore.repository.ReviewRepository;
import Spring_AdamStore.service.CurrentUserService;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "REVIEW-SERVICE")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CurrentUserService currentUserService;
    private final ReviewMapper reviewMapper;
    private final PageableService pageableService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ReviewResponse create(ReviewRequest request) {
        Review review = reviewMapper.toReview(request);

        User user = currentUserService.getCurrentUser();
        review.setUser(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        review.setProduct(product);

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse fetchById(Long id) {
        Review review = findReviewById(id);

        return reviewMapper.toReviewResponse(review);
    }

    @Override
    public PageResponse<ReviewResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Review.class);

        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        return PageResponse.<ReviewResponse>builder()
                .page(reviewPage.getNumber() + 1)
                .size(reviewPage.getSize())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(reviewPage.getTotalElements())
                .items(reviewMapper.toReviewResponseList(reviewPage.getContent()))
                .build();
    }

    @Override
    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateRequest request) {
        Review review = findReviewById(id);

        reviewMapper.update(review, request);

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) {
        Review review = findReviewById(id);
        reviewRepository.delete(review);
    }

    private Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED));
    }
}
