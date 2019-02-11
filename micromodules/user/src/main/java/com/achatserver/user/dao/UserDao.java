package com.achatserver.user.dao;

import com.achatserver.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaSpecificationExecutor<User>, JpaRepository<User, String> {

    public User findByMobile(String mobile);

}
