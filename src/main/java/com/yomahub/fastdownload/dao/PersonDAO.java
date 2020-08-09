package com.yomahub.fastdownload.dao;

import com.yomahub.fastdownload.po.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDAO extends JpaRepository<Person, Long> {
}
