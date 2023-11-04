package com.zzu.video.service;

import com.zzu.video.dao.CommentMapper;
import com.zzu.video.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 注释
 *
 * @author Coutana
 * @since 2.9.0
 */
@Service
public class CommentService {
    private final CommentMapper commentMapper;
    @Autowired
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }
    public int findCommentCountByVideoId(int videoId) {
        return commentMapper.selectCountByVideoId(videoId);
    }
    public List<Comment> findCommentByVideoId(int videoId,int offset,int limit) {
        return commentMapper.selectCommentsByVideoId(videoId,offset,limit);
    }

    public Comment addComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
        return comment;
    }

    public void like(int commentId,int userId) {

    }
    public void getCommentLikeCount(int commentId) {

    }
}
