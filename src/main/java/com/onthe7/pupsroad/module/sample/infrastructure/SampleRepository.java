package com.onthe7.pupsroad.module.sample.infrastructure;

import com.onthe7.pupsroad.module.sample.domain.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
