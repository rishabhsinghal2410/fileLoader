package com.fileloader.repository;

import com.fileloader.models.FileStatus;
import org.springframework.data.repository.CrudRepository;

public interface FileStatusRepository extends CrudRepository<FileStatus, Long> {
}
