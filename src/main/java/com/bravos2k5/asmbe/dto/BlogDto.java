package com.bravos2k5.asmbe.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.bravos2k5.asmbe.model.Blog}
 */
public record BlogDto(UUID id, String title, String excerpt, String image) implements Serializable {
}