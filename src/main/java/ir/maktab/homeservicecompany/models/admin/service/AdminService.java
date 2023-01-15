package ir.maktab.homeservicecompany.models.admin.service;

import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

import java.util.Optional;

public interface AdminService extends BaseService<Admin> {
    Optional<Admin> findByEmail(String email);
}
