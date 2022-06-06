package com.laughtale.usermanagement.model.entity;

import com.laughtale.usermanagement.constants.ColumnName;
import com.laughtale.usermanagement.constants.TableName;
import lombok.Getter;
import lombok.Setter;
import org.dozer.Mapping;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = TableName.USER)
public class UserEntity {

    @Id
    @Column(name = ColumnName.USER_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = ColumnName.USER_NAME, unique = true, nullable = false)
    private String userName;

    @Column(name = ColumnName.EMAIL, unique = true, nullable = false)
    private String email;

    @Column(name = ColumnName.PASSWORD, nullable = false)
    @Mapping("this")
    private String password;

    @Column(name = ColumnName.PROFILE_PICTURE)
    private String profilePicture;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = ColumnName.LAST_ACTIVE)
    private Date lastActive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = ColumnName.CREATE_DATE, nullable = false, updatable = false)
    @CreationTimestamp
    private Date createDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = TableName.USER_ROLE, joinColumns = @JoinColumn(name = ColumnName.USER_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnName.ROLE_ID))
    private Set<RoleEntity> roles;
}
