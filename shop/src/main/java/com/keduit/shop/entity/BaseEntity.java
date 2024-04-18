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

  @CreatedBy
  @Column(updatable = false)
  private String createdBy;

  @LastModifiedBy
  private String modifiedBy;
}
