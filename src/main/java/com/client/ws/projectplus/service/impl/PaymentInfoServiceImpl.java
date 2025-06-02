package com.client.ws.projectplus.service.impl;

import com.client.ws.projectplus.dto.PaymentProcessDto;
import com.client.ws.projectplus.dto.wsraspay.CreditCardDto;
import com.client.ws.projectplus.dto.wsraspay.CustomerDto;
import com.client.ws.projectplus.dto.wsraspay.OrderDto;
import com.client.ws.projectplus.dto.wsraspay.PaymentDto;
import com.client.ws.projectplus.enums.UserTypeEnum;
import com.client.ws.projectplus.exception.BusinessException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.integration.MailIntegration;
import com.client.ws.projectplus.integration.WsRaspayIntegration;
import com.client.ws.projectplus.mapper.UserPaymentInfoMapper;
import com.client.ws.projectplus.mapper.wsraspay.CreditCardMapper;
import com.client.ws.projectplus.mapper.wsraspay.CustomerMapper;
import com.client.ws.projectplus.mapper.wsraspay.OrderMapper;
import com.client.ws.projectplus.mapper.wsraspay.PaymentMapper;
import com.client.ws.projectplus.model.jpa.User;
import com.client.ws.projectplus.model.jpa.UserCredentials;
import com.client.ws.projectplus.model.jpa.UserPaymentInfo;
import com.client.ws.projectplus.repository.jpa.*;
import com.client.ws.projectplus.service.PaymentInfoService;
import com.client.ws.projectplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    @Value("${webservice.projectplus.default.password}")
    private String defaultPassword;

    private final UserRepository userRepository;
    private final UserPaymentInfoRepository userPaymentInfoRepository;
    private final WsRaspayIntegration wsRaspayIntegration;
    private final MailIntegration mailIntegration;
    private final UserDetailsRepository userDetailsRepository;
    private final UserTypeRepository userTypeRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;

    PaymentInfoServiceImpl(UserRepository userRepository, UserPaymentInfoRepository userPaymentInfoRepository,
                           WsRaspayIntegration wsRaspayIntegration, MailIntegration mailIntegration,
                           UserDetailsRepository userDetailsRepository, UserTypeRepository userTypeRepository,
                           SubscriptionTypeRepository subscriptionTypeRepository) {
        this.userRepository = userRepository;
        this.userPaymentInfoRepository = userPaymentInfoRepository;
        this.wsRaspayIntegration = wsRaspayIntegration;
        this.mailIntegration = mailIntegration;
        this.userDetailsRepository = userDetailsRepository;
        this.userTypeRepository = userTypeRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    @Override
    public Boolean process(PaymentProcessDto dto) {
        var userOpt = userRepository.findById(dto.getUserPaymentInfoDto().getUserId());
        if(userOpt.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }
        User user = userOpt.get();
        if(Objects.nonNull(user.getSubscriptionType())){
            throw new BusinessException("Pagamento não pode ser processado, pois usuário já possui assinatura");
        }

        Boolean successPayment = getSuccessPayment(dto, user);

        return createUserCredentials(dto, user, successPayment);
    }

    private boolean createUserCredentials(PaymentProcessDto dto, User user, Boolean successPayment) {
        if(Boolean.TRUE.equals(successPayment)) {
            UserPaymentInfo userPaymentInfo = UserPaymentInfoMapper.fromDtoToEntity(dto.getUserPaymentInfoDto(), user);
            userPaymentInfoRepository.save(userPaymentInfo);

            var userTypeOpt = userTypeRepository.findById(UserTypeEnum.ALUNO.getId());
            if (userTypeOpt.isEmpty()) {
                throw new NotFoundException("Tipo de usuário não encontrado");
            }
            UserCredentials userCredentials = new UserCredentials(null, user.getEmail(), PasswordUtils.encode(defaultPassword), userTypeOpt.get());
            userDetailsRepository.save(userCredentials);

            var subscriptionTypeOpt = subscriptionTypeRepository.findByProductKey(dto.getProductKey());
            if (subscriptionTypeOpt.isEmpty()) {
                throw new NotFoundException("Tipo de assinatura não encontrado");
            }
            user.setSubscriptionType(subscriptionTypeOpt.get());
            userRepository.save(user);

            mailIntegration.send(user.getEmail(), "Acesso Liberado!",
                    "Olá, " + user.getName() + "\n" +
                            "Seu acesso foi liberado com sucesso!\n" +
                            "Usuario: " + user.getEmail() + " - Senha: " + defaultPassword + "\n" +
                            "Acesse o sistema e aproveite todos os recursos disponíveis.\n" +
                            "Atenciosamente,\n" +
                            "Equipe ProjectPlus");
            return true;
        }

        return false;
    }

    private Boolean getSuccessPayment(PaymentProcessDto dto, User user) {
        CustomerDto customerDto = wsRaspayIntegration.createCustomer(CustomerMapper.build(user));
        OrderDto orderDto = wsRaspayIntegration.createOrder(OrderMapper.build(customerDto.getId(), dto));
        CreditCardDto creditCardDto = CreditCardMapper.build(dto.getUserPaymentInfoDto(), customerDto.getCpf());
        PaymentDto paymentDto = PaymentMapper.build(customerDto.getId(), orderDto.getId(), creditCardDto);
        return wsRaspayIntegration.processPayment(paymentDto);
    }
}
