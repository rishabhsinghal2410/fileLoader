package com.fileloader.repository;

import com.fileloader.models.CorruptDeal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CorruptDealRepository extends CrudRepository<CorruptDeal, Long> {

    @Query(value = "SELECT cd "
            +"FROM CorruptDeal cd "
            +"WHERE cd.fileName = ?1")
    List<CorruptDeal> dealsByFileName(String fileName);
}
