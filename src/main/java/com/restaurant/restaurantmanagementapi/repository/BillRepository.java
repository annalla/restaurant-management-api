package com.restaurant.restaurantmanagementapi.repository;

import com.restaurant.restaurantmanagementapi.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * The BillRepository class manipulate access to bill database
 */
@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
}
