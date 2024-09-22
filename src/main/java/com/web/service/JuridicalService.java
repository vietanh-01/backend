package com.web.service;

import com.web.entity.Category;
import com.web.entity.Juridical;
import com.web.repository.CategoryRepository;
import com.web.repository.JuridicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JuridicalService {

    @Autowired
    private JuridicalRepository juridicalRepository;

    public List<Juridical> findAll() {
        List<Juridical> list = juridicalRepository.findAll();
        return list;
    }
}
