package com.infobip.pmf.course.storage;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepo extends ListCrudRepository<UserEntity, Long> {
}
