package me.cyberproton.ocean.features.search;


import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.features.elasticsearch.TestSearchRepository;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    private final TestSearchRepository testSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @GetMapping
    public SearchResponse search(@Valid SearchQuery query) {
        System.out.println(query);
        return searchService.search(query);
    }

    @GetMapping("test")
    public Object test(@RequestParam Long id) {
        return null;
    }

    @PostMapping("refresh-indices")
    public void refreshIndices() {
        searchService.refreshIndices();
    }
}
