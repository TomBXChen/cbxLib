package com.taohdao.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/20.
 */

public class User implements Serializable {

    private static final long serialVersionUID = 509995125141315182L;


    /**
     * id : 8
     * name : null
     * mobile : 15917585757
     * sex : null
     * photo : null
     * motto : null
     * token : 07b777353852bfd77310c1071b33110b
     * type : 0
     * devices : android
     * auth : null
     * state : null
     * msgstate : null
     */

    public int id;
    public String name;
    public String mobile;
    public String sex;
    public String photo;
    public String motto;
    public String token;
    public String type;
    public String devices;
    public String auth;
    public String state;//商家资料审核 0待提交 1待审核 2审核通过 3不通过
    public String msgstate;
    public String hxuser;
    public String ispay;
    public String storeid;
    public boolean mdr;//免打扰
}
