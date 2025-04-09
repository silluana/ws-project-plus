package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.PaymentProcessDto;
import com.client.ws.projectplus.dto.wsraspay.CreditCardDto;
import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;
import com.client.ws.projectplus.dto.wsraspay.PaymentDto;
import com.client.ws.projectplus.exception.BusinessException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.integration.MailIntegration;
import com.client.ws.projectplus.integration.WsRaspayIntegration;
import com.client.ws.projectplus.mapper.UserPaymentInfoMapper;
import com.client.ws.projectplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.projectplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.projectplus.mapper.wsraspay.OrderMapper;
import com.client.ws.projectplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.projectplus.model.User;
import com.client.ws.projectplus.model.UserPaymentInfo;
import com.client.ws.projectplus.repository.UserPaymentInfoRepository;
import com.client.ws.projectplus.repository.UserRepository;
import com.client.ws.projectplus.service.PaymentInfoService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final UserRepository userRepository;
    private final UserPaymentInfoRepository userPaymentInfoRepository;
    private final WsRaspayIntegration wsRaspayIntegration;
    private final MailIntegration mailIntegration;

    PaymentInfoServiceImpl(UserRepository userRepository, UserPaymentInfoRepository userPaymentInfoRepository,
                           WsRaspayIntegration wsRaspayIntegration, MailIntegration mailIntegration) {
        this.userRepository = userRepository;
        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.wsRaspayIntegration = wsRaspayIntegration;
        this.mailIntegration = mailIntegration;
    }

    @Override
    public Boolean process(PaymentProcessDto dto) {
        //verificar se o usuario existe por id e se já possui assinatura
        var userOpt = userRepository.findById(dto.getUserPaymentInfoDto().getUserId());
        if(userOpt.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }
        User user = userOpt.get();
        if(Objects.nonNull(user.getSubscriptionType())){
            throw new BusinessException("Pagamento não pode ser processado, pois usuário já possui assinatura");
        }

        //cria ou atualizar usuario raspay
        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));

        //cria o pedido de pagamento
        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), dto));

        //processar o pagamento
        CreditCardDto creditCardDto = CreditCardMapper.build(dto.getUserPaymentInfoDto(), customerDto.getCpf());
        PaymentDto paymentDto = PaymentMapper.build(customerDto.getId(), orderDto.getId(), creditCardDto);
        Boolean successPayment = wsRaspayIntegration.processPayment(paymentDto);

        if(successPayment) {
            //salvar informações de pagamento
            UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(dto.getUserPaymentInfoDto(), user);
            userPaymentInfoRepository.save(userPaymentInfo);

            //enviar email. de criação de conta
            mailIntegration.send(user.getEmail(), "Acesso Liberado!",
                    "Olá, " + user.getName() + "\n" +
                            "Seu acesso foi liberado com sucesso!\n" +
                            "Usuario: " + user.getEmail() + " - Senha: alunoplus\n" +
                            "Acesse o sistema e aproveite todos os recursos disponíveis.\n" +
                            "Atenciosamente,\n" +
                            "Equipe ProjectPlus");
        }

        //retornar o sucesso ou não do pagamento

        return null;
    }
}
