package tbs.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tbs.pojo.StudentDetail;
import tbs.repo.good.BatchSaveRepository;

import java.util.List;

public interface StudentDao extends JpaRepository<StudentDetail,Integer>, BatchSaveRepository<StudentDetail> {

    @Cacheable(key = "#phone",value = "student_")
    @Query("select u from StudentDetail u where u.basic.phone=?1 and u.basic.password=?2")
    StudentDetail studentLoginWithPhone(String phone,String password);

    List<StudentDetail> findAllByGrade(int grade);

}
