package com.yuman.repository;

import com.yuman.entity.Account;
import com.yuman.entity.ForgetCurve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LoginRepository extends JpaRepository<Account, String> {

}
