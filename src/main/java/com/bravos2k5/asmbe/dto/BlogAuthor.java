package com.bravos2k5.asmbe.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.bravos2k5.asmbe.model.User}
 */
public record BlogAuthor(String name, String avatar) implements Serializable {
}