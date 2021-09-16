package com.hzy.mapper;
/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/30 16:59
 * Description: None
 */

import com.hzy.model.User;
import com.hzy.param.PageParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserMapper {
//    public List<User> findAll(PageParam pageParam);
//    public int getTotalCount();
    public List<User> findByLikeNameAndUserType(Map<String, Object> map);
    public int getTotalCountCondition(Map<String, Object> map);
    public Optional<User> findById(Long id);
    public User findByUserName(String userName);
    public User findByEmail(String email);
    public User findByUserNameOrEmail(String userName, String email);
    public List<String> findUserTypes();
    public int insertUser(User user);
    public int updateUser(User user);
    public int deleteById(Long id);
}
