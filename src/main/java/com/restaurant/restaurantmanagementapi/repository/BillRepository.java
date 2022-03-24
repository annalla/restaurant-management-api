package com.restaurant.restaurantmanagementapi.repository;

import com.restaurant.restaurantmanagementapi.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill,Long> {
}
