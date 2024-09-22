package com.web.api;

import com.web.dto.response.BlogResponse;
import com.web.entity.Juridical;
import com.web.service.BlogService;
import com.web.service.JuridicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/juridical")
@CrossOrigin
public class JuridicalApi {

    @Autowired
    private JuridicalService juridicalService;

    @GetMapping("/public/findAll")
    public ResponseEntity<?> findAll(){
        List<Juridical> result = juridicalService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
