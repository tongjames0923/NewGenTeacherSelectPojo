package tbs.pojo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tbs.dao.MasterRelationDao;

import javax.annotation.Resource;

@SpringBootTest
class PojoApplicationTests {


    @Resource
    MasterRelationDao dao;
    @Test
    void contextLoads() {
        MasterRelation relation=new MasterRelation("10013",11);
        dao.insert(relation);
    }

}
