package tbs.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "basicuser",indexes = {@Index(name = "login_index",columnList = "phone,password")})
public class BasicUser implements Serializable {
    @Id
    @Column(name = "uid",unique = true,length = 50,nullable = false)
    private String uid;
    @Column(name = "phone",unique = true,length = 20,nullable = false)
    String phone;
    @Column(name = "password",length = 50,nullable = false)
    private String password;
    @Column(name = "name",length = 20,nullable = true)
    private String name;

    @Column(name = "userlevel",length =20,nullable = false)
    private String identity=USERLEVEL_STUDENT;

    @Column(name = "schoolPart",length = 20,nullable = false)
    private String schoolPart;

    public static final String USERLEVEL_STUDENT="学生",USERLEVEL_TEACHER="教师",USERLEVEL_ADMIN="管理员";

    public static BasicUser newUser(String phone,String password,String userLevel)
    {
        BasicUser basicUser=new BasicUser();
        basicUser.setPhone(phone);
        basicUser.setPassword(password);
        basicUser.setIdentity(userLevel);
        return basicUser;
    }
    public static BasicUser newUser(String phone,String password)
    {
        return newUser(phone,password,USERLEVEL_STUDENT);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public BasicUser() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BasicUser{" +
                "uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }
}
