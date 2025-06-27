package com.tibbertil.botforbibyt.repository;

import com.tibbertil.botforbibyt.entity.LogEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {
}
