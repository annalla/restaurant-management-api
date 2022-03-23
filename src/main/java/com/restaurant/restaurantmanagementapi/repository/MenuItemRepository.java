package com.restaurant.restaurantmanagementapi.repository;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    @Query(value = "SELECT * FROM menu_item where is_deleted=false", nativeQuery = true)
    @Override
    List<MenuItem> findAll();

    @Query(value = "SELECT * FROM menu_item m where m.is_deleted=false and m.id=:id ", nativeQuery = true)
    @Override
    Optional<MenuItem> findById(@Param("id") Long id);

}
