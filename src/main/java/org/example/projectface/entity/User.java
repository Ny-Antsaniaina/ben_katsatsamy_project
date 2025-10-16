package org.example.projectface.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private Role role;
}
