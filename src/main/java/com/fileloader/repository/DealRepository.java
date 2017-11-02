package com.fileloader.repository;

import com.fileloader.models.Deal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DealRepository extends CrudRepository<Deal, Long> {

    @Query(value = "SELECT deal "
            +"FROM Deal deal "
            +"WHERE deal.fileName = ?1")
    List<Deal> dealsByFileName(String fileName);
}
