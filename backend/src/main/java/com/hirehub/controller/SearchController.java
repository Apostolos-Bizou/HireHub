package com.hirehub.controller;

import com.hirehub.dto.request.SearchCandidatesRequest;
import com.hirehub.dto.response.SearchResultResponse;
import com.hirehub.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/candidates")
    @PreAuthorize("hasAnyRole('SHIPOWNER', 'ADMIN')")
    public ResponseEntity<SearchResultResponse> searchCandidates(
            @RequestBody SearchCandidatesRequest request) {
        return ResponseEntity.ok(searchService.searchCandidates(request));
    }
}
