package com.restaurant.restaurantmanagementapi.repository;


import com.restaurant.restaurantmanagementapi.model.OrderedDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedDetailRepository extends JpaRepository<OrderedDetail,Long> {
}
