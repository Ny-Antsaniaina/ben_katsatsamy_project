package org.example.projectface.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Student {
    private int std;
    private Face face;
    private User user;
}
