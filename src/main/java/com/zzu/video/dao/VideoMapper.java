package com.zzu.video.dao;

import com.zzu.video.entity.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface VideoMapper {
    Video selectById(int id);
    List<Video> selectByUserId(int userId);
    int insertVideo(Video video);
    int selectUserIdByVideoId(int videoId);
    List<Video> selectByTag(String tag,int offset,int limit);

}
