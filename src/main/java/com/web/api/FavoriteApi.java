package com.web.api;
import com.web.entity.Favorite;
import com.web.entity.RealEstate;
import com.web.entity.User;
import com.web.enums.Status;
import com.web.exception.MessageException;
import com.web.service.FavoriteService;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorite")
@CrossOrigin("*")
public class FavoriteApi {

    @Autowired
    FavoriteService favoriteService;

    @PostMapping("/all/add")
    public ResponseEntity<?> addFavorite(@RequestParam("id") Long bdsId){
        String result = favoriteService.save(bdsId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/all/my-favorite")
    public ResponseEntity<?> findByUser(){
        List<Favorite> result = favoriteService.myFavorite();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/all/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Long id){
        favoriteService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
