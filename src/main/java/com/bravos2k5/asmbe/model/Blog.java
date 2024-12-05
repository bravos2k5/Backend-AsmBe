package com.bravos2k5.asmbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, columnDefinition = "nvarchar(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String content;

    private LocalDate createdDate = LocalDate.now();

    private String image;

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();

}
