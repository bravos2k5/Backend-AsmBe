package com.bravos2k5.asmbe.dto;

import java.time.LocalDate;

public record CommentDto(String name, LocalDate commentDate , String avatar, String content) {

}
