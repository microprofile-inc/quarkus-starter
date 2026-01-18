package com.microprofile.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "sms_code")
@Schema
public class SmsCode extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    @Schema(required = true)
    public UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @Schema(description = "country code")
    public CountryCode countryCode;

    @Column(length = 11, nullable = false)
    @Schema(required = true)
    public String phoneNumber;

    @Column(length = 4, nullable = false)
    @Schema(required = true)
    public String code;

    @Column()
    @Schema(required = true, description = "是否有效, (使用过后变为无效)")
    public boolean effective = true;

    @CreationTimestamp
    @Schema(description = "when created", required = true)
    public Instant whenCreated;

    @UpdateTimestamp
    @Schema(description = "when modified", required = true)
    public Instant whenModified;

    @SoftDelete
    public Instant whenDeleted;
}
