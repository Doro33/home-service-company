package ir.maktab.homeservicecompany.models.comment.service;

import ir.maktab.homeservicecompany.utils.base.service.BaseService;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.comment.entity.Comment;

public interface CommentService extends BaseService<Comment> {
    Comment addComment(Client client,Comment comment);
}