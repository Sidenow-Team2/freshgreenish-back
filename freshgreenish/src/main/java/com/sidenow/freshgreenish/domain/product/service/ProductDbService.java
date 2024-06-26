package com.sidenow.freshgreenish.domain.product.service;

import com.sidenow.freshgreenish.domain.product.dto.GetProductCategory;
import com.sidenow.freshgreenish.domain.product.dto.GetProductDetail;
import com.sidenow.freshgreenish.domain.product.entity.Product;
import com.sidenow.freshgreenish.domain.product.entity.ProductImage;
import com.sidenow.freshgreenish.domain.product.repository.ProductRepository;
import com.sidenow.freshgreenish.domain.user.entity.User;
import com.sidenow.freshgreenish.domain.user.service.UserDbService;
import com.sidenow.freshgreenish.global.exception.BusinessLogicException;
import com.sidenow.freshgreenish.global.exception.ExceptionCode;
import com.sidenow.freshgreenish.global.file.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDbService {
    private final ProductRepository productRepository;
    private final UserDbService userDbService;

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product saveAndReturnProduct(Product product) {
        return productRepository.save(product);
    }

    public void saveProductImage(List<UploadFile> productImages, Product product) {
        productImages.forEach(productImage -> {
            ProductImage createProductImage = ProductImage.builder()
                    .originFileName(productImage.getOriginFileName())
                    .fileName(productImage.getFileName())
                    .filePath(productImage.getFilePath())
                    .fileSize(productImage.getFileSize())
                    .build();
            product.addProductImage(createProductImage);
        });
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("존재 하지 않는 상품입니다."));
        
   }
  
    public Product ifExistsReturnProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
    }

    public void ifProductIsDeletedThrowError(Long productId) {
        Boolean isDeleted = productRepository.isDeleted(productId);
        if (isDeleted.equals(true)) {
                throw new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND);
        }
    }

    public Page<GetProductCategory> getMainPage(Pageable pageable) {
        return productRepository.getMainPage(pageable);
    }

    public GetProductDetail getProductDetail(Long productId, OAuth2User ouath) {
        Long userId = userDbService.findUserIdByOauth(ouath);
        if (userId != null) return productRepository.getProductDetailUponLogin(productId, userId);
        return productRepository.getProductDetail(productId);
    }

    public Page<GetProductCategory> getProductCategory(String category, Integer sortId, Pageable pageable) {
        if (sortId == 2) return productRepository.getProductCategoryOrderByPurchaseCount(category, pageable);
        else if (sortId == 3) return productRepository.getProductCategoryOrderByLikeCount(category, pageable);
        else return productRepository.getProductCategoryOrderByProductId(category, pageable);
    }

    public Page<GetProductCategory> searchProductCategoryForTitle(String title, String category, Integer sortId, Pageable pageable) {
        if (sortId == 2) return productRepository.searchProductCategoryForTitleOrderByPurchaseCount(split(title), category, pageable);
        else if (sortId == 3) return productRepository.searchProductCategoryForTitleOrderByLikeCount(split(title), category, pageable);
        else return productRepository.searchProductCategoryForTitleOrderByProductId(split(title), category, pageable);
    }

    private List<String> split(String title) {
        List<String> titles = new ArrayList<>();
        String[] list = title.split(" ");
        Collections.addAll(titles, list);
        return titles;
    }

    public Integer getPrice(Long productId) {
        return productRepository.getPrice(productId);
    }

    public Integer getDiscountPrice(Long productId) {
        return productRepository.getDiscountPrice(productId);
    }

    public User findUser(OAuth2User oauth) {
        return userDbService.findUserByEmail(oauth);
    }

    public String getProductTitle(Long purchaseId) {
        return productRepository.getProductTitle(purchaseId);
    }
}
