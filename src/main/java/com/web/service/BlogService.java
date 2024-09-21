package com.web.service;

import com.web.dto.response.BlogResponse;
import com.web.entity.Blog;
import com.web.exception.MessageException;
import com.web.mapper.BlogMapper;
import com.web.repository.BlogRepository;
import com.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private BlogMapper blogMapper;

    public BlogResponse saveOrUpdate(Blog blog) {
        blog.setUser(userUtils.getUserWithAuthority());
        blog.setCreatedDate(new Date(System.currentTimeMillis()));
        Blog result = blogRepository.save(blog);
        return blogMapper.blogToResponse(result);
    }

    public void delete(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()){
            throw new MessageException("Blog not found");
        }
        blogRepository.delete(blog.get());
    }

    public Page<BlogResponse> findAll(Pageable pageable) {
        Page<Blog> page = blogRepository.findAll(pageable);
//        Page<BlogResponse> result = page.map(this::convertToResponse);
        Page<BlogResponse> result = page.map(blog-> blogMapper.blogToResponse(blog));
        return result;
    }

    public BlogResponse findById(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()){
            throw new MessageException("Blog not found");
        }
        return blogMapper.blogToResponse(blog.get());
    }
}
