package com.sidenow.freshgreenish.domain.purchase.repository;

import com.sidenow.freshgreenish.domain.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, CustomPurchaseRepository {

}
