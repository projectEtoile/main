package com.keduit.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value={AuditingEntityListener.class})  // Auditing 적용 대상
@MappedSuperclass  // 공통 매핑 정보가 필요할 때 사용. 자식클래스에게  상속정보를 제공하기위해 붙임.
@Setter
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false) // 최초 등록일자는 업데이트되선 안됌.
    private LocalDateTime regTime;

    @LastModifiedDate
    private LocalDateTime updateTime;
}
