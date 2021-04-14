package softuni.workshop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import softuni.workshop.data.service.UserServiceModel;
import softuni.workshop.data.binding.UserRegisterBindingModel;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserRegisterBindingModel userRegisterBindingModel);
}
