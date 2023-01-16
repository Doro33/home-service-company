package ir.maktab.homeservicecompany.utils.service;

import ir.maktab.homeservicecompany.models.admin.entity.Admin;
import ir.maktab.homeservicecompany.models.admin.service.AdminService;
import ir.maktab.homeservicecompany.models.client.entity.Client;
import ir.maktab.homeservicecompany.models.client.service.ClientService;
import ir.maktab.homeservicecompany.models.worker.entity.Worker;
import ir.maktab.homeservicecompany.models.worker.service.WorkerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final AdminService adminSer;
    private final WorkerService workerSer;
    private final ClientService clientSer;

    public UserService(@Lazy AdminService adminSer, @Lazy WorkerService workerSer, @Lazy ClientService clientSer) {
        this.adminSer = adminSer;
        this.workerSer = workerSer;
        this.clientSer = clientSer;
    }

    public boolean signUpPermit(String email) {
        return !(adminSer.findByEmail(email).isPresent() ||
                workerSer.findByEmail(email).isPresent() ||
                clientSer.findByEmail(email).isPresent());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(email);
        Optional<Admin> admin = adminSer.findByEmail(email);
        Optional<Worker> worker = workerSer.findByEmail(email);
        Optional<Client> client = clientSer.findByEmail(email);
        System.out.println(1111111);
        System.out.println(admin.orElse(null));
        System.out.println(worker.orElse(null));
        System.out.println(client.orElse(null));
        if (admin.isPresent())
            return admin.get();
        if (worker.isPresent())
            return worker.get();
        if (client.isPresent())
            return client.get();

        throw new UsernameNotFoundException("invalid input");
    }
}
