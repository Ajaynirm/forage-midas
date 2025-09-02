package com.jpmc.midascore.Controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/balance")
    public ResponseEntity<Balance> getBalance(@RequestParam Long userId) {
        UserRecord user = userRepo.findById(userId)
                .orElse(null);
        if(user==null){
            return ResponseEntity.ok(new Balance(0));
        }
        return ResponseEntity.ok(new Balance((float) user.getBalance()));
    }
}

