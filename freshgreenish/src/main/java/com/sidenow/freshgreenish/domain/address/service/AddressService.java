package com.sidenow.freshgreenish.domain.address.service;

import com.sidenow.freshgreenish.domain.address.dto.GetAddressDTO;
import com.sidenow.freshgreenish.domain.address.dto.PostAddressDTO;
import com.sidenow.freshgreenish.domain.address.entity.Address;
import com.sidenow.freshgreenish.domain.address.repository.AddressRepository;
import com.sidenow.freshgreenish.domain.purchase.dto.AddressInfo;
import com.sidenow.freshgreenish.domain.purchase.entity.Purchase;
import com.sidenow.freshgreenish.domain.purchase.service.PurchaseDbService;
import com.sidenow.freshgreenish.domain.user.entity.User;
import com.sidenow.freshgreenish.domain.user.repository.UserRepository;
import com.sidenow.freshgreenish.domain.user.service.UserDbService;
import com.sidenow.freshgreenish.global.exception.BusinessLogicException;
import com.sidenow.freshgreenish.global.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressDbService addressDbService;
    private final UserDbService userDbService;
    private final PurchaseDbService purchaseDbService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    @Transactional
    public  List<GetAddressDTO> readAddress(@AuthenticationPrincipal OAuth2User oauth) {
        String userEmail = oauth.getAttribute("email");
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(
                        NullPointerException::new
                );
        Address defaultAddress = addressRepository.findByUserAndIsDefaultAddressAndIsInMyPageAndDeleted(user, true, true, false);
        List<Address> OtherAddresses = addressRepository.findAllByUserAndIsDefaultAddressAndIsInMyPageAndDeleted(user, false, true, false);

        List<GetAddressDTO> Addresses = new ArrayList<>();
        if (defaultAddress == null){
            GetAddressDTO mainAdressDTO = new GetAddressDTO(" ", " ",true);
            Addresses.add(mainAdressDTO);
        }else{
            GetAddressDTO mainAdressDTO = new GetAddressDTO();
            mainAdressDTO.setAddressId(defaultAddress.getAddressId());
            mainAdressDTO.setAddressNickname(defaultAddress.getAddressNickname());
            mainAdressDTO.setAddressMain(defaultAddress.getAddressMain());
            mainAdressDTO.setIsDefaultAddress(defaultAddress.getIsDefaultAddress());
            mainAdressDTO.setRecipientName(defaultAddress.getRecipientName());
            mainAdressDTO.setPhoneNumber(defaultAddress.getPhoneNumber());

            Addresses.add(mainAdressDTO);
        }

        if (OtherAddresses == null){
            GetAddressDTO newAdressDTO = new GetAddressDTO(" ", " ",false);
            Addresses.add(newAdressDTO);
        }else{
            for (Address a:OtherAddresses){
                GetAddressDTO newAdressDTO = new GetAddressDTO();
                newAdressDTO.setAddressId(a.getAddressId());
                newAdressDTO.setAddressNickname(a.getAddressNickname());
                newAdressDTO.setAddressMain(a.getAddressMain());
                newAdressDTO.setIsDefaultAddress(a.getIsDefaultAddress());
                newAdressDTO.setRecipientName(defaultAddress.getRecipientName());
                newAdressDTO.setPhoneNumber(defaultAddress.getPhoneNumber());

                Addresses.add(newAdressDTO);
            }
        }


        return Addresses;
    }

    @Transactional
    public void createAddress(@AuthenticationPrincipal OAuth2User oauth, PostAddressDTO dto, Boolean isInMyPage) {
        String userEmail = oauth.getAttribute("email");
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(
                        NullPointerException::new
                );
        System.out.println(dto.toString());

        if (dto.getIsDefaultAddress() == true){
            if (isDefaultAddressOnlyOne(oauth) == true){
                Address address = Address.builder()
                        .user(user)
                        .postalCode(dto.getPostalCode())
                        .addressMain(dto.getAddressMain())
                        .addressDetail(dto.getAddressDetail())
                        .addressNickname(dto.getAddressNickname())
                        .isDefaultAddress(dto.getIsDefaultAddress())
                        .isInMyPage(isInMyPage)
                        .recipientName(dto.getRecipientName())
                        .phoneNumber(dto.getPhoneNumber())
                        .build();

                addressRepository.save(address);
            } else{
                throw new BusinessLogicException(ExceptionCode.DEFAULT_ADDRESS_CAN_BE_ONLY_ONE);
            }
        } else{
            Address address = Address.builder()
                    .user(user)
                    .postalCode(dto.getPostalCode())
                    .addressMain(dto.getAddressMain())
                    .addressDetail(dto.getAddressDetail())
                    .addressNickname(dto.getAddressNickname())
                    .isDefaultAddress(dto.getIsDefaultAddress())
                    .isInMyPage(isInMyPage)
                    .recipientName(dto.getRecipientName())
                    .phoneNumber(dto.getPhoneNumber())
                    .build();

            addressRepository.save(address);
        }

    }

    @Transactional
    public void updateAddress(@AuthenticationPrincipal OAuth2User oauth, Long addressId, PostAddressDTO dto) {
        Address UpdateAddress = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_FOUND));

        UpdateAddress.setAddressNickname(dto.getAddressNickname());
        UpdateAddress.setPostalCode(dto.getPostalCode());
        UpdateAddress.setAddressMain(dto.getAddressMain());
        UpdateAddress.setAddressDetail(dto.getAddressDetail());
        UpdateAddress.setRecipientName(dto.getRecipientName());
        UpdateAddress.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getIsDefaultAddress() == true) {
            if (isDefaultAddressOnlyOne(oauth) == true) {
                UpdateAddress.setIsDefaultAddress(dto.getIsDefaultAddress());

                addressRepository.save(UpdateAddress);
            } else{
                throw new BusinessLogicException(ExceptionCode.DEFAULT_ADDRESS_CAN_BE_ONLY_ONE);
            }
        } else{
            UpdateAddress.setIsDefaultAddress(dto.getIsDefaultAddress());

            addressRepository.save(UpdateAddress);
        }

    }

    private boolean isDefaultAddressOnlyOne(@AuthenticationPrincipal OAuth2User oauth){
        String userEmail = oauth.getAttribute("email");
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(
                        NullPointerException::new
                );
        List<Address> defaultaddresslist= addressRepository.findAllByUserAndIsDefaultAddress(user, true);
        if (defaultaddresslist.size() >= 1) {
            return false;
        } else{
            return true;
        }
    }

    @Transactional
    public void deleteAddress(Long addressId){
        Address address = addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_FOUND));

        if (address.getDeleted().equals(true)) {
            throw new BusinessLogicException(ExceptionCode.ADDRESS_ALREADY_DELETE);
        }
        address.setDeleted(true);
        addressRepository.save(address);
    }

    @Transactional
    public void addAddressForPurchase(OAuth2User oauth, Long purchaseId, PostAddressDTO post) {
        User findUser = userDbService.findUserByEmail(oauth);
        Address address = Address.builder()
                .user(findUser)
                .postalCode(post.getPostalCode())
                .addressMain(post.getAddressMain())
                .addressDetail(post.getAddressDetail())
                .addressNickname(post.getAddressNickname())
                .isInMyPage(post.getIsDefaultAddress())
                .isDefaultAddress(post.getIsDefaultAddress())
                .recipientName(post.getRecipientName())
                .phoneNumber(post.getPhoneNumber())
                .build();

        address.setIsInMyPage(false);
        addressDbService.saveAddress(address);

        Purchase findPurchase = purchaseDbService.ifExistsReturnPurchase(purchaseId);
        findPurchase.setAddressId(address.getAddressId());

        purchaseDbService.savePurchase(findPurchase);
    }

    @Transactional
    public void addAddressIdForPurchase(OAuth2User oauth, Long purchaseId, Long addressId) {
        Long userId = userDbService.findUserIdByOauth(oauth);
        Address findAddress = addressDbService.ifExistsReturnAddress(addressId);
        Purchase findPurchase = purchaseDbService.ifExistsReturnPurchase(purchaseId);

        if (!userId.equals(findAddress.getUser().getUserId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        findPurchase.setAddressId(findAddress.getAddressId());
        purchaseDbService.savePurchase(findPurchase);
    }

    public AddressInfo getAddressForPurchase(OAuth2User oauth, Long purchaseId) {
        Long userId = userDbService.findUserIdByOauth(oauth);
        Purchase findPurchase = purchaseDbService.ifExistsReturnPurchase(purchaseId);

        if (!userId.equals(findPurchase.getUserId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        return purchaseDbService.getAddressInfo(findPurchase.getAddressId());
    }
}
