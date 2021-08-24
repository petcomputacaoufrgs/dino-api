package br.ufrgs.inf.pet.dinoapi.repository.report;

import br.ufrgs.inf.pet.dinoapi.entity.report.Report;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
    @Query("SELECT n FROM Report n WHERE n.id = :id AND n.user.id = :userId")
    Optional<Report> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT n FROM Report n WHERE n.id IN :ids")
    List<Report> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT n FROM Report n WHERE n.id IN :ids AND n.user.id = :userId")
    List<Report> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Query("SELECT n FROM Report n WHERE n.id NOT IN :ids AND n.user.id = :userId")
    List<Report> findAllByUserIdExcludingIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    @Query("SELECT n FROM Report n WHERE n.id NOT IN :ids")
    List<Report> findAllExcludingIds(@Param("ids") List<Long> ids);

    @Query("SELECT n FROM Report n WHERE n.user.id = :userId")
    List<Report> findAllByUserId(@Param("userId")  Long userId);
}
