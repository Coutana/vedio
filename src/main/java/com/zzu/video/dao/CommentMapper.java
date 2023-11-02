package com.zzu.video.dao;

import com.zzu.video.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    int insertComment(Comment comment);
    int selectCountByVideoId(int videoId);
    Comment selectById(int commentId);
    List<Comment> selectCommentsByVideoId(int videoId,int offset, int limit);
}
