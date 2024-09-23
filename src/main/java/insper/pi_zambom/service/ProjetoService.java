package insper.pi_zambom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import insper.pi_zambom.dto.ProjetoDTO;
import insper.pi_zambom.model.Projeto;
import insper.pi_zambom.model.StatusProjeto;
import insper.pi_zambom.repository.ProjetoRepository;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Projeto cadastrarProjeto(ProjetoDTO projetoDTO) {
        if (!verificarUsuario(projetoDTO.getCpfGerente())) {
            throw new RuntimeException("Gerente não encontrado");
        }

        Projeto projeto = new Projeto();
        projeto.setNome(projetoDTO.getNome());
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setStatus(projetoDTO.getStatus());
        projeto.setCpfGerente(projetoDTO.getCpfGerente());

        return projetoRepository.save(projeto);
    }

    public List<Projeto> listarProjetos(StatusProjeto status) {
        if (status != null) {
            return projetoRepository.findByStatus(status);
        }
        return projetoRepository.findAll();
    }

    public Projeto adicionarMembroProjeto(String projetoId, String cpfMembro) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        if (projeto.getStatus() == StatusProjeto.FINALIZADO) {
            throw new RuntimeException("Não é possível adicionar membros a um projeto finalizado");
        }

        if (!verificarUsuario(cpfMembro)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        projeto.getMembros().add(cpfMembro);
        return projetoRepository.save(projeto);
    }

    private boolean verificarUsuario(String cpf) {
        String url = "http://184.72.80.215:8080/usuario/" + cpf;
        try {
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
