package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AddressDTO;
import com.soumya.ecommerce.entity.Address;
import com.soumya.ecommerce.entity.User;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.AddressMapper;
import com.soumya.ecommerce.repository.AddressRepository;
import com.soumya.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> getMyAddresses() {

        User user = SecurityUtils.getCurrentUser();

        return addressRepository.findByUserId(user.getId()).stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {

        User user = SecurityUtils.getCurrentUser();

        if (addressDTO.isDefault()) {
            clearExistingDefault(user.getId());
        }

        Address address = addressMapper.toEntity(addressDTO, user);

        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDto(savedAddress);
    }

    @Override
    public AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO) {

        User user = SecurityUtils.getCurrentUser();

        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Address", addressId));

        if (addressDTO.isDefault() && !address.isDefault()) {
            clearExistingDefault(user.getId());
        }

        address.setLabel(addressDTO.getLabel());
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        address.setContactPhone(addressDTO.getContactPhone());
        address.setDefault(addressDTO.isDefault());

        Address updatedAddress = addressRepository.save(address);

        return addressMapper.toDto(updatedAddress);
    }

    @Override
    public void deleteAddress(UUID addressId) {

        User user = SecurityUtils.getCurrentUser();

        Address address = addressRepository.findByIdAndUserId(addressId, user.getId())
                .orElseThrow(() -> ResourceNotFoundException.of("Address", addressId));

        addressRepository.delete(address);
    }

    private void clearExistingDefault(UUID userId) {

        addressRepository.findByUserId(userId).forEach(existing -> {
            if (existing.isDefault()) {
                existing.setDefault(false);
                addressRepository.save(existing);
            }
        });
    }
}
