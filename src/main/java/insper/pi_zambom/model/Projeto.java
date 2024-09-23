package insper.pi_zambom.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "projetos")
public class Projeto {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private StatusProjeto status;
    private String cpfGerente;
    private Set<String> membros = new HashSet<>();
}