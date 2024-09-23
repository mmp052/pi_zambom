package insper.pi_zambom.dto;

import insper.pi_zambom.model.StatusProjeto;
import lombok.Data;

@Data
public class ProjetoDTO {
    private String nome;
    private String descricao;
    private StatusProjeto status;
    private String cpfGerente;
}
