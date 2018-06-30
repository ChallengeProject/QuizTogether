package me.quiz_together.root.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.quiz_together.root.model.request.UserLoginRequest;
import me.quiz_together.root.model.user.User;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    User selectUserById(@Param("id")long id);

    int updateUserProfile(long userId, String profileImageUrl);

    int deleteUserById(@Param("id")long id);

    int selectUserByName(@Param("name") String name);

    User login(UserLoginRequest userLoginRequest);
}
