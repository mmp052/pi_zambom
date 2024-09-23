package insper.pi_zambom.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import insper.pi_zambom.dto.ProjetoDTO;
import insper.pi_zambom.model.Projeto;
import insper.pi_zambom.model.StatusProjeto;
import insper.pi_zambom.repository.ProjetoRepository;

class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProjetoService projetoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarProjeto_usuarioValido() {
        ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setNome("Projeto Teste");
        projetoDTO.setDescricao("Descrição do projeto de teste");
        projetoDTO.setStatus(StatusProjeto.PLANEJAMENTO);
        projetoDTO.setCpfGerente("123");

        Projeto projeto = new Projeto();
        projeto.setNome(projetoDTO.getNome());
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setStatus(projetoDTO.getStatus());
        projeto.setCpfGerente(projetoDTO.getCpfGerente());

        // Simulando uma resposta HTTP 200 para um usuário válido
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Usuário encontrado", HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);
        Mockito.when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);

        Projeto savedProjeto = projetoService.cadastrarProjeto(projetoDTO);

        Assertions.assertNotNull(savedProjeto);
        Assertions.assertEquals(projetoDTO.getNome(), savedProjeto.getNome());
        Assertions.assertEquals(projetoDTO.getDescricao(), savedProjeto.getDescricao());
        Assertions.assertEquals(projetoDTO.getStatus(), savedProjeto.getStatus());
        Assertions.assertEquals(projetoDTO.getCpfGerente(), savedProjeto.getCpfGerente());
    }

    @Test
    void testCadastrarProjeto_usuarioInvalido() {
        ProjetoDTO projetoDTO = new ProjetoDTO();
        projetoDTO.setCpfGerente("456");

        // Simulando uma resposta HTTP 404 para um usuário inválido
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        Assertions.assertThrows(RuntimeException.class, () -> projetoService.cadastrarProjeto(projetoDTO));
    }

    @Test
    void testListarProjetos_comFiltro() {
        Projeto projeto1 = new Projeto();
        projeto1.setStatus(StatusProjeto.PLANEJAMENTO);
        Projeto projeto2 = new Projeto();
        projeto2.setStatus(StatusProjeto.EXECUCAO);
        Projeto projeto3 = new Projeto();
        projeto3.setStatus(StatusProjeto.FINALIZADO);

        Mockito.when(projetoRepository.findByStatus(StatusProjeto.PLANEJAMENTO)).thenReturn(Arrays.asList(projeto1));

        List<Projeto> projetos = projetoService.listarProjetos(StatusProjeto.PLANEJAMENTO);

        Assertions.assertEquals(1, projetos.size());
        Assertions.assertEquals(StatusProjeto.PLANEJAMENTO, projetos.get(0).getStatus());
    }

    @Test
    void testListarProjetos_semFiltro() {
        Projeto projeto1 = new Projeto();
        projeto1.setStatus(StatusProjeto.PLANEJAMENTO);
        Projeto projeto2 = new Projeto();
        projeto2.setStatus(StatusProjeto.EXECUCAO);
        Projeto projeto3 = new Projeto();
        projeto3.setStatus(StatusProjeto.FINALIZADO);

        Mockito.when(projetoRepository.findAll()).thenReturn(Arrays.asList(projeto1, projeto2, projeto3));

        List<Projeto> projetos = projetoService.listarProjetos(null);

        Assertions.assertEquals(3, projetos.size());
    }

    @Test
    void testAdicionarMembroProjeto_projetoEncontrado() {
        String projetoId = "123";
        String cpfMembro = "456";

        Projeto projeto = new Projeto();
        projeto.setStatus(StatusProjeto.PLANEJAMENTO);

        Mockito.when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Usuário encontrado", HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);
        Mockito.when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);

        Projeto savedProjeto = projetoService.adicionarMembroProjeto(projetoId, cpfMembro);

        Assertions.assertNotNull(savedProjeto);
        Assertions.assertTrue(savedProjeto.getMembros().contains(cpfMembro));
    }

    @Test
    void testAdicionarMembroProjeto_projetoFinalizado() {
        String projetoId = "123";
        String cpfMembro = "456";

        Projeto projeto = new Projeto();
        projeto.setStatus(StatusProjeto.FINALIZADO);

        Mockito.when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

        Assertions.assertThrows(RuntimeException.class, () -> projetoService.adicionarMembroProjeto(projetoId, cpfMembro));
    }

    @Test
    void testAdicionarMembroProjeto_usuarioInvalido() {
        String projetoId = "123";
        String cpfMembro = "456";

        Projeto projeto = new Projeto();
        projeto.setStatus(StatusProjeto.PLANEJAMENTO);

        Mockito.when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        Assertions.assertThrows(RuntimeException.class, () -> projetoService.adicionarMembroProjeto(projetoId, cpfMembro));
    }

    @Test
    void testVerificarUsuario_usuarioEncontrado() {
        String cpf = "123";

        // Simulando uma resposta HTTP 200 (OK)
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Usuário encontrado", HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        boolean usuarioEncontrado = projetoService.verificarUsuario(cpf);

        Assertions.assertTrue(usuarioEncontrado, "O usuário deveria ser encontrado.");
    }

    @Test
    void testVerificarUsuario_usuarioNaoEncontrado() {
        String cpf = "456";

        // Simulando uma resposta HTTP 404 (Not Found)
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        boolean usuarioEncontrado = projetoService.verificarUsuario(cpf);

        Assertions.assertFalse(usuarioEncontrado, "O usuário não deveria ser encontrado.");
    }

    @Test
    void testVerificarUsuario_erroNaRequisicao() {
        String cpf = "789";

        // Simulando uma exceção na requisição
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class)))
                .thenThrow(new RuntimeException("Erro na requisição"));

        boolean usuarioEncontrado = projetoService.verificarUsuario(cpf);

        Assertions.assertFalse(usuarioEncontrado, "Deveria retornar falso em caso de exceção.");
    }
}
