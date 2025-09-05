package br.com.iff.marketplace.user.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.SellerStatus;
import br.com.iff.marketplace.user.enums.UserProfiles;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    public Page<User> findAll(
            boolean incluirInativos,
            Pageable pageable) {

        if (incluirInativos) {
            return userRepository.findAllEvenIfInactive(pageable);
        }
        else {
            return userRepository.findAll(pageable);
        }
    }

    public void deleteById(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuário não encontrado com o ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void toggleActivation(Long userId) {

        User user = userRepository.findByIdEvenIfInactive(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + userId));

        boolean estadoAtual = user.isActive();
        user.setActive(!estadoAtual);

        userRepository.save(user);
    }

    public Page<User> findPendingSellerRequests(Pageable pageable) {
        return userRepository.findBySellerStatus(SellerStatus.PENDING_APPROVAL, pageable);
    }

    @Transactional
    public void approveSellerRequest(Long userId) {

        User user = userRepository.findByIdEvenIfInactive(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + userId));

        if (user.getSellerStatus() != SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário não possui uma solicitação pendente para se tornar vendedor.");
        }

        user.setProfile(UserProfiles.SELLER);
        user.setSellerStatus(SellerStatus.APPROVED);
        userRepository.save(user);
    }

    @Transactional
    public void rejectSellerRequest(Long userId) {

        User user = userRepository.findByIdEvenIfInactive(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + userId));

        if (user.getSellerStatus() != SellerStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Este usuário não possui uma solicitação pendente.");
        }

        user.setSellerStatus(SellerStatus.REJECTED);
        userRepository.save(user);
    }

}
