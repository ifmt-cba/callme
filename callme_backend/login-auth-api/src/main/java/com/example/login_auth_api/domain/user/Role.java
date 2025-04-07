package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

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

    public enum values{

        RT(2L),
        ADMIN(1L);

        long roleId;

        values(long roleId) {
            this.roleId = roleId;
        }
    }
    public long getRoleid(){

        return roleId;
    }

}
