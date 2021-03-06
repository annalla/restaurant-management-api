package com.restaurant.restaurantmanagementapi.repository;

import com.restaurant.restaurantmanagementapi.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The MenuItemRepository class manipulate access to menu item database
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query(value = "SELECT * FROM menu_item m where m.status=true", nativeQuery = true)
    @Override
    Page<MenuItem> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM menu_item m where m.status=true and m.id=:id ", nativeQuery = true)
    Optional<MenuItem> findActiveItemById(@Param("id") Long id);

    @Query(value = "SELECT * FROM menu_item m where m.status=true and m.name=:name ", nativeQuery = true)
    Optional<MenuItem> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM menu_item m where m.status=true and m.name=:name and m.id!=:id", nativeQuery = true)
    Optional<MenuItem> findByName(@Param("name") String name, @Param("id") Long id);

    @Query(value = "SELECT * FROM menu_item m where m.status=true and m.name=:name ", nativeQuery = true)
    MenuItem findByNameItem(@Param("name") String name);

    @Query(value = "SELECT * FROM menu_item m where m.status=true and (lower(m.name) LIKE lower(concat('%', ?1,'%')) or lower(m.description) LIKE lower(concat('%', ?1,'%'))) ",
            countQuery = "SELECT count(*) FROM menu_item m where m.status=true and (lower(m.name) LIKE lower(concat('%', ?1,'%')) or lower(m.description) LIKE lower(concat('%', ?1,'%'))) ",
            nativeQuery = true)
    Page<MenuItem> search(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM menu_item m where m.status=?2 and (lower(m.name) LIKE lower(concat('%', ?1,'%')) or lower(m.description) LIKE lower(concat('%', ?1,'%'))) ",
            countQuery = "SELECT count(*) FROM menu_item m where m.status=?2 and (lower(m.name) LIKE lower(concat('%', ?1,'%')) or lower(m.description) LIKE lower(concat('%', ?1,'%'))) ",
            nativeQuery = true)
    Page<MenuItem> searchWithFilter(@Param("keyword") String keyword, @Param("filter") Boolean filter, Pageable pageable);
}

