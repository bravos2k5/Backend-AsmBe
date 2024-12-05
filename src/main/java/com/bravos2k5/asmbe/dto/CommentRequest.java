package com.bravos2k5.asmbe.dto;

import java.util.UUID;

public record CommentRequest(UUID blogId, String content) {
}
