package com.restaurant.restaurantmanagementapi.repository;


import com.restaurant.restaurantmanagementapi.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem,Long> {

    @Query(value = "SELECT * FROM bill_item b  where b.menu_item_id=:id", nativeQuery = true)
    List<BillItem> findByMenuItemId(@Param("id") Long id);
    @Query(value = "SELECT * FROM bill_item b where b.bill_id=:id", nativeQuery = true)
    List<BillItem> findByBillId(@Param("id") Long id);
    @Query(value = "SELECT * FROM bill_item b where b.menu_item_id=:menuItemId and b.bill_id=:billId", nativeQuery = true)
    Optional<BillItem> findByMenuItemIdAdBillId(@Param("billId") Long billId,@Param("menuItemId") Long menuItemId);
}
