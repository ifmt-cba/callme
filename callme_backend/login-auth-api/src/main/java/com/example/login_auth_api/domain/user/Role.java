package com.example.login_auth_api.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id") // Corrigido: nome da coluna em minúsculo
    private long roleId;

    @Column(name = "name") // Adicionado para garantir mapeamento correto
    private String name;

    public long getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public enum Values {
        RT(2L),
        ADMIN(1L);

        private final long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
