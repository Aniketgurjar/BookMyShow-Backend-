package Services;

import Models.User;
import Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class UserServices {
    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = null;
        if (optionalUser.isEmpty()) {
            //Create the user object and save it to DB.
            user = new User();
            user.setName(name);
            user.setEmail(email);

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(password));

            user = userRepository.save(user);
        }

        return user;
    }

    public void signIn(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            //Redirect to SignUp.
        }

        User user = optionalUser.get();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            System.out.println("Login Successful");
        } else {
            System.out.println("Wrong password");
        }
    }

}
