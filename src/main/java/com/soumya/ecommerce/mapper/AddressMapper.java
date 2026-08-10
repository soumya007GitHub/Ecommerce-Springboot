package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.AddressDTO;
import com.soumya.ecommerce.entity.Address;
import com.soumya.ecommerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressDTO dto, User user) {

        Address address = new Address();
        address.setLabel(dto.getLabel());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setContactPhone(dto.getContactPhone());
        address.setDefault(dto.isDefault());
        address.setUser(user);

        return address;
    }

    public AddressDTO toDto(Address address) {

        return AddressDTO.builder()
                .id(address.getId())
                .label(address.getLabel())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .contactPhone(address.getContactPhone())
                .isDefault(address.isDefault())
                .build();
    }
}
