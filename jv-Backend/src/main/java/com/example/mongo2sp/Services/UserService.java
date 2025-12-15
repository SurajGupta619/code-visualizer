package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.UserRepository;
import com.example.mongo2sp.dto.UserDto.UserResdto;
import com.example.mongo2sp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor

public class UserService {
    @Autowired
    UserRepository userRepository;

    public User ins(User user)
    {

        return userRepository.save(user);
    }

    public List<UserResdto> find(){
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> new UserResdto(user.getId(),user.getUsername(),user.getName())).collect(Collectors.toList());
    }

    public UserResdto findby_id(String id){
        if(userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            return new UserResdto(user.getId(), user.getUsername(), user.getName());
        }
        return null;
    }

    public void del(){
        userRepository.deleteAll();
    }

    public Optional<User> find_id(String id){
        return userRepository.findById(id);
    }

    public List<User> delete_id(String id){
        userRepository.deleteById(id);
        return userRepository.findAll();
    }

}
