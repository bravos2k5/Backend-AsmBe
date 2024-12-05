package com.bravos2k5.asmbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "blogId")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String content;

    @CreatedDate
    private LocalDate createdDate = LocalDate.now();

}
