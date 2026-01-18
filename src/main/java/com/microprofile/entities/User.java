package com.microprofile.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microprofile.enums.E_ROLE;
import com.microprofile.enums.E_SEX;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * user entity
 */
@Entity
@Getter()
@Setter
@Table(
    name = "`user`"
)
@UserDefinition
@Schema
public class User extends PanacheEntityBase {
    @Transient
    @JsonIgnore
    public boolean isFullData = false;

    /**
     * 设置后用于获取用户真实信息
     * @param fullData boolean
     */
    public void setGetFullData(boolean fullData) {
        this.isFullData = fullData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    @Schema(required = true)
    public UUID id;

    @Column(unique = true, length = 32, nullable = false, comment = "user name")
    @Size(min = 2, max = 32)
    @Schema(description = "username", required = true)
    @Username
    public String username;

    @Roles()
    @Column(nullable = false, comment = "user role")
    @Schema(description = "user role", required = true)
    public String role = E_ROLE.USER.toString();

    @Column(length = 128, comment = "website")
    @Size(min = 2, max = 128)
    @Schema(description = "website url", minLength = 2, maxLength = 128)
    public String website;

    @Column(length = 128, comment = "social")
    @Size(min = 2, max = 128)
    @Schema(description = "social url", minLength = 2, maxLength = 128)
    public String social;

    @Column(length = 42, comment = "User avatar")
    @Size(max = 42)
    @Schema(description = "avatar")
    public String avatar;

    @Enumerated(value = EnumType.STRING)
    @Schema(description = "sex")
    public E_SEX sex;

    @Column(length = 60, nullable = false, comment = "password")
    @Password
    @JsonIgnore
    @Schema(description = "password")
    public String password;

    @Column(unique = true, length = 32)
    @Size(min = 4, max = 11)
    @Schema(description = "phoneNumber", required = true)
    public String phoneNumber;

    public String getPhoneNumber() {
        if(this.isFullData) {
            return this.phoneNumber;
        } else {
            return String.format("%s****%s", this.phoneNumber.substring(0, 3), this.phoneNumber.substring(7, 11));
        }
    }

    @Column(unique = true, length = 32)
    @Email
    @JsonIgnore
    @Size(min = 6, max = 32)
    @NaturalId
    @Schema(description = "email")
    public String email;

    @CreationTimestamp
    @Schema(description = "when created", required = true)
    public Instant whenCreated;

    @UpdateTimestamp
    @Schema(description = "when modified", required = true)
    public Instant whenModified;

    @SoftDelete
    public Instant whenDeleted;
}
