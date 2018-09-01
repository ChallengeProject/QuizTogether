package me.quiz_together.root.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInventoryMapper {

    void insertUserInventory(@Param("userId") Long userId);

    int updateUserHeartCount(@Param("userId") Long userId, @Param("heartCount") int heartCount);
}
