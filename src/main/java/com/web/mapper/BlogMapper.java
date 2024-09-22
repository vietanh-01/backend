package com.web.mapper;

import com.web.dto.request.BlogRequest;
import com.web.dto.response.BlogResponse;
import com.web.dto.response.UserDto;
import com.web.dto.response.UserResponse;
import com.web.entity.Blog;
import com.web.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserMapper userMapper;

    public BlogResponse blogToResponse(Blog obj){
        BlogResponse response = mapper.map(obj, BlogResponse.class);
        if(obj.getUser() != null){
            response.setUser(userMapper.userToResponse(obj.getUser()));
        }
        return response;
    }

    public Blog requestToBlog(BlogRequest request){
        Blog blog = mapper.map(request, Blog.class);
        return blog;
    }

}
