package com.renatmirzoev.wallet.mapper;

import com.renatmirzoev.wallet.model.entity.Player;
import com.renatmirzoev.wallet.model.rest.CreatePlayerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

    Player fromCreateRequest(CreatePlayerRequest request);
}
