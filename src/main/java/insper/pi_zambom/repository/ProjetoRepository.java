package insper.pi_zambom.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import insper.pi_zambom.model.Projeto;
import insper.pi_zambom.model.StatusProjeto;

public interface ProjetoRepository extends MongoRepository<Projeto, String> {
    List<Projeto> findByStatus(StatusProjeto status);
}
