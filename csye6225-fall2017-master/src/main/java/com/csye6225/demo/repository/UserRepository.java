/**
 * <chinmay keskar>, <001221409>, <keskar.c@husky.neu.edu>
 * <harshal neelkamal>, <001645951>, <neelkamal.h@husky.neu.edu>
 * <snigdha joshi>, <001602328>, <joshi.sn@husky.neu.edu>
 * <piyush sharma>, <001282198>, <sharma.pi@husky.neu.edu>
 **/

package com.csye6225.demo.repository;

import com.csye6225.demo.bean.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete


@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Integer>
{
    public User findByUsername(String username);
}