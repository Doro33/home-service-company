package ir.maktab.homeservicecompany.models.comment.service;

import ir.maktab.homeservicecompany.models.comment.dto.CommentDTO;
import ir.maktab.homeservicecompany.models.request.entity.Request;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.comment.entity.Comment;

public interface CommentService extends BaseService<Comment> {
    void addComment(CommentDTO commentDTO);
    Boolean existsByRequest(Request request);
}