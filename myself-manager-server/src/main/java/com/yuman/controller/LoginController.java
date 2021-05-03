package com.yuman.controller;

import com.yuman.entity.Account;
import com.yuman.model.response.BaseResponse;
import com.yuman.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class LoginController {

    @Autowired
    LoginRepository loginRepository;

    @GetMapping("/login")
    public BaseResponse login(String username, String password){
        Account account = new Account();
        account.setUsername(username);
        Example<Account> example = Example.of(account);
        Optional<Account> one = loginRepository.findOne(example);
        if(!one.isPresent()){
            return BaseResponse.error("用户不存在!");
        }
        account.setPassword(password);
        //创建实例
        example = Example.of(account);
        one = loginRepository.findOne(example);
        if(one.isPresent()){
            return BaseResponse.ok();
        }
        return BaseResponse.error("用户名或密码错误!");
    }
}
