package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "role_Id")
    private long roleId;

    public long getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }


    //Criando as Holes
    private String name;

    public enum Values {

        RT(2L),
        ADMIN(1L);

        long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }
    }
    public long getRoleid(){

        return roleId;
    }

}
