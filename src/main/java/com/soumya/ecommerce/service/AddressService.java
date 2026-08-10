package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AddressDTO;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    List<AddressDTO> getMyAddresses();

    AddressDTO addAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO);

    void deleteAddress(UUID addressId);
}
