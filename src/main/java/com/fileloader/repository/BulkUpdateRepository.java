package com.fileloader.repository;

import com.fileloader.models.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class BulkUpdateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FileStatusRepository fileStatusRepository;

    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;

    @Async("processExecutor")
    @Transactional
    public <T> Future<String> bulkSave(Collection<T> entities, FileStatus fileStatus) {
        entityManager.flush();
        final List<T> savedEntities = new ArrayList<T>(entities.size());
        int i = 0;
        for (T t : entities) {
            entityManager.persist(t);
            savedEntities.add(t);
            i++;
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        if(fileStatus != null) {
            fileStatusRepository.save(fileStatus);
        }
        return new AsyncResult<String>("DONE");
    }
}
