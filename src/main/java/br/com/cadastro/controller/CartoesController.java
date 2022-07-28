package br.com.cadastro.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cadastro.entity.Cartao;
import br.com.cadastro.models.CartaoRequest;
import br.com.cadastro.models.RecargaRequest;
import br.com.cadastro.services.CartaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "Cartoes")
@RestController
@CrossOrigin
@RequestMapping("/v1")
public class CartoesController {


	@Autowired
	private CartaoService cartaoService;

	private static final Logger logger = LogManager.getLogger(CartoesController.class);

	@ApiOperation(value = "Inclui Cartao")
	@PostMapping(path = "/cartoes" , produces = {"application/json"})
	public ResponseEntity<Cartao> inclusao(@Valid @RequestBody CartaoRequest cartaoRequest) {
		logger.info("Iniciando a Inclusao do Cliente");
		return cartaoService.inclusao(cartaoRequest );
	}


	@ApiOperation(value = "Obter Saldo Cartao")
	@GetMapping(path = "/cartoes/consultarSaldoCartao" , produces = {"application/json"})
	public ResponseEntity<Cartao> consultarSaldoCartao(@RequestParam("numeroCartao") String numeroCartao) {
		logger.info("Iniciando consultar Saldo Cartao");
		return cartaoService.consultarCartao(numeroCartao );
	}
	
	
	@ApiOperation(value = "Incluir Credito Cartao")
	@PostMapping(path = "/cartoes/incluirCreditoCartao" , produces = {"application/json"})
	public ResponseEntity<Cartao> incluirCreditoCartao(@Valid @RequestBody RecargaRequest recargaRequest) {
		logger.info("Iniciando incluirCreditoCartao");
		return cartaoService.incluirCreditoCartao(recargaRequest);
	}
	
	@ApiOperation(value = "Incluir Debito Cartao")
	@PostMapping(path = "/cartoes/debitarSaldoCartao" , produces = {"application/json"})
	public ResponseEntity<Cartao> debitarSaldoCartao(@Valid @RequestBody RecargaRequest recargaRequest) {
		logger.info("Iniciando debitarSaldoCartao");
		return cartaoService.debitarSaldoCartao(recargaRequest);
	}
}
