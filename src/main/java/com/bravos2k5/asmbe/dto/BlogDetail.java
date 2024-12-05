package com.bravos2k5.asmbe.dto;

import java.time.LocalDate;
import java.util.UUID;

public record BlogDetail(UUID id, String title, BlogAuthor author, LocalDate date, String image, String content) {
}
