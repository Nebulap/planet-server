package com.kai.planet.common.domain.entity.user;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;

/**
 *
 * @since 2024/11/12 23:16
 * @author 29002
 * @version 1.0.0
 */

@Table("t_test")
public class Test1 {
    @Id(keyType = KeyType.Auto)
    private Long id;
    private Integer test;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }
}
