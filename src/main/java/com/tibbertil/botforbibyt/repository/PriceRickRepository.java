package com.tibbertil.botforbibyt.repository;

import com.tibbertil.botforbibyt.entity.PriceTickEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRickRepository extends JpaRepository<PriceTickEntity, Long> {

    PriceTickEntity findTop1BySymbolOrderByTimestampDesc(String symbol);
}
