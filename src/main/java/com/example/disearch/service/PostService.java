package com.example.disearch.service;

import com.example.disearch.entity.Post;
import com.example.disearch.entity.Tag;
import com.example.disearch.repository.PostRepository;
import com.example.disearch.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Post createPost(Long serverId, String serverName, String category, List<String> tagNames, String content) {
        Post post = new Post();
        post.setServerId(serverId);
        post.setServerName(serverName);
        post.setCategory(category);
        post.setContent(content);

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .map(existingTag -> {
                        existingTag.setCount(existingTag.getCount() + 1);
                        return existingTag;
                    })
                    .orElseGet(() -> {
                        Tag newTag = new Tag(tagName);
                        newTag.setCount(1);
                        return newTag;
                    });
            tags.add(tag);
            tagRepository.save(tag);
        }
        post.setTags(tags);
        return postRepository.save(post);
    }

    public List<Post> getPosts(String tag, String category) {
        if (tag != null) {
            return postRepository.findAllByTagsName(tag);
        } else if (category != null) {
            return postRepository.findAllByCategory(category);
        } else {
            return postRepository.findAll();
        }
    }


}
