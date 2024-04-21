package com.keduit.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// 엔티티의 변경 사항을 감시하기 위한 엔티티 리스너를 지정 엔티티의 생성 및 수정 시간을 추적
@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 적용 대상
@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용. 자식클래스에게만 상속정보를 제공하기 위해 붙임
// 부모 클래스인 BaseTimeEntity가 테이블을 생성하지 않고,
// 자식 클래스에게 매핑 정보를 제공한다는 것을 나타냄 즉, 부모 클래스의 필드들은 자식 엔티티 클래스에서 상속되어 사용
@Setter
@Getter
public  abstract class BaseTimeEntity {

    // 엔티티가 생성될 때 자동으로 현재 시간을 설정하는 필드
    // 이 필드는 엔티티가 처음 생성될 때만 값을 갖습니다.
    @CreationTimestamp
    // updatable = false 속성은 이 필드가 엔티티가 수정될 때 변경되지 않도록 합니다.
    @Column(updatable = false)
    private LocalDateTime regTime;

    // 엔티티가 수정될 때마다 자동으로 현재 시간을 설정하는 필드
    @LastModifiedDate
    private LocalDateTime updateTime;
}
