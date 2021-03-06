package com.st4r.sky.services;


import com.st4r.sky.domain.Cliente;
import com.st4r.sky.domain.ItemPedido;
import com.st4r.sky.domain.PagamentoComBoleto;
import com.st4r.sky.domain.Pedido;
import com.st4r.sky.domain.enums.EstadoPagamento;
import com.st4r.sky.repositories.ItemPedidoRepository;
import com.st4r.sky.repositories.PagamentoRepository;
import com.st4r.sky.repositories.PedidoRepository;
import com.st4r.sky.security.UserSS;
import com.st4r.sky.services.exceptions.AuthorizationException;
import com.st4r.sky.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado. ID:" + id + ", Tipo:" + Pedido.class.getName()));
    }

    @Transactional
    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.find(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
        }
        obj = repository.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for (ItemPedido ip : obj.getItens()) {
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        emailService.sendOrderConfirmactionEmail(obj);
        return obj;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPage, String orderBy, String direction) {
        UserSS user = UserService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso negado");
        }

        PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
        Cliente cliente = clienteService.find(user.getId());

        return repository.findByCliente(cliente, pageRequest);
    }
}
