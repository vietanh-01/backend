package com.web.service;

import com.web.entity.Favorite;
import com.web.entity.RealEstate;
import com.web.entity.User;
import com.web.enums.Status;
import com.web.exception.MessageException;
import com.web.repository.FavoriteRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    UserUtils userUtils;

    public String save(Long bdsId){
        User user = userUtils.getUserWithAuthority();
        Optional<Favorite> fa = favoriteRepository.findByUserAndBds(user.getId(), bdsId);
        if(fa.isPresent()){
            return "Tin bất động sản đã được thêm";
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        RealEstate realEstate = new RealEstate();
        realEstate.setId(bdsId);
        favorite.setRealEstate(realEstate);
        favoriteRepository.save(favorite);
        return "Lưu tin thành công";
    }

    public List<Favorite> myFavorite(){
        User user = userUtils.getUserWithAuthority();
        return favoriteRepository.findByUser(user.getId(), Status.DANG_HIEN_THI);
    }

    public void delete(Long id){
        Favorite favorite = favoriteRepository.findById(id).get();
        if(favorite.getUser().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("Không đủ quyền");
        }
        favoriteRepository.deleteById(id);
    }
}
