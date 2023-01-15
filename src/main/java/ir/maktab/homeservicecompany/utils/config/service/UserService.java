package ir.maktab.homeservicecompany.utils.config.service;

import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final AdminService adminSer;
    private final WorkerService workerSer;
    private final ClientService clientSer;



    public UserService(AdminService adminSer, WorkerService workerSer, ClientService clientSer) {
        this.adminSer = adminSer;
        this.workerSer = workerSer;
        this.clientSer = clientSer;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (adminSer.findByEmail(email).isPresent())
            return adminSer.findByEmail(email).get();
        else if (workerSer.findByEmail(email).isPresent())
            return workerSer.findByEmail(email).get();
        else if (clientSer.findByEmail(email).isPresent())
            return clientSer.findByEmail(email).get();
        else throw new IllegalArgumentException("invalid inout");
    }
}
