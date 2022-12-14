package org.vuestock.domain.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class EntityBase(
    @CreatedDate
    open var createdDate: LocalDateTime = LocalDateTime.MIN,

    @LastModifiedDate
    open var lastUpdatedDate: LocalDateTime = LocalDateTime.MIN
)