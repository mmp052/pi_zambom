package insper.pi_zambom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import insper.pi_zambom.dto.CpfMembroDTO;
import insper.pi_zambom.dto.ProjetoDTO;
import insper.pi_zambom.model.Projeto;
import insper.pi_zambom.model.StatusProjeto;
import insper.pi_zambom.service.ProjetoService;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<Projeto> cadastrarProjeto(@RequestBody ProjetoDTO projetoDTO) {
        Projeto projeto = projetoService.cadastrarProjeto(projetoDTO);
        return ResponseEntity.ok(projeto);
    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listarProjetos(@RequestParam(required = false) StatusProjeto status) {
        List<Projeto> projetos = projetoService.listarProjetos(status);
        return ResponseEntity.ok(projetos);
    }

    @PostMapping("/{projetoId}/membros")
    public ResponseEntity<Projeto> adicionarMembroProjeto(@PathVariable String projetoId, @RequestBody CpfMembroDTO cpfMembroDTO) {
        Projeto projeto = projetoService.adicionarMembroProjeto(projetoId, cpfMembroDTO.getCpfMembro());
        return ResponseEntity.ok(projeto);
    }
}
