package com.example.disearch.controller;

import com.example.disearch.entity.Tag;
import com.example.disearch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.findAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagService.findTagById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<List<Tag>> createTags(@RequestBody Map<String, List<String>> tagMap) {
        if (tagMap != null && tagMap.containsKey("tag")) {
            List<String> tagNames = tagMap.get("tag");
            List<Tag> createdOrUpdatedTags = tagService.createOrUpdateTags(tagNames);
            return ResponseEntity.ok(createdOrUpdatedTags);
        } else {
            // 요청 본문에 "tags" 키가 존재하지 않는 경우, 잘못된 요청으로 간주
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tagDetails) {
        return tagService.findTagById(id)
                .map(existingTag -> {
                    existingTag.setName(tagDetails.getName());
                    Tag updatedTag = tagService.saveTag(existingTag);
                    return ResponseEntity.ok(updatedTag);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        return tagService.findTagById(id)
                .map(tag -> {
                    tagService.deleteTag(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}