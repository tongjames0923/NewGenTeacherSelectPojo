package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.pojo.BasicUser;
import tbs.pojo.Student;
import tbs.pojo.dto.StudentDetail;

import java.util.List;

@Mapper
public interface StudentDao {



    @Insert("insert into student(studentNo, grade, cla, phone) VALUES (#{studentNo},#{grade},#{cla},#{phone})")
    int saveStudent(Student item);

    @Select(StudentDetail.BASIC_DATA_SQL+"where bu.phone=#{phone}")
    StudentDetail findStudentByPhone(String phone);

    @Select(StudentDetail.BASIC_DATA_SQL+" where bu.departmentId=#{department} limit #{beg},#{end}")
    List<StudentDetail> findStudentByDepartment(int department,int beg,int end);

}
