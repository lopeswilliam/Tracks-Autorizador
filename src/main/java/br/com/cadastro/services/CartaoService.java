package br.com.cadastro.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.cadastro.entity.Cartao;
import br.com.cadastro.entity.Credits;
import br.com.cadastro.entity.Secrety;
import br.com.cadastro.models.CartaoRequest;
import br.com.cadastro.models.RecargaRequest;
import br.com.cadastro.repositories.CartaoRepository;
import br.com.cadastro.repositories.MoneyRepository;
import br.com.cadastro.repositories.SecretyRepository;

@Service
public class CartaoService {

	private static final String REFEICAO = "refeicao";

	@Autowired
	private CartaoRepository cartaoRepository;

	@Autowired
	private SecretyRepository secretyRepository;

	@Autowired
	private MoneyRepository moneyRepository;

	private static final Logger logger = LogManager.getLogger(CartaoService.class);

	public ResponseEntity<Cartao> inclusao(CartaoRequest cartaoRequest) {
		logger.info("Iniciando a Chamada da Inclusao do Cartao");

		Cartao cartao = new Cartao();

		ResponseEntity<Cartao> validatorCartao = validatorCartao(cartaoRequest);
		if(validatorCartao.getBody() != null ){
			cartao.setError(validatorCartao.getBody().getError());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cartao);
		}

		ResponseEntity<Cartao> validatorCartaoExist = validatorCartaoExist(cartaoRequest);
		if(validatorCartaoExist.getBody() != null && !validatorCartaoExist.getBody().equals("") ) {
			cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
			cartao.setError(validatorCartaoExist.getBody().getError());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}

		try {

			cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
			cartao.setDescricao(REFEICAO);
			Cartao save = cartaoRepository.saveAndFlush(cartao);

			Secrety secrety = new Secrety();
			secrety.setSenha(cartaoRequest.getSenha());
			secrety.setSequenceCartao(save.getId().toString());
			Secrety sec = secretyRepository.saveAndFlush(secrety);
			save.setSecrety(sec);

			if(save != null && sec != null) {
				Credits credits = new Credits();
				credits.setSequenceCartao(save.getId().toString());
				credits.setValor(new BigDecimal("500.00"));;
				Credits money = moneyRepository.saveAndFlush(credits);;
				save.setCredits(money);
			}

			if(save != null && sec != null ) {
				return ResponseEntity.ok().body(save);
			}

		} catch (Exception e) {
			logger.info("Ocorreu um Erro na Execuçao" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cartao);
		}

		return ResponseEntity.noContent().build();

	}

	public ResponseEntity<Cartao> validatorCartao(CartaoRequest cartaoRequest) {
		logger.info("validatorCartao" );
		Cartao cartao = new Cartao();

		if(cartaoRequest.getNumeroCartao().toString().equals("")) {
			cartao.setError("CARTAO_INEXISTENTE");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cartao);
		}

		return ResponseEntity.noContent().build();
	}

	public ResponseEntity<Cartao> validatorCartaoExist(CartaoRequest cartaoRequest) {
		logger.info("validatorCartaoExist" );
		Cartao cartao = new Cartao();

		if(cartaoRequest.getNumeroCartao() != null && !cartaoRequest.getNumeroCartao().equals("")) {
			cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
			try {

				Optional<Cartao> exists = Optional.ofNullable(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao()));
				if(exists.isPresent() ) {
					cartao.setNumeroCartao(exists.get().getNumeroCartao());
					cartao.setError("CARTAO_INEXISTENTE");
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
				}

			} catch (Exception e) {
				cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
				cartao.setError("CARTAO_INEXISTENTE");
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
			}
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<Cartao> consultarCartao(String numeroCartao) {
		logger.info("validatorCartaoExist" );

		if(numeroCartao != null ) {

			Optional<Cartao> exists = Optional.ofNullable(cartaoRepository.findByNumeroCartao(numeroCartao ));
			if(exists.isPresent() ) {
				Long id = exists.get().getId();

				List<Credits> findBySequenceCartao = moneyRepository.findBySequenceCartao(id.toString() );

				Cartao cartao = new Cartao();
				cartao.setId(exists.get().getId());
				cartao.setNumeroCartao(exists.get().getNumeroCartao());
				cartao.setDescricao(exists.get().getDescricao());

				BigDecimal saldo = BigDecimal.ZERO;
				for (Credits credits : findBySequenceCartao) {
					saldo = saldo.add(credits.getValor()) ;

					Credits cred = new Credits();
					cred.setSequenceCartao(credits.getSequenceCartao());
					cred.setValor(saldo);
					cred.setDataTransacao(credits.getDataTransacao());

					cartao.setCredits(cred);
					cred.setSaldoCartao(saldo);
				}
				return ResponseEntity.ok().body(cartao);
			}
		}

		return ResponseEntity.notFound().build();

	}


	public ResponseEntity<Cartao> incluirCreditoCartao(RecargaRequest recargaRequest) {
		logger.info("validatorCartaoExist" );

		Cartao cartao = new Cartao();
		ResponseEntity<Cartao> valida = this.validatorSenha(recargaRequest);
		if(valida.getBody() != null && !valida.getBody().equals("") ) {
			cartao.setNumeroCartao(recargaRequest.getNumeroCartao());
			cartao.setError("SENHA_INVALIDA");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}

		if(recargaRequest.getNumeroCartao() != null ) {
			String numeroCartao = recargaRequest.getNumeroCartao();

			Optional<Cartao> exists = Optional.ofNullable(cartaoRepository.findByNumeroCartao(numeroCartao ));
			if(exists.isPresent() ) {
				Long id = exists.get().getId();

				cartao.setId(exists.get().getId());
				cartao.setNumeroCartao(exists.get().getNumeroCartao());
				cartao.setDescricao(exists.get().getDescricao());

				Credits credit = new Credits();
				credit.setSequenceCartao(id.toString());
				credit.setValor(new BigDecimal(recargaRequest.getValor()));
				credit.setTipoTransacao("Credito");

				String data = format();
				credit.setDataTransacao(data);
				credit.setSaldoCartao(BigDecimal.ZERO);
				Credits save = moneyRepository.save(credit);

				List<Credits> findBySequenceCartao = moneyRepository.findBySequenceCartao(save.getSequenceCartao().toString() );
				BigDecimal saldo = BigDecimal.ZERO;
				for (Credits credits : findBySequenceCartao) {
					saldo = saldo.add(credits.getValor()) ;
					credit.setSaldoCartao(saldo);
					cartao.setCredits(save);
				}

				return ResponseEntity.ok().body(cartao);

			}
		}

		return ResponseEntity.notFound().build();

	}


	public ResponseEntity<Cartao> debitarSaldoCartao(RecargaRequest recargaRequest) {
		logger.info("validatorCartaoExist" );
		Cartao cartao = new Cartao();
		ResponseEntity<Cartao> valida = this.validatorSenha(recargaRequest);
		if(valida.getBody() != null && !valida.getBody().equals("") ) {
			cartao.setNumeroCartao(recargaRequest.getNumeroCartao());
			cartao.setError("SENHA_INVALIDA");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
		}

		if(recargaRequest.getNumeroCartao() != null ) {

			Optional<Cartao> exists = Optional.ofNullable(cartaoRepository.findByNumeroCartao(recargaRequest.getNumeroCartao() ));
			if(exists.isPresent() ) {
				Long id = exists.get().getId();

				List<Credits> findBySequenceCartao = moneyRepository.findBySequenceCartao(id.toString() );

				cartao.setId(exists.get().getId());
				cartao.setNumeroCartao(exists.get().getNumeroCartao());
				cartao.setDescricao(exists.get().getDescricao());

				BigDecimal saldo = BigDecimal.ZERO;
				Credits cred = new Credits();
				for (Credits credits : findBySequenceCartao) {
					cred.setSequenceCartao(credits.getSequenceCartao());
					saldo = saldo.add(credits.getValor()) ;
				}

				if(new BigDecimal(recargaRequest.getValor()).compareTo(saldo) == 1) {
					cartao.setNumeroCartao(recargaRequest.getNumeroCartao());
					cartao.setError("SALDO_INSUFICIENTE");
					cartao.setDescricao(exists.get().getDescricao());
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
				}

				BigDecimal debito = BigDecimal.ZERO;
				debito = saldo.subtract(new BigDecimal(recargaRequest.getValor()));
				cred.setValor(new BigDecimal(recargaRequest.getValor()));
				cred.setTipoTransacao("Debito");
				cred.setSaldoCartao(debito);

				String data = format();
				cred.setDataTransacao(data);

				Credits save = moneyRepository.save(cred);
				cartao.setCredits(save);

				return ResponseEntity.ok().body(cartao);
			}
		}

		return ResponseEntity.notFound().build();

	}

	private String format() {
		Date date = new Date();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");
		String data = dt1.format(date);
		return data;
	}

	public ResponseEntity<Cartao> validatorSenha(RecargaRequest cartaoRequest) {
		logger.info("validatorCartaoExist" );
		Cartao cartao = new Cartao();

		if(cartaoRequest.getNumeroCartao() != null && !cartaoRequest.getNumeroCartao().equals("")) {
			cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
			try {

				Optional<Cartao> exists = Optional.ofNullable(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao()));
				if(exists.isPresent() ) {
					Long id = exists.get().getId();
					List<Secrety> findBySequenceCartao = secretyRepository.findBySequenceCartao(id.toString());
					
					if(findBySequenceCartao != null && !findBySequenceCartao.equals("") ) {
						for (Secrety secrety : findBySequenceCartao) {
							if(!secrety.getSenha().trim().equals(cartaoRequest.getSenha().trim())) {
								cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
								cartao.setError("SENHA_INVALIDA");
								return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
							}
						}
					}
				}
			} catch (Exception e) {
				cartao.setNumeroCartao(cartaoRequest.getNumeroCartao());
				cartao.setError("SENHA_INVALIDA");
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartao);
			}
		}
		return ResponseEntity.notFound().build();
	}



}
