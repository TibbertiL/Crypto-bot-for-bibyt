package com.tibbertil.botforbibyt.repository;

import com.tibbertil.botforbibyt.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, Long> {
}
