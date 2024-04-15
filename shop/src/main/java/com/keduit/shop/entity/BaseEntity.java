package com.keduit.shop.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;


@EntityListeners(value={AuditingEntityListener.class})  // Auditing기능을 적용
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
    // 시간 2 작성or수정자 2 다 받고 싶으면 이거
    // 시간 2 만 받고싶으면 baseTimeEntity

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
