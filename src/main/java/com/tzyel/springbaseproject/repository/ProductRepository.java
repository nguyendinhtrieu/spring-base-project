package com.tzyel.springbaseproject.repository;

import com.tzyel.springbaseproject.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findAllByNameContaining(String keyword);
}
