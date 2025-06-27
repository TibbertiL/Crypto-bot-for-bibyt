package com.tibbertil.botforbibyt.repository;

import com.tibbertil.botforbibyt.entity.StrategyConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyConfigRepository extends JpaRepository<StrategyConfigEntity, Long> {

     List<StrategyConfigEntity> findByActiveTrue();
}
