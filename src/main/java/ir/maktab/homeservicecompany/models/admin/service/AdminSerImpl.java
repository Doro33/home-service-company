package ir.maktab.homeservicecompany.models.admin.service;

import ir.maktab.homeservicecompany.models.admin.dao.AdminDao;
import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AdminSerImpl extends BaseServiceImpl<Admin, AdminDao> implements AdminService{

    public AdminSerImpl(AdminDao repository) {
        super(repository);
    }
}