package com.laughtale.usermanagement.model.entity;

import com.laughtale.usermanagement.constants.ColumnName;
import com.laughtale.usermanagement.constants.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = TableName.ROLE)
public class RoleEntity {

    @Id
    @Column(name = ColumnName.ROLE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = ColumnName.ROLE_NAME, unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "roles")
    private Collection<UserEntity> users;
}
