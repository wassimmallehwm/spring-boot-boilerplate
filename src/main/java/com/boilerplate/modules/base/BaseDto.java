package com.boilerplate.modules.base;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isEnabled;
}
