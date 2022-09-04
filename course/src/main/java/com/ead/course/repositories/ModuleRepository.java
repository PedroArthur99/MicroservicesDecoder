package com.ead.course.repositories;

import com.ead.course.controllers.model.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

//    Esta anotação faz com que o atributo curso venha preenchido com a entidade completa
//    Isso não aconteceria já que definimos a relação com carregamento lento(FetchType.LAZY)
//    É uma excelente alternativa já que não da pra alternar entre EAGER e LAZY em tempo de execução
//    @EntityGraph(attributePaths = {"course"})
//    ModuleModel findByTitle(String string);

    @Query(value = "select * from tb_modules where course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);


    @Query(value = "select * from tb_modules where course_id = :courseId and module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId")UUID moduleId);

}
