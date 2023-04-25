package tbs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tbs.pojo.BasicUser;
import tbs.repo.good.BatchSaveRepository;

@Repository
public interface BasicUserDao extends JpaRepository<BasicUser,String>, BatchSaveRepository<BasicUser> {
}
