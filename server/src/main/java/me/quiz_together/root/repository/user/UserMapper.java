package me.quiz_together.root.repository.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.request.UserIdReq;
import me.quiz_together.root.model.user.User;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    User selectUserById(@Param("id")long id);

    List<User> getUserByIds(List<Long> ids);

    int updateUserProfile(long userId, String profileImageUrl);

    int deleteUserById(@Param("id")long id);

    User selectUserByName(@Param("name") String name);

    User login(UserIdReq userIdReq);
}
