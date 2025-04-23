package com.example.login_auth_api.dto;

import java.util.List;

public record FeedDto(List<FeedItemDto> feedItens,

                      int page,
                      int pageSize,

                      int totalPages,

                      long totalElements) {
}
