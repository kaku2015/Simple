package com.skr.simple.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author hyw
 * @since 2016/11/22
 */
@Entity
public class Sample {
    @Id
    private Long id;

    @Generated(hash = 724782928)
    public Sample(Long id) {
        this.id = id;
    }

    @Generated(hash = 976859954)
    public Sample() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
